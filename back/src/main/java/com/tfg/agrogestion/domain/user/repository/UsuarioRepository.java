package com.tfg.agrogestion.domain.user.repository;

import com.tfg.agrogestion.common.enums.EstadoUsuario;
import com.tfg.agrogestion.common.enums.RolNombre;
import com.tfg.agrogestion.domain.user.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
        SELECT u FROM Usuario u
        WHERE (:estado IS NULL OR u.estado = :estado)
          AND (:rol IS NULL OR u.rol = :rol)
          AND (:busqueda IS NULL
               OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%'))
               OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :busqueda, '%'))
               OR LOWER(u.email) LIKE LOWER(CONCAT('%', :busqueda, '%')))
        """)
    Page<Usuario> buscarConFiltros(
            @Param("estado") EstadoUsuario estado,
            @Param("rol") RolNombre rol,
            @Param("busqueda") String busqueda,
            Pageable pageable
    );

    @Modifying
    @Query("UPDATE Usuario u SET u.ultimoAcceso = :ahora WHERE u.id = :id")
    void actualizarUltimoAcceso(@Param("id") Long id,
                                @Param("ahora") LocalDateTime ahora);
}