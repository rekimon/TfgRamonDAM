package com.tfg.agrogestion.domain.tarea.mapper;

import com.tfg.agrogestion.domain.cultivo.entity.Cultivo;
import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import com.tfg.agrogestion.domain.tarea.dto.response.TareaResponse;
import com.tfg.agrogestion.domain.tarea.entity.Tarea;
import com.tfg.agrogestion.domain.user.entity.Usuario;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-05T19:41:29+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 25.0.1 (Eclipse Adoptium)"
)
@Component
public class TareaMapperImpl implements TareaMapper {

    @Override
    public TareaResponse toResponse(Tarea tarea) {
        if ( tarea == null ) {
            return null;
        }

        TareaResponse.TareaResponseBuilder tareaResponse = TareaResponse.builder();

        tareaResponse.parcelaId( tareaParcelaId( tarea ) );
        tareaResponse.parcelaNombre( tareaParcelaNombre( tarea ) );
        tareaResponse.cultivoId( tareaCultivoId( tarea ) );
        tareaResponse.asignadoAId( tareaAsignadoAId( tarea ) );
        tareaResponse.createdAt( tarea.getCreatedAt() );
        tareaResponse.descripcion( tarea.getDescripcion() );
        tareaResponse.estado( tarea.getEstado() );
        tareaResponse.fechaCompletada( tarea.getFechaCompletada() );
        tareaResponse.fechaPrevista( tarea.getFechaPrevista() );
        tareaResponse.id( tarea.getId() );
        tareaResponse.notasCompletado( tarea.getNotasCompletado() );
        tareaResponse.prioridad( tarea.getPrioridad() );
        tareaResponse.tipo( tarea.getTipo() );
        tareaResponse.titulo( tarea.getTitulo() );

        tareaResponse.cultivoNombre( tarea.getCultivo() != null ? (tarea.getCultivo().getNombrePersonalizado() != null ? tarea.getCultivo().getNombrePersonalizado() : tarea.getCultivo().getTipoCultivo().getNombre()) : null );
        tareaResponse.asignadoANombre( tarea.getAsignadoA() != null ? tarea.getAsignadoA().getNombreCompleto() : null );

        return tareaResponse.build();
    }

    private Long tareaParcelaId(Tarea tarea) {
        if ( tarea == null ) {
            return null;
        }
        Parcela parcela = tarea.getParcela();
        if ( parcela == null ) {
            return null;
        }
        Long id = parcela.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String tareaParcelaNombre(Tarea tarea) {
        if ( tarea == null ) {
            return null;
        }
        Parcela parcela = tarea.getParcela();
        if ( parcela == null ) {
            return null;
        }
        String nombre = parcela.getNombre();
        if ( nombre == null ) {
            return null;
        }
        return nombre;
    }

    private Long tareaCultivoId(Tarea tarea) {
        if ( tarea == null ) {
            return null;
        }
        Cultivo cultivo = tarea.getCultivo();
        if ( cultivo == null ) {
            return null;
        }
        Long id = cultivo.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long tareaAsignadoAId(Tarea tarea) {
        if ( tarea == null ) {
            return null;
        }
        Usuario asignadoA = tarea.getAsignadoA();
        if ( asignadoA == null ) {
            return null;
        }
        Long id = asignadoA.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
