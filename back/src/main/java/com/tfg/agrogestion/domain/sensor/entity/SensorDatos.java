package com.tfg.agrogestion.domain.sensor.entity;

import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_datos")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorDatos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcela_id", nullable = false)
    private Parcela parcela;

    @Column(precision = 5, scale = 2)
    private BigDecimal temperatura;

    @Column(name = "humedad_suelo", precision = 5, scale = 2)
    private BigDecimal humedadSuelo;

    @Column(name = "humedad_ambiental", precision = 5, scale = 2)
    private BigDecimal humedadAmbiental;

    @Column(precision = 10, scale = 2)
    private BigDecimal luminosidad;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}