package com.certificator.patron_ms.DTO;

public class UncertaintyByPtnDTO {

    private String nameIdentify;
    private String inputUnit;
    private Double inputValue;

    public UncertaintyByPtnDTO(String inputUnit, Double inputValue, String nameIdentify) {
        this.inputUnit = inputUnit;
        this.inputValue = inputValue;
        this.nameIdentify = nameIdentify;
    }

    public UncertaintyByPtnDTO() {
    }

    public String getInputUnit() { return inputUnit; }
    public void setInputUnit(String inputUnit) { this.inputUnit = inputUnit; }

    public String getNameIdentify() { return nameIdentify; }
    public void setNameIdentify(String nameIdentify) { this.nameIdentify = nameIdentify; }

    public Double getInputValue() { return inputValue; }
    public void setInputValue(Double inputValue) { this.inputValue = inputValue; }
}
