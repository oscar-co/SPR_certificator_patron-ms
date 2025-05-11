package com.certificator.patron_ms.conversion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ConversionRequestDTO {

    @NotBlank(message = "La magnitud es obligatoria")
    private String magnitud;

    @NotBlank(message = "La unidad de entrada es obligatoria")
    private String inputUnit;

    @NotBlank(message = "La unidad de salida es obligatoria")
    private String outputUnit;

    @NotNull(message = "El valor de entrada es obligatorio")
    private Double inputValue;

    public ConversionRequestDTO() {}

    public ConversionRequestDTO(String magnitud, String inputUnit, String outputUnit, Double inputValue) {
        this.magnitud = magnitud;
        this.inputUnit = inputUnit;
        this.outputUnit = outputUnit;
        this.inputValue = inputValue;
    }

    public String getMagnitud() { return magnitud; }
    public void setMagnitud(String magnitud) { this.magnitud = magnitud; }

    public String getInputUnit() { return inputUnit; }
    public void setInputUnit(String inputUnit) { this.inputUnit = inputUnit; }

    public String getOutputUnit() { return outputUnit; }
    public void setOutputUnit(String outputUnit) { this.outputUnit = outputUnit; }

    public Double getInputValue() { return inputValue; }
    public void setInputValue(Double inputValue) { this.inputValue = inputValue; }
}
