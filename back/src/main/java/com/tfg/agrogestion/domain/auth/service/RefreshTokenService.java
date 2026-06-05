package com.tfg.agrogestion.domain.auth.service;

import com.tfg.agrogestion.common.exception.AuthException;
import com.tfg.agrogestion.domain.auth.entity.RefreshToken;
import com.tfg.agrogestion.domain.auth.repository.RefreshTokenRepository;
import com.tfg.agrogestion.domain.user.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwt.refresh-token-expiration-days}")
    private long refreshTokenExpirationDays;

    @Transactional
    public RefreshToken crearRefreshToken(Usuario usuario) {
        refreshTokenRepository.revocarTodosPorUsuario(usuario);

        RefreshToken refreshToken = RefreshToken.builder()
                .usuario(usuario)
                .token(UUID.randomUUID().toString())
                .expiracion(LocalDateTime.now()
                        .plusDays(refreshTokenExpirationDays))
                .revocado(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)
    public RefreshToken validarRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthException(
                        "Refresh token no valido o inexistente"));

        if (!refreshToken.isValido()) {
            throw new AuthException(refreshToken.isRevocado()
                    ? "El refresh token ha sido revocado"
                    : "El refresh token ha expirado. Inicie sesion de nuevo");
        }

        return refreshToken;
    }

    @Transactional
    public void revocarPorUsuario(Usuario usuario) {
        refreshTokenRepository.revocarTodosPorUsuario(usuario);
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void limpiarTokensExpirados() {
        refreshTokenRepository.eliminarExpiradosYRevocados();
    }
}