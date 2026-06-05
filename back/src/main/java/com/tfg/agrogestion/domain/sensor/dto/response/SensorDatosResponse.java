package com.tfg.agrogestion.domain.sensor.dto.response;

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
public class SensorDatosResponse {
    private Long id;
    private Long parcelaId;
    private String parcelaNombre;
    private BigDecimal temperatura;
    private BigDecimal humedadSuelo;
    private BigDecimal humedadAmbiental;
    private BigDecimal luminosidad;
    private LocalDateTime timestamp;
    private LocalDateTime createdAt;
}