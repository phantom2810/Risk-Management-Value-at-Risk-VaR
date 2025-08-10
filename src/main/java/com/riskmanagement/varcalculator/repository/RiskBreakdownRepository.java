package com.riskmanagement.varcalculator.repository;

import com.riskmanagement.varcalculator.entity.RiskBreakdown;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskBreakdownRepository extends JpaRepository<RiskBreakdown, Long> {

    List<RiskBreakdown> findByRiskRunId(Long riskRunId);

    List<RiskBreakdown> findByInstrumentId(Long instrumentId);

    @Query("SELECT rb FROM RiskBreakdown rb LEFT JOIN FETCH rb.instrument " +
            "WHERE rb.riskRun.id = :riskRunId ORDER BY rb.contributionPercentage DESC")
    List<RiskBreakdown> findByRiskRunIdWithInstrumentOrderByContribution(@Param("riskRunId") Long riskRunId);

    @Query("SELECT rb FROM RiskBreakdown rb WHERE rb.riskRun.id = :riskRunId " +
            "ORDER BY rb.contributionPercentage DESC")
    List<RiskBreakdown> findTopContributorsByRiskRunId(@Param("riskRunId") Long riskRunId);

    @Query("SELECT rb FROM RiskBreakdown rb WHERE rb.riskRun.id = :riskRunId " +
            "AND rb.contributionPercentage >= :minContribution " +
            "ORDER BY rb.contributionPercentage DESC")
    List<RiskBreakdown> findSignificantContributors(@Param("riskRunId") Long riskRunId,
            @Param("minContribution") Double minContribution);
}
