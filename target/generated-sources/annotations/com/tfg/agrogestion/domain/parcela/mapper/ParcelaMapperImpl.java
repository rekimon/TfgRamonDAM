package com.tfg.agrogestion.domain.parcela.mapper;

import com.tfg.agrogestion.domain.parcela.dto.response.ParcelaResponse;
import com.tfg.agrogestion.domain.parcela.dto.response.ParcelaResumenResponse;
import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import com.tfg.agrogestion.domain.user.entity.Usuario;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-05T19:41:29+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 25.0.1 (Eclipse Adoptium)"
)
@Component
public class ParcelaMapperImpl implements ParcelaMapper {

    @Override
    public ParcelaResponse toResponse(Parcela parcela) {
        if ( parcela == null ) {
            return null;
        }

        ParcelaResponse.ParcelaResponseBuilder parcelaResponse = ParcelaResponse.builder();

        parcelaResponse.ownerId( parcelaOwnerId( parcela ) );
        parcelaResponse.activa( parcela.getActiva() );
        parcelaResponse.createdAt( parcela.getCreatedAt() );
        parcelaResponse.descripcion( parcela.getDescripcion() );
        parcelaResponse.id( parcela.getId() );
        parcelaResponse.latitud( parcela.getLatitud() );
        parcelaResponse.longitud( parcela.getLongitud() );
        parcelaResponse.municipio( parcela.getMunicipio() );
        parcelaResponse.nombre( parcela.getNombre() );
        parcelaResponse.provincia( parcela.getProvincia() );
        parcelaResponse.referenciaCatastral( parcela.getReferenciaCatastral() );
        parcelaResponse.superficieHa( parcela.getSuperficieHa() );

        parcelaResponse.ownerNombre( parcela.getOwner().getNombreCompleto() );

        return parcelaResponse.build();
    }

    @Override
    public ParcelaResumenResponse toResumen(Parcela parcela) {
        if ( parcela == null ) {
            return null;
        }

        ParcelaResumenResponse.ParcelaResumenResponseBuilder parcelaResumenResponse = ParcelaResumenResponse.builder();

        parcelaResumenResponse.activa( parcela.getActiva() );
        parcelaResumenResponse.id( parcela.getId() );
        parcelaResumenResponse.municipio( parcela.getMunicipio() );
        parcelaResumenResponse.nombre( parcela.getNombre() );
        parcelaResumenResponse.provincia( parcela.getProvincia() );
        parcelaResumenResponse.superficieHa( parcela.getSuperficieHa() );

        return parcelaResumenResponse.build();
    }

    private Long parcelaOwnerId(Parcela parcela) {
        if ( parcela == null ) {
            return null;
        }
        Usuario owner = parcela.getOwner();
        if ( owner == null ) {
            return null;
        }
        Long id = owner.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
