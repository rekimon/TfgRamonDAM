package com.tfg.agrogestion.domain.parcela.repository;

import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParcelaRepository extends JpaRepository<Parcela, Long> {

    Page<Parcela> findByOwnerIdAndActivaTrue(Long ownerId, Pageable pageable);

    Page<Parcela> findByActivaTrue(Pageable pageable);

    Optional<Parcela> findByIdAndActivaTrue(Long id);
    

    boolean existsByNombreAndOwnerIdAndActivaTrue(
            String nombre, Long ownerId);

    @Query("""
        SELECT p FROM Parcela p
        WHERE p.activa = true
          AND (:ownerId IS NULL OR p.owner.id = :ownerId)
          AND (:busqueda IS NULL
               OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%'))
               OR LOWER(p.municipio) LIKE LOWER(CONCAT('%', :busqueda, '%'))
               OR LOWER(p.provincia) LIKE LOWER(CONCAT('%', :busqueda, '%')))
        """)
    Page<Parcela> buscarConFiltros(
            @Param("ownerId") Long ownerId,
            @Param("busqueda") String busqueda,
            Pageable pageable);

    @Query(value = """
        SELECT p.* FROM parcelas p
        INNER JOIN parcela_usuarios pu ON pu.parcela_id = p.id
        WHERE pu.usuario_id = :usuarioId
          AND pu.activo = 1
          AND p.activa = 1
        """, nativeQuery = true)
    List<Parcela> findParcelasAsignadasAWorker(@Param("usuarioId") Long usuarioId);
    
    @Query(value = """
    	    SELECT p.* FROM parcelas p
    	    INNER JOIN parcela_usuarios pu ON pu.parcela_id = p.id
    	    WHERE pu.usuario_id = :usuarioId
    	      AND pu.activo = 1
    	      AND p.activa = 1
    	      AND (:busqueda IS NULL
    	           OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')))
    	    """, nativeQuery = true)
    	Page<Parcela> buscarParcelasWorker(
    	        @Param("usuarioId") Long usuarioId,
    	        @Param("busqueda") String busqueda,
    	        Pageable pageable);
}