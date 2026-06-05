package com.tfg.agrogestion.domain.cosecha.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ActualizarCosechaRequest {

    private LocalDate fechaCosecha;

    @DecimalMin(value = "0.01", message = "Los kg deben ser mayores que 0")
    private BigDecimal kgObtenidos;

    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    private BigDecimal precioPorKg;

    @Pattern(regexp = "BAJA|ESTANDAR|PREMIUM",
            message = "La calidad debe ser BAJA, ESTANDAR o PREMIUM")
    private String calidad;

    @Size(max = 500)
    private String observaciones;
}