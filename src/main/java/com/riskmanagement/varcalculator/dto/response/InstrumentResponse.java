package com.riskmanagement.varcalculator.dto.response;

import com.riskmanagement.varcalculator.entity.Instrument;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
public class InstrumentResponse {

    private Long id;
    private String symbol;
    private String name;
    private Instrument.InstrumentType type;
    private String exchange;
    private String sector;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
