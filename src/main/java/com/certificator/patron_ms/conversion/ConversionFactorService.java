package com.certificator.patron_ms.conversion;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.certificator.patron_ms.certificate.Certificate;
import com.certificator.patron_ms.certificate.CertificateRepository;
import com.certificator.patron_ms.conversion.dto.ConversionRequestDTO;
import com.certificator.patron_ms.conversion.dto.ConversionResponseDTO;
import com.certificator.patron_ms.conversion.dto.ConversionResultDTO;
import com.certificator.patron_ms.conversion.dto.UncertaintyByPtnDTO;
import com.certificator.patron_ms.shared.Exception.CertificateNotFoundException;
import com.certificator.patron_ms.shared.utils.Utils;


@Service
public class ConversionFactorService {

    private final CertificateRepository certificateRepository;
    private final ConversionFactorRepository conversionFactorRepository;
    private final UnitConversionService unitConversionService;
    
    public ConversionFactorService(CertificateRepository certificateRepository, UnitConversionService unitConversionService, ConversionFactorRepository conversionFactorRepository) {
        this.certificateRepository = certificateRepository;
        this.unitConversionService = unitConversionService;
        this.conversionFactorRepository = conversionFactorRepository;
    }

    public List<Certificate> getPatronesByMeasure(ConversionResponseDTO request) {

        String magnitudFormatted = Utils.capitalize(request.getMagnitud().toLowerCase());
        ConversionResultDTO conversionInputValue = unitConversionService.convertUnits(magnitudFormatted, request.getInputUnit(), null, request.getInputValue());
        return certificateRepository.findMatchingCertificates( magnitudFormatted, conversionInputValue.getConvertedValue() );
    }

    public Double getUncertaintyByPtnS(UncertaintyByPtnDTO request){

        String magnitud = certificateRepository.findMagnitudByNameIdentify(request.getNameIdentify());
        if (magnitud == null) {
            throw new CertificateNotFoundException("No se encontró magnitud para el identificador: " + request.getNameIdentify());
        }
        ConversionResultDTO conversionResult = unitConversionService.convertUnits(
            magnitud, 
            request.getInputUnit(), 
            null, 
            request.getInputValue()
        );
        Optional<Double> uncertainty = certificateRepository.findUncertaintyAboveReferenceByNameIdentify(
            request.getNameIdentify(), 
            conversionResult.getConvertedValue()
        );        
        return uncertainty.orElseThrow(() ->
            new CertificateNotFoundException("No se encontró incertidumbre para el valor convertido: " + conversionResult.getConvertedValue())
        );
    }

    public List<String> getUnitsByMagnitu(String magnitud) throws Exception {
        if (magnitud == null || magnitud.isBlank()) {
            throw new Exception("Magnitud no introducida");
        }
        return conversionFactorRepository.findDistinctInputUnitsByMagnitud(magnitud);
    }


    public ConversionResponseDTO convert(ConversionRequestDTO dto) throws Exception {

        ConversionResultDTO result = unitConversionService.convertUnits(dto.getMagnitud(), dto.getInputUnit(), dto.getOutputUnit(), dto.getInputValue());
        return new ConversionResponseDTO(
            dto.getMagnitud(),
            dto.getInputUnit(),
            dto.getOutputUnit(),
            dto.getInputValue(),
            result.getConvertedValue()
        );
    }

}
