package com.tfg.agrogestion.domain.alerta.mapper;

import com.tfg.agrogestion.domain.alerta.dto.response.ReglaAlertaResponse;
import com.tfg.agrogestion.domain.alerta.entity.ReglaAlertaManual;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReglaAlertaMapper {

    @Mapping(target = "parcelaId", source = "parcela.id")
    @Mapping(target = "parcelaNombre", source = "parcela.nombre")
    ReglaAlertaResponse toResponse(ReglaAlertaManual regla);
}