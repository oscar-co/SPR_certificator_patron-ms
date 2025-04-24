package com.certificator.patron_ms.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.certificator.patron_ms.DTO.UncertaintyByPtnDTO;
import com.certificator.patron_ms.Model.Certificate;
import com.certificator.patron_ms.Model.Change;
import com.certificator.patron_ms.Model.ConversionResult;
import com.certificator.patron_ms.Repository.CertificateRepository;
import com.certificator.patron_ms.utils.Utils;

@Service
public class ChangeService {

    private final CertificateRepository certificateRepository;
    private final UnitConversionService unitConversionService;
    
    public ChangeService(CertificateRepository certificateRepository, UnitConversionService unitConversionService) {
        this.certificateRepository = certificateRepository;
        this.unitConversionService = unitConversionService;
    }

    public List<Certificate> getPatronesByMeasure(Change request) {

        Double inputValue = request.getInputValue();

        if (inputValue == null || request.getMagnitud() == null || request.getInputUnit() == null) {
            throw new IllegalArgumentException("Faltan datos obligatorios en el cuerpo del JSON.");
        }
        var magnitudFormatted = Utils.capitalize(request.getMagnitud().toLowerCase());
        inputValue = UnitConversionService.calculoTemperatura(request.getInputUnit(), inputValue);
        return certificateRepository.findMatchingCertificates( magnitudFormatted, inputValue );
    }

    public Double getUncertaintyByPtnS(UncertaintyByPtnDTO request){

        String magnitud = certificateRepository.findMagnitudByNameIdentify(request.getNameIdentify());
        if (magnitud == null) {
            throw new IllegalArgumentException("No se encontró magnitud para el identificador: " + request.getNameIdentify());
        }
        String unit = request.getInputUnit();
        Double value = request.getInputValue();

        ConversionResult conversionResult = unitConversionService.convertToReferenceUnit(magnitud, unit, value);
        Optional<Double> uncertainty = certificateRepository.findUncertaintyAboveReferenceByNameIdentify(request.getNameIdentify(), conversionResult.getConvertedValue());
        return uncertainty.orElse(null);
    }
}
