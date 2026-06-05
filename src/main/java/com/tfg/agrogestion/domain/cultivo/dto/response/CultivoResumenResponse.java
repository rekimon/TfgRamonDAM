package com.tfg.agrogestion.domain.cultivo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CultivoResumenResponse {
    private Long id;
    private String nombrePersonalizado;
    private String tipoCultivoNombre;
    private LocalDate fechaSiembra;
    private String estado;
}