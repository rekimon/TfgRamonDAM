package com.tfg.agrogestion.domain.alerta.repository;

import com.tfg.agrogestion.domain.alerta.entity.Alerta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    Page<Alerta> findByParcelaIdOrderByFechaDisparoDesc(
            Long parcelaId, Pageable pageable);
    
    @Query("""
    	    SELECT a FROM Alerta a
    	    WHERE (:estado IS NULL OR a.estado = :estado)
    	      AND (:severidad IS NULL OR a.severidad = :severidad)
    	    """)
    	Page<Alerta> buscarTodas(
    	        @Param("estado") String estado,
    	        @Param("severidad") String severidad,
    	        Pageable pageable);

    @Query("""
        SELECT a FROM Alerta a
        WHERE a.parcela.id = :parcelaId
          AND (:estado IS NULL OR a.estado = :estado)
          AND (:severidad IS NULL OR a.severidad = :severidad)
        """)
    Page<Alerta> buscarConFiltros(
            @Param("parcelaId") Long parcelaId,
            @Param("estado") String estado,
            @Param("severidad") String severidad,
            Pageable pageable);

    @Query("""
        SELECT a FROM Alerta a
        WHERE a.parcela.id = :parcelaId
          AND a.estado = 'ACTIVA'
          AND a.tipoAlerta = :tipoAlerta
        """)
    List<Alerta> findAlertasActivasByTipo(
            @Param("parcelaId") Long parcelaId,
            @Param("tipoAlerta") String tipoAlerta);
    
    

    long countByParcelaIdAndEstado(Long parcelaId, String estado);
}