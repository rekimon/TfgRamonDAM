package com.tfg.agrogestion.domain.parcela.mapper;

import com.tfg.agrogestion.domain.parcela.dto.response.ParcelaResumenResponse;
import com.tfg.agrogestion.domain.parcela.dto.response.ParcelaResponse;
import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParcelaMapper {

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "ownerNombre", expression = "java(parcela.getOwner().getNombreCompleto())")
    ParcelaResponse toResponse(Parcela parcela);

    ParcelaResumenResponse toResumen(Parcela parcela);
}