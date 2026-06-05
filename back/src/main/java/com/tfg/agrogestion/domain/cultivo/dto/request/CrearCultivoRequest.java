package com.tfg.agrogestion.domain.cultivo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CrearCultivoRequest {

    @NotNull(message = "La parcela es obligatoria")
    private Long parcelaId;

    @NotNull(message = "El tipo de cultivo es obligatorio")
    private Long tipoCultivoId;

    @Size(max = 150)
    private String nombrePersonalizado;

    @NotNull(message = "La fecha de siembra es obligatoria")
    private LocalDate fechaSiembra;

    private LocalDate fechaCosechaEstimada;

    @Size(max = 1000)
    private String notas;
}
