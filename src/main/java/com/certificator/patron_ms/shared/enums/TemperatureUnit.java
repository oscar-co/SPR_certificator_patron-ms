package com.certificator.patron_ms.shared.enums;

public enum TemperatureUnit {
    C, F, K;

    public static TemperatureUnit fromString(String unit) {
        for (TemperatureUnit u : values()) {
            if (u.name().equalsIgnoreCase(unit)) {
                return u;
            }
        }
        throw new IllegalArgumentException("Temperature unit not supported: " + unit);
    }
}