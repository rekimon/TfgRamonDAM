package com.tfg.agrogestion.domain.user.dto.request;

import com.tfg.agrogestion.common.enums.RolNombre;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AprobarUsuarioRequest {

    @NotNull(message = "El rol es obligatorio para aprobar el usuario")
    private RolNombre rol;
}