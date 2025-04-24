package com.certificator.patron_ms;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


import com.certificator.patron_ms.utils.Utils;

public class UtilsTest {

    @Test
    void capitalize_nullInput_returnsNull() {
        assertNull(Utils.capitalize(null), "Expected null when input is null");
    }

    @Test
    void capitalize_emptyInput_returnsEmpty() {
        assertEquals("", Utils.capitalize(""), "Expected empty string when input is empty");
    }

    @Test
    void readJsonFromFile_fileNotFound_throwsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Utils.readJsonFromFile("nonexistent.json");
        });
        assertTrue(exception.getMessage().contains("File not found"));
    }
}
