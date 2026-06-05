package com.tfg.agrogestion.domain.parcela.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CrearParcelaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
    private String nombre;

    @Size(max = 500)
    private String descripcion;

    @NotNull(message = "La superficie es obligatoria")
    @DecimalMin(value = "0.0001", message = "La superficie debe ser mayor que 0")
    private BigDecimal superficieHa;

    @NotNull(message = "La latitud es obligatoria")
    @DecimalMin(value = "-90.0", message = "Latitud invalida")
    @DecimalMax(value = "90.0", message = "Latitud invalida")
    private BigDecimal latitud;

    @NotNull(message = "La longitud es obligatoria")
    @DecimalMin(value = "-180.0", message = "Longitud invalida")
    @DecimalMax(value = "180.0", message = "Longitud invalida")
    private BigDecimal longitud;

    @Size(max = 150)
    private String municipio;

    @Size(max = 100)
    private String provincia;

    @Size(max = 50)
    private String referenciaCatastral;
}