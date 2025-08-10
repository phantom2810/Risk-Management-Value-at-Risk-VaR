package com.riskmanagement.varcalculator.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePositionRequest {

    @NotBlank(message = "Symbol is required")
    private String symbol;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    @Positive(message = "Average cost must be positive")
    private BigDecimal averageCost;

    private String instrumentName;

    private String instrumentType = "STOCK";

    private String exchange;

    private String sector;

    private String currency = "USD";
}
