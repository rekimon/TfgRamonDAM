package com.tfg.agrogestion.domain.alerta.mapper;

import com.tfg.agrogestion.domain.alerta.dto.response.AlertaResponse;
import com.tfg.agrogestion.domain.alerta.entity.Alerta;
import com.tfg.agrogestion.domain.cultivo.entity.Cultivo;
import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-05T19:41:29+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 25.0.1 (Eclipse Adoptium)"
)
@Component
public class AlertaMapperImpl implements AlertaMapper {

    @Override
    public AlertaResponse toResponse(Alerta alerta) {
        if ( alerta == null ) {
            return null;
        }

        AlertaResponse.AlertaResponseBuilder alertaResponse = AlertaResponse.builder();

        alertaResponse.parcelaId( alertaParcelaId( alerta ) );
        alertaResponse.parcelaNombre( alertaParcelaNombre( alerta ) );
        alertaResponse.cultivoId( alertaCultivoId( alerta ) );
        alertaResponse.createdAt( alerta.getCreatedAt() );
        alertaResponse.estado( alerta.getEstado() );
        alertaResponse.fechaDisparo( alerta.getFechaDisparo() );
        alertaResponse.id( alerta.getId() );
        alertaResponse.mensaje( alerta.getMensaje() );
        alertaResponse.reconocidaEn( alerta.getReconocidaEn() );
        alertaResponse.resueltaEn( alerta.getResueltaEn() );
        alertaResponse.severidad( alerta.getSeveridad() );
        alertaResponse.tipoAlerta( alerta.getTipoAlerta() );
        alertaResponse.tipoOrigen( alerta.getTipoOrigen() );
        alertaResponse.valorDetectado( alerta.getValorDetectado() );

        alertaResponse.cultivoNombre( alerta.getCultivo() != null ? (alerta.getCultivo().getNombrePersonalizado() != null ? alerta.getCultivo().getNombrePersonalizado() : alerta.getCultivo().getTipoCultivo().getNombre()) : null );

        return alertaResponse.build();
    }

    private Long alertaParcelaId(Alerta alerta) {
        if ( alerta == null ) {
            return null;
        }
        Parcela parcela = alerta.getParcela();
        if ( parcela == null ) {
            return null;
        }
        Long id = parcela.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String alertaParcelaNombre(Alerta alerta) {
        if ( alerta == null ) {
            return null;
        }
        Parcela parcela = alerta.getParcela();
        if ( parcela == null ) {
            return null;
        }
        String nombre = parcela.getNombre();
        if ( nombre == null ) {
            return null;
        }
        return nombre;
    }

    private Long alertaCultivoId(Alerta alerta) {
        if ( alerta == null ) {
            return null;
        }
        Cultivo cultivo = alerta.getCultivo();
        if ( cultivo == null ) {
            return null;
        }
        Long id = cultivo.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
