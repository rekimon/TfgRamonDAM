package com.tfg.agrogestion.domain.tipocultivo.mapper;

import com.tfg.agrogestion.domain.tipocultivo.dto.response.TipoCultivoResumenResponse;
import com.tfg.agrogestion.domain.tipocultivo.dto.response.TipoCultivoResponse;
import com.tfg.agrogestion.domain.tipocultivo.entity.TipoCultivo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoCultivoMapper {

    TipoCultivoResponse toResponse(TipoCultivo tipoCultivo);

    TipoCultivoResumenResponse toResumen(TipoCultivo tipoCultivo);
}