package com.riskmanagement.varcalculator.service;

import com.riskmanagement.varcalculator.dto.request.CreatePositionRequest;
import com.riskmanagement.varcalculator.dto.response.InstrumentResponse;
import com.riskmanagement.varcalculator.dto.response.PositionResponse;
import com.riskmanagement.varcalculator.entity.Instrument;
import com.riskmanagement.varcalculator.entity.Portfolio;
import com.riskmanagement.varcalculator.entity.Position;
import com.riskmanagement.varcalculator.repository.InstrumentRepository;
import com.riskmanagement.varcalculator.repository.PortfolioRepository;
import com.riskmanagement.varcalculator.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PositionService {

    private final PositionRepository positionRepository;
    private final PortfolioRepository portfolioRepository;
    private final InstrumentRepository instrumentRepository;

    public PositionResponse createPosition(Long portfolioId, CreatePositionRequest request) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + portfolioId));

        // Find or create instrument
        Instrument instrument = instrumentRepository.findBySymbol(request.getSymbol())
                .orElseGet(() -> createInstrument(request));

        Position position = Position.builder()
                .portfolio(portfolio)
                .instrument(instrument)
                .quantity(request.getQuantity())
                .averageCost(request.getAverageCost())
                .marketValue(request.getQuantity().multiply(request.getAverageCost()))
                .build();

        Position savedPosition = positionRepository.save(position);
        return mapToResponse(savedPosition);
    }

    public List<PositionResponse> uploadPositionsFromCsv(Long portfolioId, MultipartFile file) {
        // Verify portfolio exists
        portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + portfolioId));

        List<PositionResponse> positions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header line
                }

                String[] values = line.split(",");
                if (values.length >= 3) {
                    CreatePositionRequest request = new CreatePositionRequest();
                    request.setSymbol(values[0].trim());
                    request.setQuantity(new BigDecimal(values[1].trim()));
                    request.setAverageCost(new BigDecimal(values[2].trim()));
                    request.setInstrumentName(values[0].trim()); // Use symbol as name if no name provided

                    PositionResponse position = createPosition(portfolioId, request);
                    positions.add(position);
                }
            }
        } catch (Exception e) {
            log.error("Error processing CSV file", e);
            throw new RuntimeException("Error processing CSV file: " + e.getMessage());
        }

        return positions;
    }

    public List<PositionResponse> getPortfolioPositions(Long portfolioId) {
        List<Position> positions = positionRepository.findByPortfolioId(portfolioId);
        return positions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private Instrument createInstrument(CreatePositionRequest request) {
        Instrument instrument = Instrument.builder()
                .symbol(request.getSymbol())
                .name(request.getInstrumentName() != null ? request.getInstrumentName() : request.getSymbol())
                .type(Instrument.InstrumentType.valueOf(request.getInstrumentType()))
                .exchange(request.getExchange())
                .sector(request.getSector())
                .currency(request.getCurrency())
                .build();

        return instrumentRepository.save(instrument);
    }

    private PositionResponse mapToResponse(Position position) {
        InstrumentResponse instrumentResponse = InstrumentResponse.builder()
                .id(position.getInstrument().getId())
                .symbol(position.getInstrument().getSymbol())
                .name(position.getInstrument().getName())
                .type(position.getInstrument().getType())
                .exchange(position.getInstrument().getExchange())
                .sector(position.getInstrument().getSector())
                .currency(position.getInstrument().getCurrency())
                .createdAt(position.getInstrument().getCreatedAt())
                .updatedAt(position.getInstrument().getUpdatedAt())
                .build();

        return PositionResponse.builder()
                .id(position.getId())
                .portfolioId(position.getPortfolio().getId())
                .instrument(instrumentResponse)
                .quantity(position.getQuantity())
                .averageCost(position.getAverageCost())
                .marketValue(position.getMarketValue())
                .weight(position.getWeight())
                .createdAt(position.getCreatedAt())
                .updatedAt(position.getUpdatedAt())
                .build();
    }
}
