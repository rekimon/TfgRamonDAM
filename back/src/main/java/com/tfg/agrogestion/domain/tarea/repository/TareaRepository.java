package com.tfg.agrogestion.domain.tarea.repository;

import com.tfg.agrogestion.domain.tarea.entity.Tarea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    Page<Tarea> findByParcelaIdOrderByFechaPrevistaAsc(
            Long parcelaId, Pageable pageable);

    Page<Tarea> findByAsignadoAIdOrderByFechaPrevistaAsc(
            Long usuarioId, Pageable pageable);

    @Query("""
    	    SELECT t FROM Tarea t
    	    WHERE (:parcelaId IS NULL OR t.parcela.id = :parcelaId)
    	      AND (:estado IS NULL OR t.estado = :estado)
    	      AND (:prioridad IS NULL OR t.prioridad = :prioridad)
    	      AND (:asignadoAId IS NULL OR t.asignadoA.id = :asignadoAId)
    	      AND (:desde IS NULL OR t.fechaPrevista >= :desde)
    	      AND (:hasta IS NULL OR t.fechaPrevista <= :hasta)
    	    """)
    	Page<Tarea> buscarConFiltros(
    	        @Param("parcelaId") Long parcelaId,
    	        @Param("estado") String estado,
    	        @Param("prioridad") String prioridad,
    	        @Param("asignadoAId") Long asignadoAId,
    	        @Param("desde") LocalDate desde,
    	        @Param("hasta") LocalDate hasta,
    	        Pageable pageable);

    long countByParcelaIdAndEstado(Long parcelaId, String estado);
}