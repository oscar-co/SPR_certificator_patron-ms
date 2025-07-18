package com.certificator.patron_ms.conversion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "conversion_factor",
    uniqueConstraints = @UniqueConstraint(columnNames = {"uEntrada", "uSalida"})
)
public class ConversionFactor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "magnitud")
    private String magnitud;

    @Column(name = "u_entrada")
    private String uEntrada;

    @Column(name = "u_salida")
    private String uSalida;

    @Column(name = "factor")
    private double factor;

    // Getters y setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    
    public String getMagnitud() { return magnitud; }
    public void setMagnitud(String magnitud) { this.magnitud = magnitud; }

    public String getuEntrada() { return uEntrada; }
    public void setuEntrada(String uEntrada) { this.uEntrada = uEntrada; }

    public String getuSalida() { return uSalida; }
    public void setuSalida(String uSalida) { this.uSalida = uSalida; }

    public double getFactor() { return factor; }
    public void setFactor(double factor) { this.factor = factor; }
}
