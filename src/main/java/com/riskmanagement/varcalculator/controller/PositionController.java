package com.riskmanagement.varcalculator.controller;

import com.riskmanagement.varcalculator.dto.request.CreatePositionRequest;
import com.riskmanagement.varcalculator.dto.response.PositionResponse;
import com.riskmanagement.varcalculator.service.PositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PositionController {

    private final PositionService positionService;

    @PostMapping("/portfolio/{portfolioId}/positions")
    public ResponseEntity<PositionResponse> createPosition(@PathVariable Long portfolioId,
            @Valid @RequestBody CreatePositionRequest request) {
        log.info("Received request to create position for portfolio {}: {}", portfolioId, request.getSymbol());

        try {
            PositionResponse response = positionService.createPosition(portfolioId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating position", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/portfolio/{portfolioId}/positions/upload")
    public ResponseEntity<List<PositionResponse>> uploadPositions(@PathVariable Long portfolioId,
            @RequestParam("file") MultipartFile file) {
        log.info("Received request to upload positions for portfolio {} from file: {}", portfolioId,
                file.getOriginalFilename());

        try {
            List<PositionResponse> response = positionService.uploadPositionsFromCsv(portfolioId, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error uploading positions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/portfolio/{portfolioId}/positions")
    public ResponseEntity<List<PositionResponse>> getPortfolioPositions(@PathVariable Long portfolioId) {
        log.info("Received request to get positions for portfolio {}", portfolioId);

        try {
            List<PositionResponse> response = positionService.getPortfolioPositions(portfolioId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching positions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
