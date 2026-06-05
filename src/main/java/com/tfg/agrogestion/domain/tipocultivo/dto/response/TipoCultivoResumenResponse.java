package com.tfg.agrogestion.domain.tipocultivo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoCultivoResumenResponse {
    private Long id;
    private String nombre;
    private String nombreCientifico;
    private String descripcion;
    private String iconoUrl;
}