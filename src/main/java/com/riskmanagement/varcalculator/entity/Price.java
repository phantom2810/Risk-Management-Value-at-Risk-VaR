package com.riskmanagement.varcalculator.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

    @Column(name = "price_date", nullable = false)
    private LocalDate priceDate;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal open;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal high;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal low;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal close;

    @Column(nullable = false)
    private Long volume;

    @Column(name = "adjusted_close", precision = 19, scale = 4)
    private BigDecimal adjustedClose;

    @Column(name = "log_return", precision = 19, scale = 8)
    private BigDecimal logReturn;

    @Column(name = "simple_return", precision = 19, scale = 8)
    private BigDecimal simpleReturn;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
