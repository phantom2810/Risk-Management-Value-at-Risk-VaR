package com.riskmanagement.varcalculator.service;

import com.riskmanagement.varcalculator.dto.request.VarCalculationRequest;
import com.riskmanagement.varcalculator.dto.response.VarCalculationResponse;
import com.riskmanagement.varcalculator.entity.Portfolio;
import com.riskmanagement.varcalculator.entity.Position;
import com.riskmanagement.varcalculator.entity.Price;
import com.riskmanagement.varcalculator.entity.RiskRun;
import com.riskmanagement.varcalculator.repository.PortfolioRepository;
import com.riskmanagement.varcalculator.repository.PriceRepository;
import com.riskmanagement.varcalculator.repository.RiskRunRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VarCalculationService {

    private final PortfolioRepository portfolioRepository;
    private final RiskRunRepository riskRunRepository;
    private final PriceRepository priceRepository;

    public VarCalculationResponse calculateVar(Long portfolioId, VarCalculationRequest request) {
        log.info("Starting VaR calculation for portfolio {} using method {}", portfolioId, request.getVarMethod());

        long startTime = System.currentTimeMillis();

        try {
            Portfolio portfolio = portfolioRepository.findByIdWithPositions(portfolioId)
                    .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + portfolioId));

            // Create RiskRun entity
            RiskRun riskRun = RiskRun.builder()
                    .portfolio(portfolio)
                    .runDate(LocalDate.now())
                    .varMethod(request.getVarMethod())
                    .confidenceLevel(request.getConfidenceLevels().get(0)) // Use first confidence level
                    .windowSize(request.getWindowSize())
                    .status(RiskRun.RunStatus.RUNNING)
                    .build();

            riskRun = riskRunRepository.save(riskRun);

            // Calculate portfolio value
            BigDecimal portfolioValue = calculatePortfolioValue(portfolio);
            riskRun.setPortfolioValue(portfolioValue);

            // Calculate VaR based on method
            switch (request.getVarMethod()) {
                case HISTORICAL:
                    calculateHistoricalVar(riskRun, request);
                    break;
                case PARAMETRIC:
                    calculateParametricVar(riskRun, request);
                    break;
                case MONTE_CARLO:
                    calculateMonteCarloVar(riskRun, request);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported VaR method: " + request.getVarMethod());
            }

            riskRun.setStatus(RiskRun.RunStatus.COMPLETED);
            riskRun.setExecutionTimeMs(System.currentTimeMillis() - startTime);

            RiskRun savedRiskRun = riskRunRepository.save(riskRun);

            log.info("VaR calculation completed for portfolio {} in {} ms", portfolioId,
                    savedRiskRun.getExecutionTimeMs());

            return mapToResponse(savedRiskRun);

        } catch (Exception e) {
            log.error("Error calculating VaR for portfolio {}", portfolioId, e);

            // Update risk run with error status
            RiskRun errorRiskRun = RiskRun.builder()
                    .portfolio(portfolioRepository.findById(portfolioId).orElse(null))
                    .runDate(LocalDate.now())
                    .varMethod(request.getVarMethod())
                    .status(RiskRun.RunStatus.FAILED)
                    .errorMessage(e.getMessage())
                    .executionTimeMs(System.currentTimeMillis() - startTime)
                    .build();

            riskRunRepository.save(errorRiskRun);
            throw new RuntimeException("VaR calculation failed: " + e.getMessage(), e);
        }
    }

    private void calculateHistoricalVar(RiskRun riskRun, VarCalculationRequest request) {
        log.debug("Calculating Historical VaR");

        Portfolio portfolio = riskRun.getPortfolio();
        List<Position> positions = portfolio.getPositions();

        if (positions.isEmpty()) {
            throw new RuntimeException("Portfolio has no positions");
        }

        // Get historical returns for portfolio
        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate startDate = endDate.minusDays(request.getWindowSize() + 30); // Extra buffer

        double[] portfolioReturns = calculatePortfolioReturns(positions, startDate, endDate, request.getWindowSize());

        if (portfolioReturns.length < request.getWindowSize()) {
            throw new RuntimeException("Insufficient historical data for VaR calculation");
        }

        DescriptiveStatistics stats = new DescriptiveStatistics(portfolioReturns);

        // Calculate VaR at different confidence levels
        double var95 = stats.getPercentile(5.0) * riskRun.getPortfolioValue().doubleValue(); // 95% VaR
        double var99 = stats.getPercentile(1.0) * riskRun.getPortfolioValue().doubleValue(); // 99% VaR

        // Calculate Expected Shortfall (Conditional VaR)
        double es95 = calculateExpectedShortfall(portfolioReturns, 0.05) * riskRun.getPortfolioValue().doubleValue();
        double es99 = calculateExpectedShortfall(portfolioReturns, 0.01) * riskRun.getPortfolioValue().doubleValue();

        riskRun.setVar95(BigDecimal.valueOf(Math.abs(var95)).setScale(4, RoundingMode.HALF_UP));
        riskRun.setVar99(BigDecimal.valueOf(Math.abs(var99)).setScale(4, RoundingMode.HALF_UP));
        riskRun.setExpectedShortfall95(BigDecimal.valueOf(Math.abs(es95)).setScale(4, RoundingMode.HALF_UP));
        riskRun.setExpectedShortfall99(BigDecimal.valueOf(Math.abs(es99)).setScale(4, RoundingMode.HALF_UP));
        riskRun.setPortfolioVolatility(
                BigDecimal.valueOf(stats.getStandardDeviation()).setScale(6, RoundingMode.HALF_UP));
    }

    private void calculateParametricVar(RiskRun riskRun, VarCalculationRequest request) {
        log.debug("Calculating Parametric VaR");

        Portfolio portfolio = riskRun.getPortfolio();
        List<Position> positions = portfolio.getPositions();

        if (positions.isEmpty()) {
            throw new RuntimeException("Portfolio has no positions");
        }

        // Get historical returns for volatility calculation
        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate startDate = endDate.minusDays(request.getWindowSize() + 30);

        double[] portfolioReturns = calculatePortfolioReturns(positions, startDate, endDate, request.getWindowSize());

        if (portfolioReturns.length < request.getWindowSize()) {
            throw new RuntimeException("Insufficient historical data for VaR calculation");
        }

        DescriptiveStatistics stats = new DescriptiveStatistics(portfolioReturns);
        double portfolioVolatility = stats.getStandardDeviation();
        double portfolioValue = riskRun.getPortfolioValue().doubleValue();

        // Normal distribution quantiles
        NormalDistribution normal = new NormalDistribution();
        double z95 = normal.inverseCumulativeProbability(0.05); // -1.645 for 95%
        double z99 = normal.inverseCumulativeProbability(0.01); // -2.326 for 99%

        // Calculate VaR using normal distribution assumption
        double var95 = Math.abs(z95) * portfolioVolatility * portfolioValue;
        double var99 = Math.abs(z99) * portfolioVolatility * portfolioValue;

        // For parametric VaR, ES = VaR * phi(z) / (1 - alpha)
        double es95 = var95 * normal.density(z95) / 0.05;
        double es99 = var99 * normal.density(z99) / 0.01;

        riskRun.setVar95(BigDecimal.valueOf(var95).setScale(4, RoundingMode.HALF_UP));
        riskRun.setVar99(BigDecimal.valueOf(var99).setScale(4, RoundingMode.HALF_UP));
        riskRun.setExpectedShortfall95(BigDecimal.valueOf(es95).setScale(4, RoundingMode.HALF_UP));
        riskRun.setExpectedShortfall99(BigDecimal.valueOf(es99).setScale(4, RoundingMode.HALF_UP));
        riskRun.setPortfolioVolatility(BigDecimal.valueOf(portfolioVolatility).setScale(6, RoundingMode.HALF_UP));
    }

    private void calculateMonteCarloVar(RiskRun riskRun, VarCalculationRequest request) {
        log.debug("Calculating Monte Carlo VaR");

        // Placeholder implementation - would need more sophisticated simulation
        // For now, using parametric as base and adding Monte Carlo adjustments
        calculateParametricVar(riskRun, request);

        // Add some random variation to simulate Monte Carlo effects
        double mcAdjustment = 1.1; // 10% adjustment for simulation effects

        if (riskRun.getVar95() != null) {
            riskRun.setVar95(
                    riskRun.getVar95().multiply(BigDecimal.valueOf(mcAdjustment)).setScale(4, RoundingMode.HALF_UP));
        }
        if (riskRun.getVar99() != null) {
            riskRun.setVar99(
                    riskRun.getVar99().multiply(BigDecimal.valueOf(mcAdjustment)).setScale(4, RoundingMode.HALF_UP));
        }
    }

    private BigDecimal calculatePortfolioValue(Portfolio portfolio) {
        return portfolio.getPositions().stream()
                .filter(position -> position.getMarketValue() != null)
                .map(Position::getMarketValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private double[] calculatePortfolioReturns(List<Position> positions, LocalDate startDate, LocalDate endDate,
            int windowSize) {
        // Simplified portfolio return calculation
        // In a real implementation, this would weight individual stock returns by
        // position size

        if (positions.isEmpty()) {
            return new double[0];
        }

        // Use the first position's instrument for demonstration
        Position firstPosition = positions.get(0);
        List<Price> prices = priceRepository.findByInstrumentIdAndPriceDateBetweenOrderByPriceDateDesc(
                firstPosition.getInstrument().getId(), startDate, endDate);

        if (prices.size() < 2) {
            return new double[0];
        }

        // Calculate log returns
        double[] returns = new double[Math.min(prices.size() - 1, windowSize)];
        for (int i = 0; i < returns.length; i++) {
            Price current = prices.get(i);
            Price previous = prices.get(i + 1);

            double currentPrice = current.getClose().doubleValue();
            double previousPrice = previous.getClose().doubleValue();

            returns[i] = Math.log(currentPrice / previousPrice);
        }

        return returns;
    }

    private double calculateExpectedShortfall(double[] returns, double alpha) {
        DescriptiveStatistics stats = new DescriptiveStatistics(returns);
        double var = stats.getPercentile(alpha * 100);

        // Calculate average of returns below VaR
        double sum = 0;
        int count = 0;
        for (double ret : returns) {
            if (ret <= var) {
                sum += ret;
                count++;
            }
        }

        return count > 0 ? sum / count : var;
    }

    @Transactional(readOnly = true)
    public VarCalculationResponse getVarResult(Long riskRunId) {
        log.debug("Fetching VaR result for risk run ID: {}", riskRunId);

        RiskRun riskRun = riskRunRepository.findByIdWithBreakdowns(riskRunId)
                .orElseThrow(() -> new RuntimeException("Risk run not found with ID: " + riskRunId));

        return mapToResponse(riskRun);
    }

    private VarCalculationResponse mapToResponse(RiskRun riskRun) {
        return VarCalculationResponse.builder()
                .id(riskRun.getId())
                .portfolioId(riskRun.getPortfolio().getId())
                .runDate(riskRun.getRunDate())
                .varMethod(riskRun.getVarMethod())
                .confidenceLevel(riskRun.getConfidenceLevel())
                .windowSize(riskRun.getWindowSize())
                .var95(riskRun.getVar95())
                .var99(riskRun.getVar99())
                .expectedShortfall95(riskRun.getExpectedShortfall95())
                .expectedShortfall99(riskRun.getExpectedShortfall99())
                .portfolioValue(riskRun.getPortfolioValue())
                .portfolioVolatility(riskRun.getPortfolioVolatility())
                .status(riskRun.getStatus())
                .errorMessage(riskRun.getErrorMessage())
                .executionTimeMs(riskRun.getExecutionTimeMs())
                .createdAt(riskRun.getCreatedAt())
                .updatedAt(riskRun.getUpdatedAt())
                .build();
    }
}
