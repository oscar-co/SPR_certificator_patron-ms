package com.certificator.patron_ms.utils;


import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {


    public static String readJsonFromFile(String relativePath) throws Exception {
        ClassLoader classLoader = Utils.class.getClassLoader();
        URL resource = classLoader.getResource(relativePath);

        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + relativePath);
        }
        return new String(Files.readAllBytes(Paths.get(classLoader.getResource(relativePath).toURI())));
    }

}
