package com.certificator.patron_ms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.certificator.patron_ms.Model.Certificate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

}