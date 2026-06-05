package com.tfg.agrogestion.domain.alerta.mapper;

import com.tfg.agrogestion.domain.alerta.dto.response.AlertaResponse;
import com.tfg.agrogestion.domain.alerta.entity.Alerta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertaMapper {

    @Mapping(target = "parcelaId", source = "parcela.id")
    @Mapping(target = "parcelaNombre", source = "parcela.nombre")
    @Mapping(target = "cultivoId", source = "cultivo.id")
    @Mapping(target = "cultivoNombre",
             expression = "java(alerta.getCultivo() != null ? " +
                         "(alerta.getCultivo().getNombrePersonalizado() != null ? " +
                         "alerta.getCultivo().getNombrePersonalizado() : " +
                         "alerta.getCultivo().getTipoCultivo().getNombre()) : null)")
    AlertaResponse toResponse(Alerta alerta);
}