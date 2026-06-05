package com.tfg.agrogestion.domain.alerta.mapper;

import com.tfg.agrogestion.domain.alerta.dto.response.ReglaAlertaResponse;
import com.tfg.agrogestion.domain.alerta.entity.ReglaAlertaManual;
import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-05T19:41:29+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 25.0.1 (Eclipse Adoptium)"
)
@Component
public class ReglaAlertaMapperImpl implements ReglaAlertaMapper {

    @Override
    public ReglaAlertaResponse toResponse(ReglaAlertaManual regla) {
        if ( regla == null ) {
            return null;
        }

        ReglaAlertaResponse.ReglaAlertaResponseBuilder reglaAlertaResponse = ReglaAlertaResponse.builder();

        reglaAlertaResponse.parcelaId( reglaParcelaId( regla ) );
        reglaAlertaResponse.parcelaNombre( reglaParcelaNombre( regla ) );
        reglaAlertaResponse.activa( regla.getActiva() );
        reglaAlertaResponse.campo( regla.getCampo() );
        reglaAlertaResponse.createdAt( regla.getCreatedAt() );
        reglaAlertaResponse.descripcion( regla.getDescripcion() );
        reglaAlertaResponse.id( regla.getId() );
        reglaAlertaResponse.nombre( regla.getNombre() );
        reglaAlertaResponse.operador( regla.getOperador() );
        reglaAlertaResponse.severidad( regla.getSeveridad() );
        reglaAlertaResponse.valorUmbral( regla.getValorUmbral() );
        reglaAlertaResponse.valorUmbralMax( regla.getValorUmbralMax() );

        return reglaAlertaResponse.build();
    }

    private Long reglaParcelaId(ReglaAlertaManual reglaAlertaManual) {
        if ( reglaAlertaManual == null ) {
            return null;
        }
        Parcela parcela = reglaAlertaManual.getParcela();
        if ( parcela == null ) {
            return null;
        }
        Long id = parcela.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String reglaParcelaNombre(ReglaAlertaManual reglaAlertaManual) {
        if ( reglaAlertaManual == null ) {
            return null;
        }
        Parcela parcela = reglaAlertaManual.getParcela();
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
