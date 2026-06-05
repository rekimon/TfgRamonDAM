package com.tfg.agrogestion.domain.user.mapper;

import com.tfg.agrogestion.domain.user.dto.response.UsuarioResponse;
import com.tfg.agrogestion.domain.user.dto.response.UsuarioResumenResponse;
import com.tfg.agrogestion.domain.user.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponse toResponse(Usuario usuario);

    @Mapping(target = "nombreCompleto",
             expression = "java(usuario.getNombreCompleto())")
    UsuarioResumenResponse toResumen(Usuario usuario);
}