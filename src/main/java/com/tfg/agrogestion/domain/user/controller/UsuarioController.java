package com.tfg.agrogestion.domain.user.controller;

import com.tfg.agrogestion.common.enums.EstadoUsuario;
import com.tfg.agrogestion.common.enums.RolNombre;
import com.tfg.agrogestion.common.response.ApiResponse;
import com.tfg.agrogestion.domain.user.dto.request.ActualizarUsuarioAdminRequest;
import com.tfg.agrogestion.domain.user.dto.request.ActualizarUsuarioRequest;
import com.tfg.agrogestion.domain.user.dto.request.AprobarUsuarioRequest;
import com.tfg.agrogestion.domain.user.dto.request.CambiarPasswordRequest;
import com.tfg.agrogestion.domain.user.dto.response.UsuarioResponse;
import com.tfg.agrogestion.domain.user.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuarios", description = "Gestion de usuarios y perfiles")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar usuarios con filtros [ADMIN]")
    public ResponseEntity<ApiResponse<Page<UsuarioResponse>>> listar(
            @RequestParam(required = false) EstadoUsuario estado,
            @RequestParam(required = false) RolNombre rol,
            @RequestParam(required = false) String busqueda,
            @PageableDefault(size = 15, sort = "createdAt",
                    direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(ApiResponse.ok(
                usuarioService.listarUsuarios(estado, rol, busqueda, pageable)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener usuario por ID [ADMIN]")
    public ResponseEntity<ApiResponse<UsuarioResponse>> obtener(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(
                usuarioService.obtenerPorId(id)));
    }

    @PostMapping("/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Aprobar usuario y asignar rol [ADMIN]")
    public ResponseEntity<ApiResponse<UsuarioResponse>> aprobar(
            @PathVariable Long id,
            @Valid @RequestBody AprobarUsuarioRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Usuario aprobado correctamente",
                usuarioService.aprobar(id, request)));
    }

    @PostMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Rechazar solicitud de registro [ADMIN]")
    public ResponseEntity<ApiResponse<UsuarioResponse>> rechazar(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Solicitud rechazada",
                usuarioService.rechazar(id)));
    }

    @PostMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desactivar cuenta [ADMIN]")
    public ResponseEntity<ApiResponse<UsuarioResponse>> desactivar(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Usuario desactivado",
                usuarioService.desactivar(id)));
    }

    @PostMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activar cuenta [ADMIN]")
    public ResponseEntity<ApiResponse<UsuarioResponse>> activar(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Usuario activado",
                usuarioService.activar(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar usuario [ADMIN]")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.ok(
                ApiResponse.mensaje("Usuario eliminado correctamente"));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener perfil propio")
    public ResponseEntity<ApiResponse<UsuarioResponse>> miPerfil(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                usuarioService.obtenerPerfil(userDetails.getUsername())));
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Actualizar perfil propio")
    public ResponseEntity<ApiResponse<UsuarioResponse>> actualizar(
            @Valid @RequestBody ActualizarUsuarioRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        UsuarioResponse perfil = usuarioService
                .obtenerPerfil(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(
                "Perfil actualizado",
                usuarioService.actualizar(
                        perfil.getId(), request, userDetails.getUsername())));
    }

    @PutMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Cambiar contrasena propia")
    public ResponseEntity<ApiResponse<Void>> cambiarPassword(
            @Valid @RequestBody CambiarPasswordRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        UsuarioResponse perfil = usuarioService
                .obtenerPerfil(userDetails.getUsername());
        usuarioService.cambiarPassword(
                perfil.getId(), request, userDetails.getUsername());
        return ResponseEntity.ok(
                ApiResponse.mensaje("Contrasena actualizada correctamente"));
    }
    @GetMapping("/workers")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    @Operation(summary = "Listar workers activos")
    public ResponseEntity<ApiResponse<Page<UsuarioResponse>>> listarWorkers(
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(
                usuarioService.listarUsuarios(
                        EstadoUsuario.ACTIVO,
                        RolNombre.ROLE_WORKER,
                        null, pageable)));
    }
    @PutMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar datos completos de usuario [ADMIN]")
    public ResponseEntity<ApiResponse<UsuarioResponse>> actualizarAdmin(
            @PathVariable Long id,
            @RequestBody ActualizarUsuarioAdminRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Usuario actualizado",
                usuarioService.actualizarAdmin(id, request)));
    }
}
