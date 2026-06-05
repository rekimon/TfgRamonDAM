package com.tfg.agrogestion.domain.cosecha.mapper;

import com.tfg.agrogestion.domain.cosecha.dto.response.CosechaResponse;
import com.tfg.agrogestion.domain.cosecha.entity.Cosecha;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CosechaMapper {

    @Mapping(target = "cultivoId", source = "cultivo.id")
    @Mapping(target = "cultivoNombre",
             expression = "java(cosecha.getCultivo().getNombrePersonalizado() != null " +
                         "? cosecha.getCultivo().getNombrePersonalizado() " +
                         ": cosecha.getCultivo().getTipoCultivo().getNombre())")
    @Mapping(target = "parcelaNombre",
             source = "cultivo.parcela.nombre")
    CosechaResponse toResponse(Cosecha cosecha);
}