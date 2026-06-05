package com.tfg.agrogestion.domain.cultivo.dto.response;

import com.tfg.agrogestion.domain.tipocultivo.dto.response.TipoCultivoResumenResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CultivoResponse {
    private Long id;
    private Long parcelaId;
    private String parcelaNombre;
    private TipoCultivoResumenResponse tipoCultivo;
    private String nombrePersonalizado;
    private LocalDate fechaSiembra;
    private LocalDate fechaCosechaEstimada;
    private String estado;
    private String notas;
    private Boolean activo;
    private LocalDateTime createdAt;
}
