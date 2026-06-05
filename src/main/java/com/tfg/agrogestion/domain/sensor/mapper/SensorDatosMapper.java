package com.tfg.agrogestion.domain.sensor.mapper;

import com.tfg.agrogestion.domain.sensor.dto.response.SensorDatosResponse;
import com.tfg.agrogestion.domain.sensor.entity.SensorDatos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SensorDatosMapper {

    @Mapping(target = "parcelaId", source = "parcela.id")
    @Mapping(target = "parcelaNombre", source = "parcela.nombre")
    SensorDatosResponse toResponse(SensorDatos sensorDatos);
}