package com.tfg.agrogestion.domain.tipocultivo.repository;

import com.tfg.agrogestion.domain.tipocultivo.entity.TipoCultivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TipoCultivoRepository extends JpaRepository<TipoCultivo, Long> {

    Optional<TipoCultivo> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    @Query("""
        SELECT t FROM TipoCultivo t
        WHERE (:busqueda IS NULL
               OR LOWER(t.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%'))
               OR LOWER(t.nombreCientifico) LIKE LOWER(CONCAT('%', :busqueda, '%')))
        """)
    Page<TipoCultivo> buscarConFiltros(
            @Param("busqueda") String busqueda,
            Pageable pageable);
}