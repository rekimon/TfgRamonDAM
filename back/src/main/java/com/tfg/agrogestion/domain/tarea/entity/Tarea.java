package com.tfg.agrogestion.domain.tarea.entity;

import com.tfg.agrogestion.domain.cultivo.entity.Cultivo;
import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import com.tfg.agrogestion.domain.user.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tareas")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcela_id", nullable = false)
    private Parcela parcela;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cultivo_id")
    private Cultivo cultivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignado_a")
    private Usuario asignadoA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por", nullable = false)
    private Usuario creadoPor;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "NVARCHAR(1000)")
    private String descripcion;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false, length = 20)
    private String prioridad;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(name = "fecha_prevista", nullable = false)
    private LocalDate fechaPrevista;

    @Column(name = "fecha_completada")
    private LocalDateTime fechaCompletada;

    @Column(name = "notas_completado", columnDefinition = "NVARCHAR(500)")
    private String notasCompletado;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", length = 100)
    private String updatedBy;
}