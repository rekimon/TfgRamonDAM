package com.tfg.agrogestion.domain.parcela.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ActualizarParcelaRequest {

    @Size(max = 150)
    private String nombre;

    @Size(max = 500)
    private String descripcion;

    @DecimalMin(value = "0.0001", message = "La superficie debe ser mayor que 0")
    private BigDecimal superficieHa;

    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private BigDecimal latitud;

    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private BigDecimal longitud;

    @Size(max = 150)
    private String municipio;

    @Size(max = 100)
    private String provincia;

    @Size(max = 50)
    private String referenciaCatastral;
}