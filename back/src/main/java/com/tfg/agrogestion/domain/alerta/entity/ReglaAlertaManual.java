package com.tfg.agrogestion.domain.alerta.entity;

import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import com.tfg.agrogestion.domain.user.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reglas_alerta_manual")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReglaAlertaManual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcela_id", nullable = false)
    private Parcela parcela;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por", nullable = false)
    private Usuario creadoPor;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "NVARCHAR(500)")
    private String descripcion;

    @Column(nullable = false, length = 30)
    private String campo;

    @Column(nullable = false, length = 20)
    private String operador;

    @Column(name = "valor_umbral", nullable = false, precision = 10, scale = 4)
    private BigDecimal valorUmbral;

    @Column(name = "valor_umbral_max", precision = 10, scale = 4)
    private BigDecimal valorUmbralMax;

    @Column(nullable = false, length = 20)
    private String severidad;

    @Column(nullable = false)
    private Boolean activa;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}