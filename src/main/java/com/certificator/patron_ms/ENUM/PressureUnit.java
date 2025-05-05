package com.certificator.patron_ms.ENUM;

public enum PressureUnit {
    BAR, PA, PSI, MBAR, MMH2O, MMHG;

    public static PressureUnit fromString(String unit) {
        for (PressureUnit u : values()) {
            if (u.name().equalsIgnoreCase(unit)) {
                return u;
            }
        }
        throw new IllegalArgumentException("Pressure unit not supported: " + unit);
    }
}