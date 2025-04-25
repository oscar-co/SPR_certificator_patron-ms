package com.certificator.patron_ms.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.certificator.patron_ms.DTO.ApiResponse;
import com.certificator.patron_ms.DTO.UncertaintyByPtnDTO;
import com.certificator.patron_ms.Model.Certificate;
import com.certificator.patron_ms.Model.Change;
import com.certificator.patron_ms.Service.ChangeService;

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
}
