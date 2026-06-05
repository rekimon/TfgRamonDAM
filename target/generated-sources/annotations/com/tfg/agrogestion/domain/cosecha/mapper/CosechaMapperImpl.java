package com.tfg.agrogestion.domain.cosecha.mapper;

import com.tfg.agrogestion.domain.cosecha.dto.response.CosechaResponse;
import com.tfg.agrogestion.domain.cosecha.entity.Cosecha;
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
public class CosechaMapperImpl implements CosechaMapper {

    @Override
    public CosechaResponse toResponse(Cosecha cosecha) {
        if ( cosecha == null ) {
            return null;
        }

        CosechaResponse.CosechaResponseBuilder cosechaResponse = CosechaResponse.builder();

        cosechaResponse.cultivoId( cosechaCultivoId( cosecha ) );
        cosechaResponse.parcelaNombre( cosechaCultivoParcelaNombre( cosecha ) );
        cosechaResponse.calidad( cosecha.getCalidad() );
        cosechaResponse.createdAt( cosecha.getCreatedAt() );
        cosechaResponse.fechaCosecha( cosecha.getFechaCosecha() );
        cosechaResponse.id( cosecha.getId() );
        cosechaResponse.ingresoTotal( cosecha.getIngresoTotal() );
        cosechaResponse.kgObtenidos( cosecha.getKgObtenidos() );
        cosechaResponse.observaciones( cosecha.getObservaciones() );
        cosechaResponse.precioPorKg( cosecha.getPrecioPorKg() );

        cosechaResponse.cultivoNombre( cosecha.getCultivo().getNombrePersonalizado() != null ? cosecha.getCultivo().getNombrePersonalizado() : cosecha.getCultivo().getTipoCultivo().getNombre() );

        return cosechaResponse.build();
    }

    private Long cosechaCultivoId(Cosecha cosecha) {
        if ( cosecha == null ) {
            return null;
        }
        Cultivo cultivo = cosecha.getCultivo();
        if ( cultivo == null ) {
            return null;
        }
        Long id = cultivo.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String cosechaCultivoParcelaNombre(Cosecha cosecha) {
        if ( cosecha == null ) {
            return null;
        }
        Cultivo cultivo = cosecha.getCultivo();
        if ( cultivo == null ) {
            return null;
        }
        Parcela parcela = cultivo.getParcela();
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
