package com.riskmanagement.varcalculator.service;

import com.riskmanagement.varcalculator.dto.request.CreatePortfolioRequest;
import com.riskmanagement.varcalculator.dto.response.PortfolioResponse;
import com.riskmanagement.varcalculator.entity.Portfolio;
import com.riskmanagement.varcalculator.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioResponse createPortfolio(CreatePortfolioRequest request) {
        log.info("Creating new portfolio: {}", request.getName());

        Portfolio portfolio = Portfolio.builder()
                .name(request.getName())
                .description(request.getDescription())
                .baseCurrency(request.getBaseCurrency())
                .status(Portfolio.PortfolioStatus.ACTIVE)
                .build();

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        log.info("Created portfolio with ID: {}", savedPortfolio.getId());

        return mapToResponse(savedPortfolio);
    }

    @Transactional(readOnly = true)
    public PortfolioResponse getPortfolio(Long id) {
        log.debug("Fetching portfolio with ID: {}", id);

        Portfolio portfolio = portfolioRepository.findByIdWithPositions(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + id));

        return mapToResponse(portfolio);
    }

    @Transactional(readOnly = true)
    public List<PortfolioResponse> getAllPortfolios() {
        log.debug("Fetching all portfolios");

        return portfolioRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PortfolioResponse updatePortfolio(Long id, CreatePortfolioRequest request) {
        log.info("Updating portfolio with ID: {}", id);

        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + id));

        portfolio.setName(request.getName());
        portfolio.setDescription(request.getDescription());
        portfolio.setBaseCurrency(request.getBaseCurrency());

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        return mapToResponse(savedPortfolio);
    }

    public void deletePortfolio(Long id) {
        log.info("Deleting portfolio with ID: {}", id);

        if (!portfolioRepository.existsById(id)) {
            throw new RuntimeException("Portfolio not found with ID: " + id);
        }

        portfolioRepository.deleteById(id);
    }

    private PortfolioResponse mapToResponse(Portfolio portfolio) {
        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .name(portfolio.getName())
                .description(portfolio.getDescription())
                .baseCurrency(portfolio.getBaseCurrency())
                .status(portfolio.getStatus())
                .createdAt(portfolio.getCreatedAt())
                .updatedAt(portfolio.getUpdatedAt())
                .positionCount(portfolio.getPositions() != null ? portfolio.getPositions().size() : 0)
                .build();
    }
}
