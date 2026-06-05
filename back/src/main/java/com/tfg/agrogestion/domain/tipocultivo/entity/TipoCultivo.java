package com.tfg.agrogestion.domain.tipocultivo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tipo_cultivo")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoCultivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "nombre_cientifico", length = 150)
    private String nombreCientifico;

    @Column(columnDefinition = "NVARCHAR(500)")
    private String descripcion;

    @Column(name = "icono_url", length = 255)
    private String iconoUrl;

    @Column(name = "temp_optima_min", precision = 5, scale = 2)
    private BigDecimal tempOptimaMin;

    @Column(name = "temp_optima_max", precision = 5, scale = 2)
    private BigDecimal tempOptimaMax;

    @Column(name = "temp_critica_min", precision = 5, scale = 2)
    private BigDecimal tempCriticaMin;

    @Column(name = "temp_critica_max", precision = 5, scale = 2)
    private BigDecimal tempCriticaMax;

    @Column(name = "humedad_suelo_optima_min", precision = 5, scale = 2)
    private BigDecimal humedadSueloOptimaMin;

    @Column(name = "humedad_suelo_optima_max", precision = 5, scale = 2)
    private BigDecimal humedadSueloOptimaMax;

    @Column(name = "humedad_suelo_critica_min", precision = 5, scale = 2)
    private BigDecimal humedadSueloCriticaMin;

    @Column(name = "humedad_suelo_critica_max", precision = 5, scale = 2)
    private BigDecimal humedadSueloCriticaMax;

    @Column(name = "humedad_amb_optima_min", precision = 5, scale = 2)
    private BigDecimal humedadAmbOptimaMin;

    @Column(name = "humedad_amb_optima_max", precision = 5, scale = 2)
    private BigDecimal humedadAmbOptimaMax;

    @Column(name = "luminosidad_optima_min", precision = 10, scale = 2)
    private BigDecimal luminosidadOptimaMin;

    @Column(name = "luminosidad_optima_max", precision = 10, scale = 2)
    private BigDecimal luminosidadOptimaMax;

    @Column(name = "recomendacion_riego", columnDefinition = "NVARCHAR(500)")
    private String recomendacionRiego;

    @Column(name = "recomendacion_helada", columnDefinition = "NVARCHAR(500)")
    private String recomendacionHelada;

    @Column(name = "recomendacion_estres_hidrico", columnDefinition = "NVARCHAR(500)")
    private String recomendacionEstresHidrico;

    @Column(name = "recomendacion_general", columnDefinition = "NVARCHAR(1000)")
    private String recomendacionGeneral;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}