package com.certificator.patron_ms.ENUM;

public enum TemperatureUnit {
    C, K, F;

    public static boolean isValid(String unit) {
        for (TemperatureUnit u : values()) {
            if (u.name().equalsIgnoreCase(unit)) {
                return true;
            }
        }
        return false;
    }
}
