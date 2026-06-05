package com.tfg.agrogestion.common.service;

import com.tfg.agrogestion.common.enums.RolNombre;
import com.tfg.agrogestion.common.exception.BusinessException;
import com.tfg.agrogestion.common.exception.ResourceNotFoundException;
import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import com.tfg.agrogestion.domain.user.entity.Usuario;
import com.tfg.agrogestion.domain.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccesoService {

    private final UsuarioRepository usuarioRepository;

    public Usuario resolverUsuario(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado"));
    }

    public boolean esAdmin(String email) {
        return resolverUsuario(email).getRol() == RolNombre.ROLE_ADMIN;
    }

    public void verificarAccesoParcela(Parcela parcela, String email) {
        Usuario usuario = resolverUsuario(email);
        if (usuario.getRol() == RolNombre.ROLE_ADMIN) return;
        if (usuario.getRol() == RolNombre.ROLE_WORKER) return;
        if (!parcela.getOwner().getEmail().equals(email)) {
            throw new BusinessException(
                    "No tiene acceso a esta parcela",
                    HttpStatus.FORBIDDEN);
        }
    }

    public void verificarPropietarioParcela(Parcela parcela, String email) {
        Usuario usuario = resolverUsuario(email);
        if (usuario.getRol() == RolNombre.ROLE_ADMIN) return;
        if (!parcela.getOwner().getEmail().equals(email)) {
            throw new BusinessException(
                    "Solo el propietario puede realizar esta operacion",
                    HttpStatus.FORBIDDEN);
        }
    }
}