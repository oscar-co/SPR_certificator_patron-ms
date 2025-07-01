package com.certificator.patron_ms.shared.enums;

public enum AreaUnit {
    CM2, M2, KM2, MM2, HE, FT2, MI2, HA;

    public static AreaUnit fromString(String unit) {
        for (AreaUnit u : values()) {
            if (u.name().equalsIgnoreCase(unit.toUpperCase())) {
                return u;
            }
        }
        throw new IllegalArgumentException("Area unit not supported: " + unit);
    }
}