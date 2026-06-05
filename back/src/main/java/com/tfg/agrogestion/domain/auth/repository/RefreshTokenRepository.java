package com.tfg.agrogestion.domain.auth.repository;

import com.tfg.agrogestion.domain.auth.entity.RefreshToken;
import com.tfg.agrogestion.domain.user.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revocado = true WHERE rt.usuario = :usuario")
    void revocarTodosPorUsuario(@Param("usuario") Usuario usuario);

    @Modifying
    @Query("""
        DELETE FROM RefreshToken rt
        WHERE rt.revocado = true
           OR rt.expiracion < CURRENT_TIMESTAMP
        """)
    void eliminarExpiradosYRevocados();
}
