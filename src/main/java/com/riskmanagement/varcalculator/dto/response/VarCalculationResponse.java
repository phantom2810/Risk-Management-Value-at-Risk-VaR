package com.riskmanagement.varcalculator.dto.response;

import com.riskmanagement.varcalculator.entity.RiskRun;
import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class VarCalculationResponse {

    private Long id;
    private Long portfolioId;
    private LocalDate runDate;
    private RiskRun.VarMethod varMethod;
    private BigDecimal confidenceLevel;
    private Integer windowSize;
    private BigDecimal var95;
    private BigDecimal var99;
    private BigDecimal expectedShortfall95;
    private BigDecimal expectedShortfall99;
    private BigDecimal portfolioValue;
    private BigDecimal portfolioVolatility;
    private RiskRun.RunStatus status;
    private String errorMessage;
    private Long executionTimeMs;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RiskBreakdownResponse> riskBreakdowns;
}
