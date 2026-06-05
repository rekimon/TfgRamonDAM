package com.tfg.agrogestion.domain.tarea.controller;

import com.tfg.agrogestion.common.response.ApiResponse;
import com.tfg.agrogestion.domain.tarea.dto.request.ActualizarTareaRequest;
import com.tfg.agrogestion.domain.tarea.dto.request.CrearTareaRequest;
import com.tfg.agrogestion.domain.tarea.dto.response.TareaResponse;
import com.tfg.agrogestion.domain.tarea.service.TareaService;
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

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/tareas")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Tareas", description = "Gestion de tareas agricolas")
public class TareaController {

    private final TareaService tareaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "Crear nueva tarea")
    public ResponseEntity<ApiResponse<TareaResponse>> crear(
            @Valid @RequestBody CrearTareaRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        TareaResponse response = tareaService.crear(
                request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tarea creada correctamente", response));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar tareas con filtros")
    public ResponseEntity<ApiResponse<Page<TareaResponse>>> listar(
            @RequestParam(required = false) Long parcelaId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String prioridad,
            @RequestParam(required = false) Long asignadoAId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @PageableDefault(size = 10, sort = "fechaPrevista",
                    direction = Sort.Direction.ASC) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                tareaService.listar(parcelaId, estado, prioridad,
                        asignadoAId, desde, hasta,
                        userDetails.getUsername(), pageable)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener tarea por ID")
    public ResponseEntity<ApiResponse<TareaResponse>> obtener(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                tareaService.obtenerPorId(id, userDetails.getUsername())));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Actualizar tarea")
    public ResponseEntity<ApiResponse<TareaResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarTareaRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Tarea actualizada correctamente",
                tareaService.actualizar(
                        id, request, userDetails.getUsername())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "Eliminar tarea")
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        tareaService.eliminar(id, userDetails.getUsername());
        return ResponseEntity.ok(
                ApiResponse.mensaje("Tarea eliminada correctamente"));
    }
}