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


@RestController
@RequestMapping("/api/patrones")
public class ChangeController {


    @Autowired
    private ChangeService changeService;

    @PostMapping("/patrones-disponibles")
    public ResponseEntity<ApiResponse<List<Certificate>>> getPatronAvailable(@RequestBody Change change){

        return ResponseEntity.ok(
            new ApiResponse<>(
                "success", 
                "Conversión realizada correctamente", 
                changeService.getPatronesByMeasure(change) )
        );
    }

    @PostMapping("/incertidumbre-patron")
    public ResponseEntity<ApiResponse<Double>> getUncertaintyByPtn(@RequestBody UncertaintyByPtnDTO request){

        return ResponseEntity.ok(
            new ApiResponse<>(
                "success", 
                "Conversión realizada correctamente", 
                changeService.getUncertaintyByPtnS(request) ) 
        );
    }
}
