package com.certificator.patron_ms.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.certificator.patron_ms.DTO.UncertaintyByPtnDTO;
import com.certificator.patron_ms.Exception.CertificateNotFoundException;
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

        var magnitudFormatted = Utils.capitalize(request.getMagnitud().toLowerCase());
        ConversionResult inputValue = unitConversionService.convertToReferenceUnit(magnitudFormatted,request.getInputUnit(), request.getInputValue());
        return certificateRepository.findMatchingCertificates( magnitudFormatted, inputValue.getConvertedValue() );
    }

    public Double getUncertaintyByPtnS(UncertaintyByPtnDTO request){

        String magnitud = certificateRepository.findMagnitudByNameIdentify(request.getNameIdentify());
        if (magnitud == null) {
            throw new CertificateNotFoundException("No se encontró magnitud para el identificador: " + request.getNameIdentify());
        }
        ConversionResult conversionResult = unitConversionService.convertToReferenceUnit(magnitud, request.getInputUnit(), request.getInputValue());
        Optional<Double> uncertainty = certificateRepository.findUncertaintyAboveReferenceByNameIdentify(
            request.getNameIdentify(), conversionResult.getConvertedValue());        
        return uncertainty.orElse(null);
    }
}
