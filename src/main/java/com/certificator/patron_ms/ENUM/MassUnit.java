package com.certificator.patron_ms.ENUM;

public enum MassUnit {
    G, KG, MG;

    public static boolean isValid(String unit) {
        for (MassUnit u : values()) {
            if (u.name().equalsIgnoreCase(unit)) {
                return true;
            }
        }
        return false;
    }
}