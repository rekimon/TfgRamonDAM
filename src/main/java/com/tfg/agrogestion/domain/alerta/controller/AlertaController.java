package com.tfg.agrogestion.domain.alerta.controller;

import com.tfg.agrogestion.common.response.ApiResponse;
import com.tfg.agrogestion.domain.alerta.dto.request.ActualizarReglaAlertaRequest;
import com.tfg.agrogestion.domain.alerta.dto.request.CrearReglaAlertaRequest;
import com.tfg.agrogestion.domain.alerta.dto.response.AlertaResponse;
import com.tfg.agrogestion.domain.alerta.dto.response.ReglaAlertaResponse;
import com.tfg.agrogestion.domain.alerta.service.AlertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/alertas")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Alertas", description = "Gestion de alertas manuales y automaticas")
public class AlertaController {

    private final AlertaService alertaService;


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todas las alertas [ADMIN]")
    public ResponseEntity<ApiResponse<Page<AlertaResponse>>> listarTodas(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String severidad,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                alertaService.listarTodas(estado, severidad, pageable)));
    }

    @PostMapping("/reglas")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "Crear regla de alerta manual")
    public ResponseEntity<ApiResponse<ReglaAlertaResponse>> crearRegla(
            @Valid @RequestBody CrearReglaAlertaRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.ok("Regla creada correctamente",
                        alertaService.crearRegla(
                                request, userDetails.getUsername())));
    }

    @GetMapping("/reglas/parcela/{parcelaId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar reglas de alerta de una parcela")
    public ResponseEntity<ApiResponse<Page<ReglaAlertaResponse>>> listarReglas(
            @PathVariable Long parcelaId,
            @PageableDefault(size = 10, sort = "createdAt",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                alertaService.listarReglasPorParcela(
                        parcelaId, userDetails.getUsername(), pageable)));
    }

    @PutMapping("/reglas/{id}")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "Actualizar regla de alerta")
    public ResponseEntity<ApiResponse<ReglaAlertaResponse>> actualizarRegla(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarReglaAlertaRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Regla actualizada correctamente",
                alertaService.actualizarRegla(
                        id, request, userDetails.getUsername())));
    }

    @DeleteMapping("/reglas/{id}")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "Eliminar regla de alerta")
    public ResponseEntity<ApiResponse<Void>> eliminarRegla(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        alertaService.eliminarRegla(id, userDetails.getUsername());
        return ResponseEntity.ok(
                ApiResponse.mensaje("Regla eliminada correctamente"));
    }
    @GetMapping("/parcela/{parcelaId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar alertas de una parcela con filtros")
    public ResponseEntity<ApiResponse<Page<AlertaResponse>>> listarAlertas(
            @PathVariable Long parcelaId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String severidad,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                alertaService.listarAlertasPorParcela(
                        parcelaId, estado, severidad,
                        userDetails.getUsername(), pageable)));
    }
    
    @PostMapping("/{id}/reconocer")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Reconocer alerta activa")
    public ResponseEntity<ApiResponse<AlertaResponse>> reconocer(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Alerta reconocida",
                alertaService.reconocerAlerta(
                        id, userDetails.getUsername())));
    }

    @PostMapping("/{id}/resolver")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Resolver alerta")
    public ResponseEntity<ApiResponse<AlertaResponse>> resolver(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Alerta resuelta",
                alertaService.resolverAlerta(
                        id, userDetails.getUsername())));
    }
}