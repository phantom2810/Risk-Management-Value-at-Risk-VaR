package com.riskmanagement.varcalculator.dto.response;

import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class RiskBreakdownResponse {

    private Long id;
    private Long riskRunId;
    private InstrumentResponse instrument;
    private BigDecimal positionValue;
    private BigDecimal weight;
    private BigDecimal marginalVar;
    private BigDecimal componentVar;
    private BigDecimal individualVar;
    private BigDecimal volatility;
    private BigDecimal beta;
    private BigDecimal correlation;
    private BigDecimal contributionPercentage;
    private LocalDateTime createdAt;
}
