package com.tfg.agrogestion.domain.user.dto.response;

import com.tfg.agrogestion.common.enums.EstadoUsuario;
import com.tfg.agrogestion.common.enums.RolNombre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private EstadoUsuario estado;
    private RolNombre rol;
    private LocalDateTime ultimoAcceso;
    private LocalDateTime createdAt;
}