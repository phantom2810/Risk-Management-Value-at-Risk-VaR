package com.riskmanagement.varcalculator.repository;

import com.riskmanagement.varcalculator.entity.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long> {

    Optional<Instrument> findBySymbol(String symbol);

    List<Instrument> findByType(Instrument.InstrumentType type);

    List<Instrument> findBySector(String sector);

    List<Instrument> findByExchange(String exchange);

    @Query("SELECT i FROM Instrument i WHERE i.symbol IN :symbols")
    List<Instrument> findBySymbolIn(@Param("symbols") List<String> symbols);

    @Query("SELECT i FROM Instrument i WHERE UPPER(i.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    List<Instrument> findByNameContainingIgnoreCase(@Param("name") String name);

    boolean existsBySymbol(String symbol);
}
