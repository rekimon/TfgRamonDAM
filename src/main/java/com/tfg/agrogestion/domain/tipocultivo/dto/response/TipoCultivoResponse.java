package com.tfg.agrogestion.domain.tipocultivo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoCultivoResponse {
    private Long id;
    private String nombre;
    private String nombreCientifico;
    private String descripcion;
    private String iconoUrl;
    private BigDecimal tempOptimaMin;
    private BigDecimal tempOptimaMax;
    private BigDecimal tempCriticaMin;
    private BigDecimal tempCriticaMax;
    private BigDecimal humedadSueloOptimaMin;
    private BigDecimal humedadSueloOptimaMax;
    private BigDecimal humedadSueloCriticaMin;
    private BigDecimal humedadSueloCriticaMax;
    private BigDecimal humedadAmbOptimaMin;
    private BigDecimal humedadAmbOptimaMax;
    private BigDecimal luminosidadOptimaMin;
    private BigDecimal luminosidadOptimaMax;
    private String recomendacionRiego;
    private String recomendacionHelada;
    private String recomendacionEstresHidrico;
    private String recomendacionGeneral;
}