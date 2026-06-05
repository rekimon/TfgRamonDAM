package com.tfg.agrogestion.domain.parcela.controller;

import com.tfg.agrogestion.common.response.ApiResponse;
import com.tfg.agrogestion.domain.parcela.dto.request.ActualizarParcelaRequest;
import com.tfg.agrogestion.domain.parcela.dto.request.CrearParcelaRequest;
import com.tfg.agrogestion.domain.parcela.dto.response.ParcelaResponse;
import com.tfg.agrogestion.domain.parcela.service.ParcelaService;
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
@RequestMapping("/api/v1/parcelas")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Parcelas", description = "Gestion de parcelas agricolas")
public class ParcelaController {

    private final ParcelaService parcelaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "Crear nueva parcela")
    public ResponseEntity<ApiResponse<ParcelaResponse>> crear(
            @Valid @RequestBody CrearParcelaRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        ParcelaResponse response = parcelaService.crear(
                request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Parcela creada correctamente", response));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar parcelas del usuario autenticado")
    public ResponseEntity<ApiResponse<Page<ParcelaResponse>>> listar(
            @RequestParam(required = false) String busqueda,
            @PageableDefault(size = 10, sort = "createdAt",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                parcelaService.listar(
                        userDetails.getUsername(), busqueda, pageable)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener parcela por ID")
    public ResponseEntity<ApiResponse<ParcelaResponse>> obtener(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                parcelaService.obtenerPorId(id, userDetails.getUsername())));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "Actualizar parcela")
    public ResponseEntity<ApiResponse<ParcelaResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarParcelaRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Parcela actualizada correctamente",
                parcelaService.actualizar(
                        id, request, userDetails.getUsername())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "Eliminar parcela (soft delete)")
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        parcelaService.eliminar(id, userDetails.getUsername());
        return ResponseEntity.ok(
                ApiResponse.mensaje("Parcela eliminada correctamente"));
    }
}