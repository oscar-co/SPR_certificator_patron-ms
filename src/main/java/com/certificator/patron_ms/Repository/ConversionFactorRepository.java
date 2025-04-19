package com.certificator.patron_ms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.certificator.patron_ms.Model.ConversionFactor;
import java.util.Optional;

public interface ConversionFactorRepository extends JpaRepository<ConversionFactor, Long> {
    Optional<ConversionFactor> findByUEntradaAndUSalida(String uEntrada, String uSalida);
}