package com.tfg.agrogestion.domain.cosecha.controller;

import com.tfg.agrogestion.common.response.ApiResponse;
import com.tfg.agrogestion.domain.cosecha.dto.request.ActualizarCosechaRequest;
import com.tfg.agrogestion.domain.cosecha.dto.request.CrearCosechaRequest;
import com.tfg.agrogestion.domain.cosecha.dto.response.CosechaResponse;
import com.tfg.agrogestion.domain.cosecha.service.CosechaService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.tfg.agrogestion.domain.cosecha.service.PdfService;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/cosechas")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Cosechas", description = "Registro economico de cosechas")
public class CosechaController {

    private final CosechaService cosechaService;
    private final PdfService pdfService;

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "Registrar nueva cosecha")
    public ResponseEntity<ApiResponse<CosechaResponse>> crear(
            @Valid @RequestBody CrearCosechaRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        CosechaResponse response = cosechaService.crear(
                request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Cosecha registrada correctamente",
                        response));
    }

    @GetMapping("/parcela/{parcelaId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Page<CosechaResponse>>> listarPorParcela(
            @PathVariable Long parcelaId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                cosechaService.listarPorParcela(
                        parcelaId, desde, hasta,
                        userDetails.getUsername(), pageable)));
    }

    @GetMapping("/cultivo/{cultivoId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Page<CosechaResponse>>> listarPorCultivo(
            @PathVariable Long cultivoId,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                cosechaService.listarPorCultivo(
                        cultivoId, userDetails.getUsername(), pageable)));
    }
    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "Exportar cosecha a PDF")
    public ResponseEntity<byte[]> exportarPdf(@PathVariable Long id) {
        byte[] pdf = pdfService.generarPdfCosecha(id);
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition",
                        "attachment; filename=cosecha-" + id + ".pdf")
                .body(pdf);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener cosecha por ID")
    public ResponseEntity<ApiResponse<CosechaResponse>> obtener(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                cosechaService.obtenerPorId(id, userDetails.getUsername())));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "Actualizar cosecha")
    public ResponseEntity<ApiResponse<CosechaResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarCosechaRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Cosecha actualizada correctamente",
                cosechaService.actualizar(
                        id, request, userDetails.getUsername())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "Eliminar cosecha")
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        cosechaService.eliminar(id, userDetails.getUsername());
        return ResponseEntity.ok(
                ApiResponse.mensaje("Cosecha eliminada correctamente"));
    }
}