package com.riskmanagement.varcalculator.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "risk_runs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(name = "run_date", nullable = false)
    private LocalDate runDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "var_method", nullable = false)
    private VarMethod varMethod;

    @Column(name = "confidence_level", precision = 5, scale = 4, nullable = false)
    private BigDecimal confidenceLevel;

    @Column(name = "window_size", nullable = false)
    private Integer windowSize;

    @Column(name = "var_95", precision = 19, scale = 4)
    private BigDecimal var95;

    @Column(name = "var_99", precision = 19, scale = 4)
    private BigDecimal var99;

    @Column(name = "expected_shortfall_95", precision = 19, scale = 4)
    private BigDecimal expectedShortfall95;

    @Column(name = "expected_shortfall_99", precision = 19, scale = 4)
    private BigDecimal expectedShortfall99;

    @Column(name = "portfolio_value", precision = 19, scale = 4, nullable = false)
    private BigDecimal portfolioValue;

    @Column(name = "portfolio_volatility", precision = 10, scale = 6)
    private BigDecimal portfolioVolatility;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RunStatus status = RunStatus.PENDING;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "riskRun", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<RiskBreakdown> riskBreakdowns = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum VarMethod {
        HISTORICAL,
        PARAMETRIC,
        MONTE_CARLO
    }

    public enum RunStatus {
        PENDING,
        RUNNING,
        COMPLETED,
        FAILED
    }
}
