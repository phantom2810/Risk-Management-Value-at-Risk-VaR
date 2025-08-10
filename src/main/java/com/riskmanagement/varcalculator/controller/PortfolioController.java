package com.riskmanagement.varcalculator.controller;

import com.riskmanagement.varcalculator.dto.request.CreatePortfolioRequest;
import com.riskmanagement.varcalculator.dto.response.PortfolioResponse;
import com.riskmanagement.varcalculator.service.PortfolioService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping
    public ResponseEntity<PortfolioResponse> createPortfolio(@Valid @RequestBody CreatePortfolioRequest request) {
        log.info("Received request to create portfolio: {}", request.getName());

        try {
            PortfolioResponse response = portfolioService.createPortfolio(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating portfolio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioResponse> getPortfolio(@PathVariable Long id) {
        log.info("Received request to get portfolio with ID: {}", id);

        try {
            PortfolioResponse response = portfolioService.getPortfolio(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Portfolio not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching portfolio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PortfolioResponse>> getAllPortfolios() {
        log.info("Received request to get all portfolios");

        try {
            List<PortfolioResponse> response = portfolioService.getAllPortfolios();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching portfolios", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PortfolioResponse> updatePortfolio(@PathVariable Long id,
            @Valid @RequestBody CreatePortfolioRequest request) {
        log.info("Received request to update portfolio with ID: {}", id);

        try {
            PortfolioResponse response = portfolioService.updatePortfolio(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Portfolio not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating portfolio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long id) {
        log.info("Received request to delete portfolio with ID: {}", id);

        try {
            portfolioService.deletePortfolio(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Portfolio not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting portfolio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
