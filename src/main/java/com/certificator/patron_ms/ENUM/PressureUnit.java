package com.certificator.patron_ms.ENUM;

public enum PressureUnit {
    BAR, PA, PSI;

    public static boolean isValid(String unit) {
        for (PressureUnit u : values()) {
            if (u.name().equalsIgnoreCase(unit)) {
                return true;
            }
        }
        return false;
    }
}