package com.tfg.agrogestion.security.service;

import com.tfg.agrogestion.domain.user.entity.Usuario;
import com.tfg.agrogestion.domain.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con email: " + email));

        validarEstadoUsuario(usuario);

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPasswordHash())
                .authorities(List.of(new SimpleGrantedAuthority(
                        usuario.getRol().name())))
                .build();
    }

    private void validarEstadoUsuario(Usuario usuario) {
        switch (usuario.getEstado()) {
            case PENDIENTE -> throw new DisabledException(
                    "Su cuenta esta pendiente de aprobacion por un administrador");
            case RECHAZADO -> throw new DisabledException(
                    "Su solicitud de registro ha sido rechazada");
            case INACTIVO -> throw new LockedException(
                    "Su cuenta ha sido desactivada. Contacte con el administrador");
            case ACTIVO -> { }
        }
    }
}