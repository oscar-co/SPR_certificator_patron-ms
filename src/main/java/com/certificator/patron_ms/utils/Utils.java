package com.certificator.patron_ms.utils;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.certificator.patron_ms.ENUM.MagnitudeType;

public class Utils {

    public static String readJsonFromFile(String relativePath) throws Exception {
        
        ClassLoader classLoader = Utils.class.getClassLoader();
        URL resource = classLoader.getResource(relativePath);

        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + relativePath);
        }
        return new String(Files.readAllBytes(Paths.get(classLoader.getResource(relativePath).toURI())));
    }

    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static String getDefaultReferenceUnit(MagnitudeType type) {
    return switch (type) {
        case TEMPERATURA -> "C";
        case PRESION -> "bar";
        case MASA -> "g";
        default -> throw new IllegalArgumentException("No hay unidad de referencia definida");
    };
}
}
