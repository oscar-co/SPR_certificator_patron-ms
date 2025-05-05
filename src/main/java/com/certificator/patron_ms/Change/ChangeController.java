package com.certificator.patron_ms.Change;

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
import com.certificator.patron_ms.DTO.ApiResponse;
import com.certificator.patron_ms.DTO.ChangeRequestDTO;
import com.certificator.patron_ms.DTO.ChangeResponseDTO;
import com.certificator.patron_ms.DTO.UncertaintyByPtnDTO;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/patrones")
public class ChangeController {

    @Autowired
    private ChangeService changeService;

    @PostMapping("/patrones-disponibles")
    public ResponseEntity<ApiResponse<List<Certificate>>> getPatronAvailable(@Valid @RequestBody Change change){

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
    public ResponseEntity<ApiResponse<ChangeResponseDTO>> getChangeByUnitsAndValue(@Valid @RequestBody ChangeRequestDTO request) throws Exception {

        ChangeResponseDTO response = changeService.convert(request);
        return ResponseEntity.ok(
            new ApiResponse<>(
                "success",
                "Conversión realizada correctamente",
                response
            )
        );
    }
}