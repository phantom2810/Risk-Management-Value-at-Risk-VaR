package com.riskmanagement.varcalculator.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePortfolioRequest {

    @NotBlank(message = "Portfolio name is required")
    private String name;

    private String description;

    private String baseCurrency = "USD";
}
