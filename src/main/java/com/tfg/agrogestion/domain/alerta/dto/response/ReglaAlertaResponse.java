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
public class ReglaAlertaResponse {
    private Long id;
    private Long parcelaId;
    private String parcelaNombre;
    private String nombre;
    private String descripcion;
    private String campo;
    private String operador;
    private BigDecimal valorUmbral;
    private BigDecimal valorUmbralMax;
    private String severidad;
    private Boolean activa;
    private LocalDateTime createdAt;
}