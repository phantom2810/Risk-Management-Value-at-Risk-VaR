package com.riskmanagement.varcalculator.controller;

import com.riskmanagement.varcalculator.dto.request.VarCalculationRequest;
import com.riskmanagement.varcalculator.dto.response.VarCalculationResponse;
import com.riskmanagement.varcalculator.service.VarCalculationService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class VarCalculationController {

    private final VarCalculationService varCalculationService;

    @PostMapping("/portfolio/{portfolioId}/risk/run")
    public ResponseEntity<VarCalculationResponse> calculateVar(@PathVariable Long portfolioId,
            @Valid @RequestBody VarCalculationRequest request) {
        log.info("Received VaR calculation request for portfolio {} using method {}",
                portfolioId, request.getVarMethod());

        try {
            VarCalculationResponse response = varCalculationService.calculateVar(portfolioId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Error calculating VaR for portfolio {}", portfolioId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error calculating VaR", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/risk/{riskRunId}")
    public ResponseEntity<VarCalculationResponse> getVarResult(@PathVariable Long riskRunId) {
        log.info("Received request to get VaR result for risk run ID: {}", riskRunId);

        try {
            VarCalculationResponse response = varCalculationService.getVarResult(riskRunId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Risk run not found with ID: {}", riskRunId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching VaR result", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
