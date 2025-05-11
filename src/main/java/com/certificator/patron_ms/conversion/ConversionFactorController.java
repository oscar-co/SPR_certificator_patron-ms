package com.certificator.patron_ms.conversion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.certificator.patron_ms.Certificate.Certificate;
import com.certificator.patron_ms.conversion.dto.ApiResponse;
import com.certificator.patron_ms.conversion.dto.ConversionResponseDTO;
import com.certificator.patron_ms.conversion.dto.ConversionRequestDTO;
import com.certificator.patron_ms.conversion.dto.UncertaintyByPtnDTO;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/patrones")
public class ConversionFactorController {

    @Autowired
    private ConversionFactorService changeService;

    @PostMapping("/patrones-disponibles")
    public ResponseEntity<ApiResponse<List<Certificate>>> getPatronAvailable(@Valid @RequestBody ConversionResponseDTO change){
        List<Certificate> result = changeService.getPatronesByMeasure(change); 
        return ResponseEntity.ok(
            new ApiResponse<>(
                "success", 
                "Patrones disponibles para esa medida y magnitud:", 
                result)
        );
    }

    @PostMapping("/incertidumbre-patron")
    public ResponseEntity<ApiResponse<Double>> getUncertaintyByPtn(@Valid  @RequestBody UncertaintyByPtnDTO request){
        Double value = changeService.getUncertaintyByPtnS(request);
        return ResponseEntity.ok(
            new ApiResponse<>(
                "success", 
                "Conversión realizada correctamente", 
                value ) 
        );
    }

    @GetMapping("/unidades/{magnitud}")
    public ResponseEntity<ApiResponse<List<String>>> getUnitsByMagnitu(@PathVariable String magnitud) throws Exception{
        List<String> units = changeService.getUnitsByMagnitu(magnitud);
        return ResponseEntity.ok(
            new ApiResponse<>(
                "success", 
                "Unidades obtenidad correctamente", 
                units )
        );
    }

    @PostMapping("/cambio")
    public ResponseEntity<ApiResponse<ConversionResponseDTO>> getChangeByUnitsAndValue(@Valid @RequestBody ConversionRequestDTO request) throws Exception {
        ConversionResponseDTO response = changeService.convert(request);
        return ResponseEntity.ok(
            new ApiResponse<>(
                "success",
                "Conversión realizada correctamente",
                response
            )
        );
    }
}