package com.tfg.agrogestion.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarUsuarioRequest {

    @Size(max = 100)
    private String nombre;

    @Size(max = 150)
    private String apellidos;

    @Pattern(regexp = "^[+]?[0-9]{9,15}$",
            message = "El telefono no tiene un formato valido")
    private String telefono;
}