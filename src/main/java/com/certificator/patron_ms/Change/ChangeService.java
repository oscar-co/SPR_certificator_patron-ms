package com.certificator.patron_ms.Change;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.certificator.patron_ms.Certificate.Certificate;
import com.certificator.patron_ms.Certificate.CertificateRepository;
import com.certificator.patron_ms.DTO.UncertaintyByPtnDTO;
import com.certificator.patron_ms.Exception.CertificateNotFoundException;
import com.certificator.patron_ms.utils.Utils;

@Service
public class ChangeService {

    private final CertificateRepository certificateRepository;
    private final ConversionFactorRepository conversionFactorRepository;
    private final UnitConversionService unitConversionService;
    
    public ChangeService(CertificateRepository certificateRepository, UnitConversionService unitConversionService, ConversionFactorRepository conversionFactorRepository) {
        this.certificateRepository = certificateRepository;
        this.unitConversionService = unitConversionService;
        this.conversionFactorRepository = conversionFactorRepository;
    }

    public List<Certificate> getPatronesByMeasure(Change request) {

        String magnitudFormatted = Utils.capitalize(request.getMagnitud().toLowerCase());
        ConversionResult conversionInputValue = unitConversionService.convertToReferenceUnit(magnitudFormatted, request.getInputUnit(), request.getInputValue());
        return certificateRepository.findMatchingCertificates( magnitudFormatted, conversionInputValue.getConvertedValue() );
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

    public List<String> getUnitsByMagnitu(String magnitud) throws Exception {
        if (magnitud == null || magnitud.isBlank()) {
            throw new Exception("Magnitud no introducida");
        }
        return conversionFactorRepository.findDistinctInputUnitsByMagnitud(magnitud);
    }
}
