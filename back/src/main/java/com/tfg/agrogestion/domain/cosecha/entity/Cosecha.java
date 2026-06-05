package com.tfg.agrogestion.domain.cosecha.entity;

import com.tfg.agrogestion.domain.cultivo.entity.Cultivo;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cosechas")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cosecha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cultivo_id", nullable = false)
    private Cultivo cultivo;

    @Column(name = "fecha_cosecha", nullable = false)
    private LocalDate fechaCosecha;

    @Column(name = "kg_obtenidos", nullable = false, precision = 10, scale = 2)
    private BigDecimal kgObtenidos;

    @Column(name = "precio_por_kg", nullable = false, precision = 10, scale = 4)
    private BigDecimal precioPorKg;

    // ingreso_total es columna calculada en BD, no se mapea como insertable
    @Column(name = "ingreso_total", precision = 10, scale = 4,
            insertable = false, updatable = false)
    private BigDecimal ingresoTotal;

    @Column(length = 20)
    private String calidad;

    @Column(columnDefinition = "NVARCHAR(500)")
    private String observaciones;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", length = 100)
    private String createdBy;
}