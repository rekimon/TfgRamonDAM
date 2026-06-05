package com.tfg.agrogestion.domain.user.service;

import com.tfg.agrogestion.common.enums.EstadoUsuario;
import com.tfg.agrogestion.common.enums.RolNombre;
import com.tfg.agrogestion.domain.user.dto.request.ActualizarUsuarioAdminRequest;
import com.tfg.agrogestion.domain.user.dto.request.ActualizarUsuarioRequest;
import com.tfg.agrogestion.domain.user.dto.request.AprobarUsuarioRequest;
import com.tfg.agrogestion.domain.user.dto.request.CambiarPasswordRequest;
import com.tfg.agrogestion.domain.user.dto.response.UsuarioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    Page<UsuarioResponse> listarUsuarios(
            EstadoUsuario estado, RolNombre rol,
            String busqueda, Pageable pageable);

    UsuarioResponse obtenerPorId(Long id);

    UsuarioResponse obtenerPerfil(String email);

    UsuarioResponse aprobar(Long id, AprobarUsuarioRequest request);

    UsuarioResponse rechazar(Long id);

    UsuarioResponse desactivar(Long id);

    UsuarioResponse activar(Long id);
    
    UsuarioResponse actualizarAdmin(Long id, ActualizarUsuarioAdminRequest request);
    UsuarioResponse actualizar(Long id, ActualizarUsuarioRequest request,
            String emailActual);

    void cambiarPassword(Long id, CambiarPasswordRequest request,
            String emailActual);

    void eliminar(Long id);
    
    
    
}