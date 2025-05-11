package com.certificator.patron_ms.shared.enums;

public enum LongitudeUnit {
    M, KM, MM, MI, FT, CM;

    public static LongitudeUnit fromString(String unit) {
        for (LongitudeUnit u : values()) {
            if (u.name().equalsIgnoreCase(unit)) {
                return u;
            }
        }
        throw new IllegalArgumentException("Longitude unit not supported: " + unit);
    }
}