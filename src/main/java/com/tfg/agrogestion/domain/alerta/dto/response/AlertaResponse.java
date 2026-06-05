package com.tfg.agrogestion.domain.alerta.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaResponse {
    private Long id;
    private Long parcelaId;
    private String parcelaNombre;
    private Long cultivoId;
    private String cultivoNombre;
    private String tipoOrigen;
    private String tipoAlerta;
    private String severidad;
    private String mensaje;
    private BigDecimal valorDetectado;
    private LocalDateTime fechaDisparo;
    private String estado;
    private LocalDateTime reconocidaEn;
    private LocalDateTime resueltaEn;
    private LocalDateTime createdAt;
}