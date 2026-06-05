package com.tfg.agrogestion.domain.parcela.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParcelaResumenResponse {
    private Long id;
    private String nombre;
    private String municipio;
    private String provincia;
    private BigDecimal superficieHa;
    private Boolean activa;
}