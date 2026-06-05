package com.tfg.agrogestion.domain.cultivo.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ActualizarCultivoRequest {

    @Size(max = 150)
    private String nombrePersonalizado;

    private LocalDate fechaCosechaEstimada;

    @Size(max = 20)
    private String estado;

    @Size(max = 1000)
    private String notas;
}
