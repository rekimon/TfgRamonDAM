package com.tfg.agrogestion.domain.tarea.mapper;

import com.tfg.agrogestion.domain.tarea.dto.response.TareaResponse;
import com.tfg.agrogestion.domain.tarea.entity.Tarea;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TareaMapper {

    @Mapping(target = "parcelaId", source = "parcela.id")
    @Mapping(target = "parcelaNombre", source = "parcela.nombre")
    @Mapping(target = "cultivoId", source = "cultivo.id")
    @Mapping(target = "cultivoNombre",
             expression = "java(tarea.getCultivo() != null ? " +
                         "(tarea.getCultivo().getNombrePersonalizado() != null ? " +
                         "tarea.getCultivo().getNombrePersonalizado() : " +
                         "tarea.getCultivo().getTipoCultivo().getNombre()) : null)")
    @Mapping(target = "asignadoAId", source = "asignadoA.id")
    @Mapping(target = "asignadoANombre",
             expression = "java(tarea.getAsignadoA() != null ? " +
                         "tarea.getAsignadoA().getNombreCompleto() : null)")
    TareaResponse toResponse(Tarea tarea);
}