package com.tfg.agrogestion.domain.cosecha.repository;

import com.tfg.agrogestion.domain.cosecha.entity.Cosecha;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface CosechaRepository extends JpaRepository<Cosecha, Long> {

    Page<Cosecha> findByCultivoId(Long cultivoId, Pageable pageable);

    @Query("""
        SELECT c FROM Cosecha c
        WHERE c.cultivo.parcela.id = :parcelaId
          AND (:desde IS NULL OR c.fechaCosecha >= :desde)
          AND (:hasta IS NULL OR c.fechaCosecha <= :hasta)
        """)
    Page<Cosecha> findByParcelaConFiltros(
            @Param("parcelaId") Long parcelaId,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta,
            Pageable pageable);

    @Query("""
        SELECT COALESCE(SUM(c.kgObtenidos), 0)
        FROM Cosecha c
        WHERE c.cultivo.id = :cultivoId
        """)
    BigDecimal sumKgByCultivo(@Param("cultivoId") Long cultivoId);

    @Query("""
        SELECT COALESCE(SUM(c.ingresoTotal), 0)
        FROM Cosecha c
        WHERE c.cultivo.parcela.id = :parcelaId
          AND (:anio IS NULL OR YEAR(c.fechaCosecha) = :anio)
        """)
    BigDecimal sumIngresoByParcela(
            @Param("parcelaId") Long parcelaId,
            @Param("anio") Integer anio);
    @Query("""
    	    SELECT c FROM Cosecha c
    	    JOIN FETCH c.cultivo cu
    	    JOIN FETCH cu.parcela p
    	    JOIN FETCH cu.tipoCultivo tc
    	    WHERE c.id = :id
    	    """)
    	Optional<Cosecha> findByIdWithDetails(@Param("id") Long id);
}