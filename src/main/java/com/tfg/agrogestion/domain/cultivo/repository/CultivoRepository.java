package com.tfg.agrogestion.domain.cultivo.repository;

import com.tfg.agrogestion.domain.cultivo.entity.Cultivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CultivoRepository extends JpaRepository<Cultivo, Long> {

    Page<Cultivo> findByParcelaIdAndActivoTrue(Long parcelaId, Pageable pageable);

    Optional<Cultivo> findByIdAndActivoTrue(Long id);

    @Query("""
        SELECT c FROM Cultivo c
        WHERE c.parcela.id = :parcelaId
          AND c.activo = true
          AND c.estado = 'ACTIVO'
        """)
    List<Cultivo> findCultivosActivosByParcela(@Param("parcelaId") Long parcelaId);

    @Query("""
        SELECT c FROM Cultivo c
        WHERE c.activo = true
          AND (:parcelaId IS NULL OR c.parcela.id = :parcelaId)
          AND (:estado IS NULL OR c.estado = :estado)
          AND (:busqueda IS NULL
               OR LOWER(c.nombrePersonalizado) LIKE LOWER(CONCAT('%', :busqueda, '%')))
        """)
    Page<Cultivo> buscarConFiltros(
            @Param("parcelaId") Long parcelaId,
            @Param("estado") String estado,
            @Param("busqueda") String busqueda,
            Pageable pageable);

    boolean existsByParcelaIdAndTipoCultivoIdAndEstadoAndActivoTrue(
            Long parcelaId, Long tipoCultivoId, String estado);
}