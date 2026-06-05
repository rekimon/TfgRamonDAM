package com.tfg.agrogestion.domain.auth.controller;

import com.tfg.agrogestion.common.response.ApiResponse;
import com.tfg.agrogestion.domain.auth.dto.request.LoginRequest;
import com.tfg.agrogestion.domain.auth.dto.request.RefreshTokenRequest;
import com.tfg.agrogestion.domain.auth.dto.request.RegistroRequest;
import com.tfg.agrogestion.domain.auth.dto.response.AuthResponse;
import com.tfg.agrogestion.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticacion", description = "Registro, login y gestion de tokens")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registro")
    @Operation(summary = "Registro de nuevo usuario")
    public ResponseEntity<ApiResponse<Void>> registrar(
            @Valid @RequestBody RegistroRequest request) {
        authService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.mensaje(
                        "Registro completado. Su cuenta esta pendiente "
                        + "de aprobacion por un administrador."));
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticacion de usuario")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login exitoso", response));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar access token")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesion y revocar tokens")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(
                ApiResponse.mensaje("Sesion cerrada correctamente"));
    }
}