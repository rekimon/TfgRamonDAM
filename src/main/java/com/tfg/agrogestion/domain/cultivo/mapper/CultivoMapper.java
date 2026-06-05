package com.tfg.agrogestion.domain.cultivo.mapper;

import com.tfg.agrogestion.domain.cultivo.dto.response.CultivoResumenResponse;
import com.tfg.agrogestion.domain.cultivo.dto.response.CultivoResponse;
import com.tfg.agrogestion.domain.cultivo.entity.Cultivo;
import com.tfg.agrogestion.domain.tipocultivo.dto.response.TipoCultivoResumenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CultivoMapper {

    @Mapping(target = "parcelaId", source = "parcela.id")
    @Mapping(target = "parcelaNombre", source = "parcela.nombre")
    @Mapping(target = "tipoCultivo", source = "tipoCultivo",
             qualifiedByName = "tipoCultivoToResumen")
    CultivoResponse toResponse(Cultivo cultivo);

    @Mapping(target = "tipoCultivoNombre", source = "tipoCultivo.nombre")
    CultivoResumenResponse toResumen(Cultivo cultivo);

    @Named("tipoCultivoToResumen")
    default TipoCultivoResumenResponse tipoCultivoToResumen(
            com.tfg.agrogestion.domain.tipocultivo.entity.TipoCultivo tipoCultivo) {
        if (tipoCultivo == null) return null;
        return TipoCultivoResumenResponse.builder()
                .id(tipoCultivo.getId())
                .nombre(tipoCultivo.getNombre())
                .nombreCientifico(tipoCultivo.getNombreCientifico())
                .descripcion(tipoCultivo.getDescripcion())
                .iconoUrl(tipoCultivo.getIconoUrl())
                .build();
    }
}