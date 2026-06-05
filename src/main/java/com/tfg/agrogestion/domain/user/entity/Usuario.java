package com.tfg.agrogestion.domain.user.entity;

import com.tfg.agrogestion.common.enums.EstadoUsuario;
import com.tfg.agrogestion.common.enums.RolNombre;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(length = 20)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoUsuario estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 20)
    private RolNombre rol;

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

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

    public boolean isActivo() {
        return EstadoUsuario.ACTIVO.equals(this.estado);
    }

    public boolean isPendiente() {
        return EstadoUsuario.PENDIENTE.equals(this.estado);
    }

    public String getNombreCompleto() {
        return this.nombre + " " + this.apellidos;
    }
}