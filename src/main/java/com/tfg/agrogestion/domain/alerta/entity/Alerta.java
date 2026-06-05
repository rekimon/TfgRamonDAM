package com.tfg.agrogestion.domain.alerta.entity;

import com.tfg.agrogestion.domain.cultivo.entity.Cultivo;
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
@Table(name = "alertas")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alerta {

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
    @JoinColumn(name = "regla_manual_id")
    private ReglaAlertaManual reglaManual;

    @Column(name = "tipo_origen", nullable = false, length = 20)
    private String tipoOrigen;

    @Column(name = "tipo_alerta", nullable = false, length = 50)
    private String tipoAlerta;

    @Column(nullable = false, length = 20)
    private String severidad;

    @Column(nullable = false, columnDefinition = "NVARCHAR(500)")
    private String mensaje;

    @Column(name = "valor_detectado", precision = 10, scale = 4)
    private BigDecimal valorDetectado;

    @Column(name = "fecha_disparo", nullable = false)
    private LocalDateTime fechaDisparo;

    @Column(nullable = false, length = 20)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reconocida_por")
    private Usuario reconocidaPor;

    @Column(name = "reconocida_en")
    private LocalDateTime reconocidaEn;

    @Column(name = "resuelta_en")
    private LocalDateTime resueltaEn;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}