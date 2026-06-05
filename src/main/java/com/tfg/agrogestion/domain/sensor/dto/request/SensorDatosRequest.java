package com.tfg.agrogestion.domain.sensor.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class SensorDatosRequest {

    @NotNull(message = "La parcela es obligatoria")
    private Long parcelaId;

    private BigDecimal temperatura;

    private BigDecimal humedadSuelo;

    private BigDecimal humedadAmbiental;

    private BigDecimal luminosidad;

    @NotNull(message = "El timestamp es obligatorio")
    private LocalDateTime timestamp;
}