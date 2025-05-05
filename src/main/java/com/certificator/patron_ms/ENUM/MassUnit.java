package com.certificator.patron_ms.ENUM;

public enum MassUnit {
    G, KG, MG, LB;

    public static MassUnit fromString(String unit) {
        for (MassUnit u : values()) {
            if (u.name().equalsIgnoreCase(unit)) {
                return u;
            }
        }
        throw new IllegalArgumentException("Mass unit not supported: " + unit);
    }
}