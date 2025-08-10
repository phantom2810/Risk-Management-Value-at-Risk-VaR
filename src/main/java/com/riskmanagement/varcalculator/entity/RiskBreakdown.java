package com.riskmanagement.varcalculator.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "risk_breakdowns")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskBreakdown {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "risk_run_id", nullable = false)
    private RiskRun riskRun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

    @Column(name = "position_value", precision = 19, scale = 4, nullable = false)
    private BigDecimal positionValue;

    @Column(name = "weight", precision = 10, scale = 6, nullable = false)
    private BigDecimal weight;

    @Column(name = "marginal_var", precision = 19, scale = 4)
    private BigDecimal marginalVar;

    @Column(name = "component_var", precision = 19, scale = 4)
    private BigDecimal componentVar;

    @Column(name = "individual_var", precision = 19, scale = 4)
    private BigDecimal individualVar;

    @Column(name = "volatility", precision = 10, scale = 6)
    private BigDecimal volatility;

    @Column(name = "beta", precision = 10, scale = 6)
    private BigDecimal beta;

    @Column(name = "correlation", precision = 10, scale = 6)
    private BigDecimal correlation;

    @Column(name = "contribution_percentage", precision = 5, scale = 2)
    private BigDecimal contributionPercentage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
