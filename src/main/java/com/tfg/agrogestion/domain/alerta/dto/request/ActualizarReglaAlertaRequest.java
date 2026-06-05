package com.tfg.agrogestion.domain.alerta.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ActualizarReglaAlertaRequest {

    @Size(max = 150)
    private String nombre;

    @Size(max = 500)
    private String descripcion;

    private BigDecimal valorUmbral;

    private BigDecimal valorUmbralMax;

    @Pattern(regexp = "BAJA|MEDIA|ALTA|CRITICA",
            message = "Severidad invalida")
    private String severidad;

    private Boolean activa;
}