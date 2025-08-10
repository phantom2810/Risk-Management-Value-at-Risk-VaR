package com.riskmanagement.varcalculator.repository;

import com.riskmanagement.varcalculator.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findByStatus(Portfolio.PortfolioStatus status);

    @Query("SELECT p FROM Portfolio p WHERE UPPER(p.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    List<Portfolio> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT p FROM Portfolio p LEFT JOIN FETCH p.positions pos LEFT JOIN FETCH pos.instrument WHERE p.id = :id")
    Optional<Portfolio> findByIdWithPositions(@Param("id") Long id);

    @Query("SELECT p FROM Portfolio p LEFT JOIN FETCH p.riskRuns WHERE p.id = :id")
    Optional<Portfolio> findByIdWithRiskRuns(@Param("id") Long id);

    boolean existsByName(String name);
}
