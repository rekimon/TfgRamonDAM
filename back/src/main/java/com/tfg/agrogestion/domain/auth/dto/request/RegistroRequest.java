package com.tfg.agrogestion.domain.auth.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 150, message = "Los apellidos no pueden superar 150 caracteres")
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato valido")
    @Size(max = 255)
    private String email;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 8, max = 100, message = "La contrasena debe tener entre 8 y 100 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
        message = "La contrasena debe contener mayusculas, minusculas y numeros"
    )
    private String password;

    @Pattern(regexp = "^[+]?[0-9]{9,15}$",
            message = "El telefono no tiene un formato valido")
    private String telefono;
}