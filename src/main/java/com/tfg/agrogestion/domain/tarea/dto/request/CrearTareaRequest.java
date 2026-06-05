package com.tfg.agrogestion.domain.tarea.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CrearTareaRequest {

    @NotNull(message = "La parcela es obligatoria")
    private Long parcelaId;

    private Long cultivoId;

    private Long asignadoAId;

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 200)
    private String titulo;

    @Size(max = 1000)
    private String descripcion;

    @NotBlank(message = "El tipo es obligatorio")
    @Pattern(regexp = "RIEGO|FERTILIZACION|PODA|RECOLECCION|MANTENIMIENTO|OTRO",
            message = "Tipo invalido")
    private String tipo;

    @Pattern(regexp = "BAJA|MEDIA|ALTA|URGENTE",
            message = "Prioridad invalida")
    private String prioridad;

    @NotNull(message = "La fecha prevista es obligatoria")
    private LocalDate fechaPrevista;
}