package com.tfg.agrogestion.domain.parcela.dto.response;

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
public class ParcelaResponse {
    private Long id;
    private Long ownerId;
    private String ownerNombre;
    private String nombre;
    private String descripcion;
    private BigDecimal superficieHa;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private String municipio;
    private String provincia;
    private String referenciaCatastral;
    private Boolean activa;
    private LocalDateTime createdAt;
}