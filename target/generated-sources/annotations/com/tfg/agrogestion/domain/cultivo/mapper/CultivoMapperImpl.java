package com.tfg.agrogestion.domain.cultivo.mapper;

import com.tfg.agrogestion.domain.cultivo.dto.response.CultivoResponse;
import com.tfg.agrogestion.domain.cultivo.dto.response.CultivoResumenResponse;
import com.tfg.agrogestion.domain.cultivo.entity.Cultivo;
import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import com.tfg.agrogestion.domain.tipocultivo.entity.TipoCultivo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-05T19:41:29+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 25.0.1 (Eclipse Adoptium)"
)
@Component
public class CultivoMapperImpl implements CultivoMapper {

    @Override
    public CultivoResponse toResponse(Cultivo cultivo) {
        if ( cultivo == null ) {
            return null;
        }

        CultivoResponse.CultivoResponseBuilder cultivoResponse = CultivoResponse.builder();

        cultivoResponse.parcelaId( cultivoParcelaId( cultivo ) );
        cultivoResponse.parcelaNombre( cultivoParcelaNombre( cultivo ) );
        cultivoResponse.tipoCultivo( tipoCultivoToResumen( cultivo.getTipoCultivo() ) );
        cultivoResponse.activo( cultivo.getActivo() );
        cultivoResponse.createdAt( cultivo.getCreatedAt() );
        cultivoResponse.estado( cultivo.getEstado() );
        cultivoResponse.fechaCosechaEstimada( cultivo.getFechaCosechaEstimada() );
        cultivoResponse.fechaSiembra( cultivo.getFechaSiembra() );
        cultivoResponse.id( cultivo.getId() );
        cultivoResponse.nombrePersonalizado( cultivo.getNombrePersonalizado() );
        cultivoResponse.notas( cultivo.getNotas() );

        return cultivoResponse.build();
    }

    @Override
    public CultivoResumenResponse toResumen(Cultivo cultivo) {
        if ( cultivo == null ) {
            return null;
        }

        CultivoResumenResponse.CultivoResumenResponseBuilder cultivoResumenResponse = CultivoResumenResponse.builder();

        cultivoResumenResponse.tipoCultivoNombre( cultivoTipoCultivoNombre( cultivo ) );
        cultivoResumenResponse.estado( cultivo.getEstado() );
        cultivoResumenResponse.fechaSiembra( cultivo.getFechaSiembra() );
        cultivoResumenResponse.id( cultivo.getId() );
        cultivoResumenResponse.nombrePersonalizado( cultivo.getNombrePersonalizado() );

        return cultivoResumenResponse.build();
    }

    private Long cultivoParcelaId(Cultivo cultivo) {
        if ( cultivo == null ) {
            return null;
        }
        Parcela parcela = cultivo.getParcela();
        if ( parcela == null ) {
            return null;
        }
        Long id = parcela.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String cultivoParcelaNombre(Cultivo cultivo) {
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

    private String cultivoTipoCultivoNombre(Cultivo cultivo) {
        if ( cultivo == null ) {
            return null;
        }
        TipoCultivo tipoCultivo = cultivo.getTipoCultivo();
        if ( tipoCultivo == null ) {
            return null;
        }
        String nombre = tipoCultivo.getNombre();
        if ( nombre == null ) {
            return null;
        }
        return nombre;
    }
}
