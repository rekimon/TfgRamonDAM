package com.tfg.agrogestion.domain.sensor.controller;

import com.tfg.agrogestion.common.response.ApiResponse;
import com.tfg.agrogestion.domain.sensor.dto.request.SensorDatosRequest;
import com.tfg.agrogestion.domain.sensor.dto.response.SensorDatosResponse;
import com.tfg.agrogestion.domain.sensor.service.SensorDatosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/sensor-datos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Sensor Datos", description = "Recepcion y consulta de datos de sensores")
public class SensorDatosController {

    private final SensorDatosService sensorDatosService;

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "Registrar payload de datos de sensores")
    public ResponseEntity<ApiResponse<SensorDatosResponse>> registrar(
            @Valid @RequestBody SensorDatosRequest request) {
        SensorDatosResponse response = sensorDatosService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Datos registrados correctamente",
                        response));
    }

    @GetMapping("/parcela/{parcelaId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar datos de sensores de una parcela")
    public ResponseEntity<ApiResponse<Page<SensorDatosResponse>>> listar(
            @PathVariable Long parcelaId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime desde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime hasta,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(
                sensorDatosService.listarPorParcela(
                        parcelaId, desde, hasta, pageable)));
    }
    @GetMapping("/parcela/{parcelaId}/ultimo")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener ultima lectura de sensores de una parcela")
    public ResponseEntity<ApiResponse<SensorDatosResponse>> ultimo(
            @PathVariable Long parcelaId) {
        return ResponseEntity.ok(ApiResponse.ok(
                sensorDatosService.obtenerUltimoPorParcela(parcelaId)));
    }
}