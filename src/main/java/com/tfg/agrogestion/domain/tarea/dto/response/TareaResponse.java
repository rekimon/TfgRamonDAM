package com.tfg.agrogestion.domain.tarea.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TareaResponse {
    private Long id;
    private Long parcelaId;
    private String parcelaNombre;
    private Long cultivoId;
    private String cultivoNombre;
    private Long asignadoAId;
    private String asignadoANombre;
    private String titulo;
    private String descripcion;
    private String tipo;
    private String prioridad;
    private String estado;
    private LocalDate fechaPrevista;
    private LocalDateTime fechaCompletada;
    private String notasCompletado;
    private LocalDateTime createdAt;
}