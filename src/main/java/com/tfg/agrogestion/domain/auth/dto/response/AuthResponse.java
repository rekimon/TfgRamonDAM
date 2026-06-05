package com.tfg.agrogestion.domain.auth.dto.response;

import com.tfg.agrogestion.common.enums.RolNombre;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenTipo;
    private Long expiresIn;
    private Long usuarioId;
    private String nombre;
    private String email;
    private RolNombre rol;
}