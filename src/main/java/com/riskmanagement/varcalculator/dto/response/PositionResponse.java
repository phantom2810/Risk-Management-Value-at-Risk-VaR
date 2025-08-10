package com.riskmanagement.varcalculator.dto.response;

import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PositionResponse {

    private Long id;
    private Long portfolioId;
    private InstrumentResponse instrument;
    private BigDecimal quantity;
    private BigDecimal averageCost;
    private BigDecimal marketValue;
    private BigDecimal weight;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
