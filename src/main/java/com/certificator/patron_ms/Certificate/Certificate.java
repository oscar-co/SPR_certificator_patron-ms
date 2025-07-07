package com.certificator.patron_ms.certificate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El numero del certificado es obligatorio")
    private String certificateNumber;
    
    @NotBlank(message = "El insType del certificado es obligatorio")
    private String insType;
    private String brand;
    private String model;

    @NotBlank(message = "El nameIdentify del certificado es obligatorio")
    private String nameIdentify;
    private String description;

    @NotBlank(message = "El identificador del certificado es obligatorio")
    private String unit;

    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDate issueDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("instrumentReading ASC")
    @JoinColumn(name = "certificate_id")
    private List<Measurement> measurements;
    //private List<Measurement> measurements = new ArrayList<>();
}
