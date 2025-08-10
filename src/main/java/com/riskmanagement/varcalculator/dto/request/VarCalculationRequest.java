package com.riskmanagement.varcalculator.dto.request;

import com.riskmanagement.varcalculator.entity.RiskRun;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class VarCalculationRequest {

    @NotNull(message = "VaR method is required")
    private RiskRun.VarMethod varMethod;

    @NotNull(message = "Confidence levels are required")
    private List<@DecimalMin(value = "0.01", message = "Confidence level must be at least 0.01") @DecimalMax(value = "0.99", message = "Confidence level must be at most 0.99") BigDecimal> confidenceLevels;

    @Min(value = 1, message = "Window size must be at least 1")
    private Integer windowSize = 252;

    private Integer monteCarloSimulations = 10000;
}
