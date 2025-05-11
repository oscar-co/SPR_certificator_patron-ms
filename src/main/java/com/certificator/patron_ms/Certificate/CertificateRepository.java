package com.certificator.patron_ms.certificate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {


    @Query(value = """
        SELECT DISTINCT c.* FROM certificate c
        JOIN measurement m ON c.id = m.certificate_id
        WHERE c.ins_type = :magnitud
        AND :valorEntrada BETWEEN (
            SELECT MIN(m2.reference) FROM measurement m2 WHERE m2.certificate_id = c.id
        ) AND (
            SELECT MAX(m2.reference) FROM measurement m2 WHERE m2.certificate_id = c.id
        )
        """
        , nativeQuery = true)

    List<Certificate> findMatchingCertificates(
        @Param("magnitud") String magnitud,
        @Param("valorEntrada") double valorEntrada
    );


    @Query("SELECT c.insType FROM Certificate c WHERE c.nameIdentify = :nameIdentify")
    String findMagnitudByNameIdentify(@Param("nameIdentify") String nameIdentify);


    @Query(value = """
        SELECT m.uncertainty
        FROM measurement m
        JOIN certificate c ON m.cert_id = c.certificate_number
        WHERE c.name_identify = :nameIdentify
        AND m.reference > :refValue
        ORDER BY m.reference ASC
        LIMIT 1
        """, nativeQuery = true)
    Optional<Double> findUncertaintyAboveReferenceByNameIdentify(
        @Param("nameIdentify") String nameIdentify,
        @Param("refValue") double refValue
    );

}