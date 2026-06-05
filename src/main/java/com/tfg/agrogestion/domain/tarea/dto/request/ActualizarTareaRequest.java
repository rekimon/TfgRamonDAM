package com.tfg.agrogestion.domain.tarea.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ActualizarTareaRequest {

    @Size(max = 200)
    private String titulo;

    @Size(max = 1000)
    private String descripcion;

    @Pattern(regexp = "BAJA|MEDIA|ALTA|URGENTE",
            message = "Prioridad invalida")
    private String prioridad;

    @Pattern(regexp = "PENDIENTE|EN_PROGRESO|COMPLETADA|CANCELADA",
            message = "Estado invalido")
    private String estado;

    private LocalDate fechaPrevista;

    private Long asignadoAId;

    @Size(max = 500)
    private String notasCompletado;
}