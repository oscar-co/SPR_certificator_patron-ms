package com.certificator.patron_ms.conversion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UncertaintyByPtnDTO {

    @NotBlank(message = "El identificador del patrón es obligatorio")
    private String nameIdentify;

    @NotBlank(message = "La unidad de entrada es obligatoria")
    private String inputUnit;

    @NotNull(message = "El valor de entrada es obligatorio")
    private Double inputValue;

    private Double outputValue;

    public UncertaintyByPtnDTO(String inputUnit, Double inputValue, String nameIdentify) {
        this.inputUnit = inputUnit;
        this.inputValue = inputValue;
        this.nameIdentify = nameIdentify;
    }

    public UncertaintyByPtnDTO(String inputUnit, Double inputValue, String nameIdentify, Double outputValue) {
        this.inputUnit = inputUnit;
        this.inputValue = inputValue;
        this.nameIdentify = nameIdentify;
        this.outputValue = outputValue;
    }

    public UncertaintyByPtnDTO() {
    }

    public String getInputUnit() { return inputUnit; }
    public void setInputUnit(String inputUnit) { this.inputUnit = inputUnit; }

    public String getNameIdentify() { return nameIdentify; }
    public void setNameIdentify(String nameIdentify) { this.nameIdentify = nameIdentify; }

    public Double getInputValue() { return inputValue; }
    public void setInputValue(Double inputValue) { this.inputValue = inputValue; }

    public Double getOutputValue() { return outputValue; }
    public void setOutputValue(Double outputValue) { this.outputValue = outputValue; }
}
