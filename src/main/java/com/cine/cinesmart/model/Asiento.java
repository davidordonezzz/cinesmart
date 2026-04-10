package com.cine.cinesmart.model;

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

@Entity
@Table(name = "asientos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"sala_id", "fila", "columna"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// dos asiento son el mismo si tienen el mismo id en la bd
@EqualsAndHashCode(of = "id")
public class Asiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    @Column(nullable = false)
    private Integer fila;

    @Column(nullable = false)
    private Integer columna;
}
