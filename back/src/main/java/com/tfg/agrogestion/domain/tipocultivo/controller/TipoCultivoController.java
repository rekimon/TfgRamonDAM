package com.tfg.agrogestion.domain.tipocultivo.controller;

import com.tfg.agrogestion.common.response.ApiResponse;
import com.tfg.agrogestion.domain.tipocultivo.dto.response.TipoCultivoResumenResponse;
import com.tfg.agrogestion.domain.tipocultivo.dto.response.TipoCultivoResponse;
import com.tfg.agrogestion.domain.tipocultivo.service.TipoCultivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tipos-cultivo")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Tipos de Cultivo",
     description = "Catalogo agronomico de tipos de cultivo")
public class TipoCultivoController {

    private final TipoCultivoService tipoCultivoService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar tipos de cultivo")
    public ResponseEntity<ApiResponse<Page<TipoCultivoResumenResponse>>> listar(
            @RequestParam(required = false) String busqueda,
            @PageableDefault(size = 20, sort = "nombre",
                    direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(
                tipoCultivoService.listar(busqueda, pageable)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener tipo de cultivo por ID con parametros agronomicos")
    public ResponseEntity<ApiResponse<TipoCultivoResponse>> obtener(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(
                tipoCultivoService.obtenerPorId(id)));
    }
}