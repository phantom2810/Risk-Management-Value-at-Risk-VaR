package com.riskmanagement.varcalculator.dto.response;

import com.riskmanagement.varcalculator.entity.Portfolio;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PortfolioResponse {

    private Long id;
    private String name;
    private String description;
    private String baseCurrency;
    private Portfolio.PortfolioStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PositionResponse> positions;
    private Integer positionCount;
    private Double totalMarketValue;
}
