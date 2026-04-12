package com.cine.cinesmart.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// este archivop represenra una compra comprada
@Entity
@Table(name = "compras", uniqueConstraints = {

// Evita vender dos veces el mismo asiento en la misma sesion. es decir no se
// puede repetir sesion_id y asiento_id varias veces
// sesion_id asiento_id
// 1         5
// 1         5
@UniqueConstraint(columnNames = {"sesion_id", "asiento_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// dos compras son las mismas si tienen el mismo id
@EqualsAndHashCode(of = "id")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cada compra pertenece a un unico usuario.
// En la tabla compras se guarda la columna usuario_id.
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "sesion_id", nullable = false)
    private Sesion sesion;

    @ManyToOne
    @JoinColumn(name = "asiento_id", nullable = false)
    private Asiento asiento;

    @Column(nullable = false)
    private LocalDateTime fechaCompra;

    @Column(nullable = false)
    private Double precio;
}
