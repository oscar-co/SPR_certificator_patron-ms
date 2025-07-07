package com.certificator.patron_ms.certificate;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "certificate_id")
    private Certificate certificate;

    private String certificateNumber;
    private BigDecimal reference;
    private BigDecimal instrumentReading;
    private BigDecimal correction;
    private BigDecimal uncertainty;
    private BigDecimal ambientTemp;
}
