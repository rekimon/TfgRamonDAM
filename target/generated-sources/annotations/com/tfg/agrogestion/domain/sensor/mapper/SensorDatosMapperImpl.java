package com.tfg.agrogestion.domain.sensor.mapper;

import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import com.tfg.agrogestion.domain.sensor.dto.response.SensorDatosResponse;
import com.tfg.agrogestion.domain.sensor.entity.SensorDatos;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-05T19:41:29+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 25.0.1 (Eclipse Adoptium)"
)
@Component
public class SensorDatosMapperImpl implements SensorDatosMapper {

    @Override
    public SensorDatosResponse toResponse(SensorDatos sensorDatos) {
        if ( sensorDatos == null ) {
            return null;
        }

        SensorDatosResponse.SensorDatosResponseBuilder sensorDatosResponse = SensorDatosResponse.builder();

        sensorDatosResponse.parcelaId( sensorDatosParcelaId( sensorDatos ) );
        sensorDatosResponse.parcelaNombre( sensorDatosParcelaNombre( sensorDatos ) );
        sensorDatosResponse.createdAt( sensorDatos.getCreatedAt() );
        sensorDatosResponse.humedadAmbiental( sensorDatos.getHumedadAmbiental() );
        sensorDatosResponse.humedadSuelo( sensorDatos.getHumedadSuelo() );
        sensorDatosResponse.id( sensorDatos.getId() );
        sensorDatosResponse.luminosidad( sensorDatos.getLuminosidad() );
        sensorDatosResponse.temperatura( sensorDatos.getTemperatura() );
        sensorDatosResponse.timestamp( sensorDatos.getTimestamp() );

        return sensorDatosResponse.build();
    }

    private Long sensorDatosParcelaId(SensorDatos sensorDatos) {
        if ( sensorDatos == null ) {
            return null;
        }
        Parcela parcela = sensorDatos.getParcela();
        if ( parcela == null ) {
            return null;
        }
        Long id = parcela.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String sensorDatosParcelaNombre(SensorDatos sensorDatos) {
        if ( sensorDatos == null ) {
            return null;
        }
        Parcela parcela = sensorDatos.getParcela();
        if ( parcela == null ) {
            return null;
        }
        String nombre = parcela.getNombre();
        if ( nombre == null ) {
            return null;
        }
        return nombre;
    }
}
