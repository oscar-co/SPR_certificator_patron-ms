package com.certificator.patron_ms.ENUM;

public enum MagnitudeType {
    TEMPERATURA,
    PRESION,
    MASA,
    LONGITUD,
    AREA;

    public static MagnitudeType fromString(String value) {
        for (MagnitudeType m : values()) {
            if (m.name().equalsIgnoreCase(value)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Magnitud no reconocida: " + value);
    }
}