package com.certificator.patron_ms.utils;

import com.certificator.patron_ms.Certificate.Certificate;
import com.certificator.patron_ms.Change.Change;
import com.certificator.patron_ms.Change.ConversionResult;
import com.certificator.patron_ms.DTO.ChangeRequestDTO;
import com.certificator.patron_ms.DTO.ChangeResponseDTO;
import com.certificator.patron_ms.DTO.UncertaintyByPtnDTO;

public class TestDataFactory {

    public static ChangeRequestDTO buildValidChangeRequest() {
        return new ChangeRequestDTO("presion", "bar", "mbar", 34.0);
    }

    public static ChangeResponseDTO buildExpectedChangeResponse() {
        return new ChangeResponseDTO(34.0, 34000.0, "bar", "mbar", "presion");
    }

    public static ConversionResult buildConversionResult(Double value) {
        return new ConversionResult(value, 34.0, "mbar");
    }

    public static Change buildChangeRequest(String magnitud, String unit, Double value) {
        Change request = new Change();
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
