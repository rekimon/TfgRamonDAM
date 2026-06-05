package com.tfg.agrogestion.domain.alerta.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CrearReglaAlertaRequest {

    @NotNull(message = "La parcela es obligatoria")
    private Long parcelaId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150)
    private String nombre;

    @Size(max = 500)
    private String descripcion;

    @NotBlank(message = "El campo es obligatorio")
    @Pattern(regexp = "TEMPERATURA|HUMEDAD_SUELO|HUMEDAD_AMBIENTAL|LUMINOSIDAD",
            message = "Campo invalido")
    private String campo;

    @NotBlank(message = "El operador es obligatorio")
    @Pattern(regexp = "MAYOR_QUE|MENOR_QUE|ENTRE",
            message = "Operador invalido")
    private String operador;

    @NotNull(message = "El valor umbral es obligatorio")
    private BigDecimal valorUmbral;

    private BigDecimal valorUmbralMax;

    @Pattern(regexp = "BAJA|MEDIA|ALTA|CRITICA",
            message = "Severidad invalida")
    private String severidad;
}