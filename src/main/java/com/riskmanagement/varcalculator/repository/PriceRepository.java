package com.riskmanagement.varcalculator.repository;

import com.riskmanagement.varcalculator.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

        List<Price> findByInstrumentIdOrderByPriceDateDesc(Long instrumentId);

        List<Price> findByInstrumentIdAndPriceDateBetweenOrderByPriceDateDesc(
                        Long instrumentId, LocalDate startDate, LocalDate endDate);

        @Query("SELECT p FROM Price p WHERE p.instrument.id = :instrumentId " +
                        "AND p.priceDate >= :fromDate ORDER BY p.priceDate DESC")
        List<Price> findByInstrumentIdFromDate(@Param("instrumentId") Long instrumentId,
                        @Param("fromDate") LocalDate fromDate);

        @Query(value = "SELECT * FROM prices p WHERE p.instrument_id = :instrumentId " +
                        "ORDER BY p.price_date DESC LIMIT :limit", nativeQuery = true)
        List<Price> findTopNByInstrumentId(@Param("instrumentId") Long instrumentId,
                        @Param("limit") int limit);

        Optional<Price> findTopByInstrumentIdOrderByPriceDateDesc(Long instrumentId);

        @Query("SELECT p FROM Price p WHERE p.instrument.symbol = :symbol " +
                        "AND p.priceDate BETWEEN :startDate AND :endDate ORDER BY p.priceDate")
        List<Price> findBySymbolAndDateRange(@Param("symbol") String symbol,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT p FROM Price p WHERE p.instrument.id IN :instrumentIds " +
                        "AND p.priceDate = :date")
        List<Price> findByInstrumentIdsAndDate(@Param("instrumentIds") List<Long> instrumentIds,
                        @Param("date") LocalDate date);

        boolean existsByInstrumentIdAndPriceDate(Long instrumentId, LocalDate priceDate);

        @Query("SELECT COUNT(p) FROM Price p WHERE p.instrument.id = :instrumentId")
        long countByInstrumentId(@Param("instrumentId") Long instrumentId);
}
