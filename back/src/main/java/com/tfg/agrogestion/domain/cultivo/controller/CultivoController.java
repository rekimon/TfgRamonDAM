package com.tfg.agrogestion.domain.cultivo.controller;

import com.tfg.agrogestion.common.response.ApiResponse;
import com.tfg.agrogestion.domain.cultivo.dto.request.ActualizarCultivoRequest;
import com.tfg.agrogestion.domain.cultivo.dto.request.CrearCultivoRequest;
import com.tfg.agrogestion.domain.cultivo.dto.response.CultivoResponse;
import com.tfg.agrogestion.domain.cultivo.service.CultivoService;
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
@RequestMapping("/api/v1/cultivos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Cultivos", description = "Gestion de cultivos por parcela")
public class CultivoController {

    private final CultivoService cultivoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "Crear nuevo cultivo en una parcela")
    public ResponseEntity<ApiResponse<CultivoResponse>> crear(
            @Valid @RequestBody CrearCultivoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        CultivoResponse response = cultivoService.crear(
                request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Cultivo creado correctamente", response));
    }

    @GetMapping("/parcela/{parcelaId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar cultivos de una parcela")
    public ResponseEntity<ApiResponse<Page<CultivoResponse>>> listarPorParcela(
            @PathVariable Long parcelaId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String busqueda,
            @PageableDefault(size = 10, sort = "fechaSiembra",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                cultivoService.listarPorParcela(
                        parcelaId, estado, busqueda,
                        userDetails.getUsername(), pageable)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener cultivo por ID")
    public ResponseEntity<ApiResponse<CultivoResponse>> obtener(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                cultivoService.obtenerPorId(id, userDetails.getUsername())));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Actualizar cultivo")
    public ResponseEntity<ApiResponse<CultivoResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarCultivoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Cultivo actualizado correctamente",
                cultivoService.actualizar(
                        id, request, userDetails.getUsername())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "Eliminar cultivo (soft delete)")
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        cultivoService.eliminar(id, userDetails.getUsername());
        return ResponseEntity.ok(
                ApiResponse.mensaje("Cultivo eliminado correctamente"));
    }
}