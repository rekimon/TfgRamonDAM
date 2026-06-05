package com.tfg.agrogestion.domain.cosecha.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CosechaResponse {
    private Long id;
    private Long cultivoId;
    private String cultivoNombre;
    private String parcelaNombre;
    private LocalDate fechaCosecha;
    private BigDecimal kgObtenidos;
    private BigDecimal precioPorKg;
    private BigDecimal ingresoTotal;
    private String calidad;
    private String observaciones;
    private LocalDateTime createdAt;
}
