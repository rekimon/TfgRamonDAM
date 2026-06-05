package com.tfg.agrogestion.domain.cosecha.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CrearCosechaRequest {

    @NotNull(message = "El cultivo es obligatorio")
    private Long cultivoId;

    @NotNull(message = "La fecha de cosecha es obligatoria")
    private LocalDate fechaCosecha;

    @NotNull(message = "Los kg obtenidos son obligatorios")
    @DecimalMin(value = "0.01", message = "Los kg deben ser mayores que 0")
    private BigDecimal kgObtenidos;

    @NotNull(message = "El precio por kg es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    private BigDecimal precioPorKg;

    @Pattern(regexp = "BAJA|ESTANDAR|PREMIUM",
            message = "La calidad debe ser BAJA, ESTANDAR o PREMIUM")
    private String calidad;

    @Size(max = 500)
    private String observaciones;
}