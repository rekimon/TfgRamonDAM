package com.tfg.agrogestion.domain.user.mapper;

import com.tfg.agrogestion.domain.user.dto.response.UsuarioResponse;
import com.tfg.agrogestion.domain.user.dto.response.UsuarioResumenResponse;
import com.tfg.agrogestion.domain.user.entity.Usuario;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-05T19:41:29+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 25.0.1 (Eclipse Adoptium)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioResponse toResponse(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioResponse.UsuarioResponseBuilder usuarioResponse = UsuarioResponse.builder();

        usuarioResponse.apellidos( usuario.getApellidos() );
        usuarioResponse.createdAt( usuario.getCreatedAt() );
        usuarioResponse.email( usuario.getEmail() );
        usuarioResponse.estado( usuario.getEstado() );
        usuarioResponse.id( usuario.getId() );
        usuarioResponse.nombre( usuario.getNombre() );
        usuarioResponse.rol( usuario.getRol() );
        usuarioResponse.telefono( usuario.getTelefono() );
        usuarioResponse.ultimoAcceso( usuario.getUltimoAcceso() );

        return usuarioResponse.build();
    }

    @Override
    public UsuarioResumenResponse toResumen(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioResumenResponse.UsuarioResumenResponseBuilder usuarioResumenResponse = UsuarioResumenResponse.builder();

        usuarioResumenResponse.email( usuario.getEmail() );
        usuarioResumenResponse.estado( usuario.getEstado() );
        usuarioResumenResponse.id( usuario.getId() );
        usuarioResumenResponse.rol( usuario.getRol() );

        usuarioResumenResponse.nombreCompleto( usuario.getNombreCompleto() );

        return usuarioResumenResponse.build();
    }
}
