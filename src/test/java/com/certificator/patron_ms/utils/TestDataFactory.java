package com.certificator.patron_ms.utils;

import com.certificator.patron_ms.conversion.dto.ConversionResponseDTO;
import com.certificator.patron_ms.certificate.Certificate;
import com.certificator.patron_ms.conversion.dto.ConversionRequestDTO;
import com.certificator.patron_ms.conversion.dto.ConversionResultDTO;
import com.certificator.patron_ms.conversion.dto.UncertaintyByPtnDTO;

public class TestDataFactory {

    public static ConversionRequestDTO buildValidChangeRequest() {
        return new ConversionRequestDTO("presion", "bar", "mbar", 34.0);
    }

    public static ConversionResponseDTO buildExpectedChangeResponse() {
        return new ConversionResponseDTO("presion",  "bar", "mbar", 34.0, 34000.0);
    }

    public static ConversionResultDTO buildConversionResult(Double value) {
        return new ConversionResultDTO(value, 34.0, "mbar");
    }

    public static ConversionResponseDTO buildChangeRequest(String magnitud, String unit, Double value) {
        ConversionResponseDTO request = new ConversionResponseDTO();
        request.setMagnitud(magnitud);
        request.setInputUnit(unit);
        request.setInputValue(value);
        return request;
    }

    public static Certificate buildCertificate(String certificateNumber) {
        Certificate cert = new Certificate();
        cert.setCertificateNumber(certificateNumber);
        return cert;
    }

    public static UncertaintyByPtnDTO buildUncertaintyRequest(String inputUnit, Double inputValue, String nameIdentify) {
        return new UncertaintyByPtnDTO(inputUnit, inputValue, nameIdentify);
    }
}
