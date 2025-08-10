package com.riskmanagement.varcalculator.repository;

import com.riskmanagement.varcalculator.entity.RiskRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RiskRunRepository extends JpaRepository<RiskRun, Long> {

    List<RiskRun> findByPortfolioId(Long portfolioId);

    List<RiskRun> findByPortfolioIdOrderByRunDateDesc(Long portfolioId);

    List<RiskRun> findByStatus(RiskRun.RunStatus status);

    List<RiskRun> findByVarMethod(RiskRun.VarMethod varMethod);

    @Query("SELECT r FROM RiskRun r WHERE r.portfolio.id = :portfolioId " +
            "AND r.runDate BETWEEN :startDate AND :endDate ORDER BY r.runDate DESC")
    List<RiskRun> findByPortfolioIdAndDateRange(@Param("portfolioId") Long portfolioId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT r FROM RiskRun r LEFT JOIN FETCH r.riskBreakdowns rb LEFT JOIN FETCH rb.instrument " +
            "WHERE r.id = :id")
    Optional<RiskRun> findByIdWithBreakdowns(@Param("id") Long id);

    Optional<RiskRun> findTopByPortfolioIdAndStatusOrderByRunDateDesc(
            Long portfolioId, RiskRun.RunStatus status);

    @Query("SELECT r FROM RiskRun r WHERE r.portfolio.id = :portfolioId " +
            "AND r.varMethod = :varMethod AND r.runDate = :runDate")
    Optional<RiskRun> findByPortfolioIdAndMethodAndDate(@Param("portfolioId") Long portfolioId,
            @Param("varMethod") RiskRun.VarMethod varMethod,
            @Param("runDate") LocalDate runDate);

    @Query("SELECT COUNT(r) FROM RiskRun r WHERE r.portfolio.id = :portfolioId AND r.status = :status")
    long countByPortfolioIdAndStatus(@Param("portfolioId") Long portfolioId,
            @Param("status") RiskRun.RunStatus status);
}
