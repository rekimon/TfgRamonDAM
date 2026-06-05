package com.tfg.agrogestion.domain.user.dto.response;

import com.tfg.agrogestion.common.enums.EstadoUsuario;
import com.tfg.agrogestion.common.enums.RolNombre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UsuarioResumenResponse {
    private Long id;
    private String nombreCompleto;
    private String email;
    private RolNombre rol;
    private EstadoUsuario estado;
}