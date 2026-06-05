package com.tfg.agrogestion.domain.tipocultivo.mapper;

import com.tfg.agrogestion.domain.tipocultivo.dto.response.TipoCultivoResponse;
import com.tfg.agrogestion.domain.tipocultivo.dto.response.TipoCultivoResumenResponse;
import com.tfg.agrogestion.domain.tipocultivo.entity.TipoCultivo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-05T19:41:29+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 25.0.1 (Eclipse Adoptium)"
)
@Component
public class TipoCultivoMapperImpl implements TipoCultivoMapper {

    @Override
    public TipoCultivoResponse toResponse(TipoCultivo tipoCultivo) {
        if ( tipoCultivo == null ) {
            return null;
        }

        TipoCultivoResponse.TipoCultivoResponseBuilder tipoCultivoResponse = TipoCultivoResponse.builder();

        tipoCultivoResponse.descripcion( tipoCultivo.getDescripcion() );
        tipoCultivoResponse.humedadAmbOptimaMax( tipoCultivo.getHumedadAmbOptimaMax() );
        tipoCultivoResponse.humedadAmbOptimaMin( tipoCultivo.getHumedadAmbOptimaMin() );
        tipoCultivoResponse.humedadSueloCriticaMax( tipoCultivo.getHumedadSueloCriticaMax() );
        tipoCultivoResponse.humedadSueloCriticaMin( tipoCultivo.getHumedadSueloCriticaMin() );
        tipoCultivoResponse.humedadSueloOptimaMax( tipoCultivo.getHumedadSueloOptimaMax() );
        tipoCultivoResponse.humedadSueloOptimaMin( tipoCultivo.getHumedadSueloOptimaMin() );
        tipoCultivoResponse.iconoUrl( tipoCultivo.getIconoUrl() );
        tipoCultivoResponse.id( tipoCultivo.getId() );
        tipoCultivoResponse.luminosidadOptimaMax( tipoCultivo.getLuminosidadOptimaMax() );
        tipoCultivoResponse.luminosidadOptimaMin( tipoCultivo.getLuminosidadOptimaMin() );
        tipoCultivoResponse.nombre( tipoCultivo.getNombre() );
        tipoCultivoResponse.nombreCientifico( tipoCultivo.getNombreCientifico() );
        tipoCultivoResponse.recomendacionEstresHidrico( tipoCultivo.getRecomendacionEstresHidrico() );
        tipoCultivoResponse.recomendacionGeneral( tipoCultivo.getRecomendacionGeneral() );
        tipoCultivoResponse.recomendacionHelada( tipoCultivo.getRecomendacionHelada() );
        tipoCultivoResponse.recomendacionRiego( tipoCultivo.getRecomendacionRiego() );
        tipoCultivoResponse.tempCriticaMax( tipoCultivo.getTempCriticaMax() );
        tipoCultivoResponse.tempCriticaMin( tipoCultivo.getTempCriticaMin() );
        tipoCultivoResponse.tempOptimaMax( tipoCultivo.getTempOptimaMax() );
        tipoCultivoResponse.tempOptimaMin( tipoCultivo.getTempOptimaMin() );

        return tipoCultivoResponse.build();
    }

    @Override
    public TipoCultivoResumenResponse toResumen(TipoCultivo tipoCultivo) {
        if ( tipoCultivo == null ) {
            return null;
        }

        TipoCultivoResumenResponse.TipoCultivoResumenResponseBuilder tipoCultivoResumenResponse = TipoCultivoResumenResponse.builder();

        tipoCultivoResumenResponse.descripcion( tipoCultivo.getDescripcion() );
        tipoCultivoResumenResponse.iconoUrl( tipoCultivo.getIconoUrl() );
        tipoCultivoResumenResponse.id( tipoCultivo.getId() );
        tipoCultivoResumenResponse.nombre( tipoCultivo.getNombre() );
        tipoCultivoResumenResponse.nombreCientifico( tipoCultivo.getNombreCientifico() );

        return tipoCultivoResumenResponse.build();
    }
}
