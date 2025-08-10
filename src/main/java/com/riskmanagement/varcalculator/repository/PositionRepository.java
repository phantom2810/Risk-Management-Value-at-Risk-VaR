package com.riskmanagement.varcalculator.repository;

import com.riskmanagement.varcalculator.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findByPortfolioId(Long portfolioId);

    List<Position> findByInstrumentId(Long instrumentId);

    Optional<Position> findByPortfolioIdAndInstrumentId(Long portfolioId, Long instrumentId);

    @Query("SELECT p FROM Position p LEFT JOIN FETCH p.instrument WHERE p.portfolio.id = :portfolioId")
    List<Position> findByPortfolioIdWithInstrument(@Param("portfolioId") Long portfolioId);

    @Query("SELECT p FROM Position p WHERE p.portfolio.id = :portfolioId AND p.quantity > 0")
    List<Position> findActivePositionsByPortfolioId(@Param("portfolioId") Long portfolioId);

    @Query("SELECT COUNT(p) FROM Position p WHERE p.portfolio.id = :portfolioId AND p.quantity > 0")
    long countActivePositionsByPortfolioId(@Param("portfolioId") Long portfolioId);

    @Query("SELECT SUM(p.marketValue) FROM Position p WHERE p.portfolio.id = :portfolioId")
    Double getTotalMarketValueByPortfolioId(@Param("portfolioId") Long portfolioId);

    boolean existsByPortfolioIdAndInstrumentId(Long portfolioId, Long instrumentId);
}
