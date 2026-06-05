package com.tfg.agrogestion.domain.user.service;

import com.tfg.agrogestion.common.enums.EstadoUsuario;
import com.tfg.agrogestion.common.enums.RolNombre;
import com.tfg.agrogestion.common.exception.BusinessException;
import com.tfg.agrogestion.common.exception.ResourceNotFoundException;
import com.tfg.agrogestion.domain.user.dto.request.ActualizarUsuarioAdminRequest;
import com.tfg.agrogestion.domain.user.dto.request.ActualizarUsuarioRequest;
import com.tfg.agrogestion.domain.user.dto.request.AprobarUsuarioRequest;
import com.tfg.agrogestion.domain.user.dto.request.CambiarPasswordRequest;
import com.tfg.agrogestion.domain.user.dto.response.UsuarioResponse;
import com.tfg.agrogestion.domain.user.entity.Usuario;
import com.tfg.agrogestion.domain.user.mapper.UsuarioMapper;
import com.tfg.agrogestion.domain.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarUsuarios(EstadoUsuario estado,
            RolNombre rol, String busqueda, Pageable pageable) {
        return usuarioRepository
                .buscarConFiltros(estado, rol, busqueda, pageable)
                .map(usuarioMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(Long id) {
        return usuarioMapper.toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPerfil(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado"));
        return usuarioMapper.toResponse(usuario);
    }
    @Override
    @Transactional
    public UsuarioResponse actualizarAdmin(Long id,
            ActualizarUsuarioAdminRequest request) {
        Usuario usuario = findOrThrow(id);

        if (StringUtils.hasText(request.getNombre())) {
            usuario.setNombre(request.getNombre());
        }
        if (StringUtils.hasText(request.getApellidos())) {
            usuario.setApellidos(request.getApellidos());
        }
        if (StringUtils.hasText(request.getTelefono())) {
            usuario.setTelefono(request.getTelefono());
        }
        if (request.getRol() != null) {
            usuario.setRol(request.getRol());
        }
        if (request.getEstado() != null) {
            usuario.setEstado(request.getEstado());
        }

        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }
    @Override
    @Transactional
    public UsuarioResponse aprobar(Long id, AprobarUsuarioRequest request) {
        Usuario usuario = findOrThrow(id);

        if (!usuario.isPendiente()) {
            throw new BusinessException(
                    "Solo se pueden aprobar usuarios en estado PENDIENTE. "
                    + "Estado actual: " + usuario.getEstado());
        }

        if (request.getRol() == RolNombre.ROLE_ADMIN) {
            throw new BusinessException(
                    "No se puede asignar el rol ADMIN mediante aprobacion");
        }

        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setRol(request.getRol());

        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioResponse rechazar(Long id) {
        Usuario usuario = findOrThrow(id);

        if (!usuario.isPendiente()) {
            throw new BusinessException(
                    "Solo se pueden rechazar usuarios en estado PENDIENTE");
        }

        usuario.setEstado(EstadoUsuario.RECHAZADO);
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioResponse desactivar(Long id) {
        Usuario usuario = findOrThrow(id);

        if (usuario.getRol() == RolNombre.ROLE_ADMIN) {
            throw new BusinessException(
                    "No se puede desactivar una cuenta de administrador");
        }

        usuario.setEstado(EstadoUsuario.INACTIVO);
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioResponse activar(Long id) {
        Usuario usuario = findOrThrow(id);
        usuario.setEstado(EstadoUsuario.ACTIVO);
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioResponse actualizar(Long id,
            ActualizarUsuarioRequest request, String emailActual) {
        Usuario usuario = findOrThrow(id);
        verificarAccesoPropio(usuario, emailActual);

        if (StringUtils.hasText(request.getNombre())) {
            usuario.setNombre(request.getNombre());
        }
        if (StringUtils.hasText(request.getApellidos())) {
            usuario.setApellidos(request.getApellidos());
        }
        if (StringUtils.hasText(request.getTelefono())) {
            usuario.setTelefono(request.getTelefono());
        }

        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void cambiarPassword(Long id,
            CambiarPasswordRequest request, String emailActual) {
        Usuario usuario = findOrThrow(id);
        verificarAccesoPropio(usuario, emailActual);

        if (!passwordEncoder.matches(
                request.getPasswordActual(), usuario.getPasswordHash())) {
            throw new BusinessException(
                    "La contrasena actual no es correcta");
        }

        if (passwordEncoder.matches(
                request.getPasswordNueva(), usuario.getPasswordHash())) {
            throw new BusinessException(
                    "La nueva contrasena no puede ser igual a la actual");
        }

        usuario.setPasswordHash(
                passwordEncoder.encode(request.getPasswordNueva()));
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = findOrThrow(id);

        if (usuario.getRol() == RolNombre.ROLE_ADMIN) {
            throw new BusinessException(
                    "No se puede eliminar una cuenta de administrador");
        }

        usuarioRepository.delete(usuario);
    }

    private Usuario findOrThrow(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario", id));
    }

    private void verificarAccesoPropio(Usuario usuario, String emailActual) {
        if (!usuario.getEmail().equals(emailActual)) {
            throw new BusinessException(
                    "No tiene permisos para modificar este perfil",
                    HttpStatus.FORBIDDEN);
        }
    }
}
