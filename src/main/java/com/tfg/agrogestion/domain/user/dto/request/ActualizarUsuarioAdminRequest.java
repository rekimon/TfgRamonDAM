package com.tfg.agrogestion.domain.user.dto.request;

import com.tfg.agrogestion.common.enums.EstadoUsuario;
import com.tfg.agrogestion.common.enums.RolNombre;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarUsuarioAdminRequest {
    private String nombre;
    private String apellidos;
    private String telefono;
    private RolNombre rol;
    private EstadoUsuario estado;
}