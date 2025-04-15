package com.certificator.patron_ms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.certificator.patron_ms.Model.Certificate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;



public interface CertificateRepository extends JpaRepository<Certificate, Long> {


    @Query("""
        SELECT c FROM Certificate c
        JOIN c.measurements m
        WHERE c.insType = :magnitud
        AND m.instrumentReading > :valorEntrada
    """)

    List<Certificate> findMatchingCertificates(
        @Param("magnitud") String magnitud,
        @Param("valorEntrada") double valorEntrada
    );

}