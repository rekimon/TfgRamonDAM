package com.tfg.agrogestion.domain.auth.service;

import com.tfg.agrogestion.common.enums.EstadoUsuario;
import com.tfg.agrogestion.common.exception.ConflictException;
import com.tfg.agrogestion.domain.auth.dto.request.LoginRequest;
import com.tfg.agrogestion.domain.auth.dto.request.RefreshTokenRequest;
import com.tfg.agrogestion.domain.auth.dto.request.RegistroRequest;
import com.tfg.agrogestion.domain.auth.dto.response.AuthResponse;
import com.tfg.agrogestion.domain.auth.entity.RefreshToken;
import com.tfg.agrogestion.domain.user.entity.Usuario;
import com.tfg.agrogestion.domain.user.repository.UsuarioRepository;
import com.tfg.agrogestion.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Value("${app.jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;

    @Override
    @Transactional
    public void registrar(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(
                    "Ya existe una cuenta registrada con el email: "
                    + request.getEmail());
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .email(request.getEmail().toLowerCase().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .telefono(request.getTelefono())
                .estado(EstadoUsuario.PENDIENTE)
                .rol(null)
                .build();

        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase().trim(),
                        request.getPassword()));

        Usuario usuario = usuarioRepository
                .findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow();

        usuarioRepository.actualizarUltimoAcceso(
                usuario.getId(), LocalDateTime.now());

        String accessToken = jwtTokenProvider.generarAccessToken(authentication);
        RefreshToken refreshToken = refreshTokenService.crearRefreshToken(usuario);

        return buildAuthResponse(accessToken, refreshToken.getToken(), usuario);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService
                .validarRefreshToken(request.getRefreshToken());

        Usuario usuario = refreshToken.getUsuario();
        String nuevoAccessToken = jwtTokenProvider
                .generarTokenDesdeEmail(usuario.getEmail());
        RefreshToken nuevoRefresh = refreshTokenService.crearRefreshToken(usuario);

        return buildAuthResponse(
                nuevoAccessToken, nuevoRefresh.getToken(), usuario);
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        RefreshToken token = refreshTokenService.validarRefreshToken(refreshToken);
        refreshTokenService.revocarPorUsuario(token.getUsuario());
    }

    private AuthResponse buildAuthResponse(String accessToken,
            String refreshToken, Usuario usuario) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenTipo("Bearer")
                .expiresIn(accessTokenExpirationMs)
                .usuarioId(usuario.getId())
                .nombre(usuario.getNombreCompleto())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .build();
    }
}