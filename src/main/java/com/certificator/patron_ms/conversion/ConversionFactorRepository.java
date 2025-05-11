package com.certificator.patron_ms.conversion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversionFactorRepository extends JpaRepository<ConversionFactor, Long> {
    Optional<ConversionFactor> findByUEntradaAndUSalida(String uEntrada, String uSalida);

    @Query("SELECT DISTINCT c.uEntrada FROM ConversionFactor c WHERE c.magnitud = :magnitud")
    List<String> findDistinctInputUnitsByMagnitud(@Param("magnitud") String magnitud);
}