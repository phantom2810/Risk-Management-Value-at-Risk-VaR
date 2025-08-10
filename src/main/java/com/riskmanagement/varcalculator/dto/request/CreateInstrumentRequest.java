package com.riskmanagement.varcalculator.dto.request;

import com.riskmanagement.varcalculator.entity.Instrument;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateInstrumentRequest {

    @NotBlank(message = "Symbol is required")
    private String symbol;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Type is required")
    private Instrument.InstrumentType type;

    private String exchange;

    private String sector;

    private String currency = "USD";
}
