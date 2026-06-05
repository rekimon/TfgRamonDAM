package com.tfg.agrogestion.domain.sensor.repository;

import com.tfg.agrogestion.domain.sensor.entity.SensorDatos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SensorDatosRepository extends JpaRepository<SensorDatos, Long> {

	@Query("""
		    SELECT s FROM SensorDatos s
		    WHERE s.parcela.id = :parcelaId
		    """)
		Page<SensorDatos> findByParcelaIdOrderByTimestampDesc(
		        @Param("parcelaId") Long parcelaId,
		        Pageable pageable);

    Optional<SensorDatos> findTopByParcelaIdOrderByTimestampDesc(
            Long parcelaId);

    @Query("""
        SELECT s FROM SensorDatos s
        WHERE s.parcela.id = :parcelaId
          AND s.timestamp >= :desde
          AND s.timestamp <= :hasta
        ORDER BY s.timestamp DESC
        """)
    Page<SensorDatos> findByParcelaAndRangoFecha(
            @Param("parcelaId") Long parcelaId,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta,
            Pageable pageable);
}