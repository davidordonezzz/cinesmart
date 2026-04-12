package com.cine.cinesmart.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
// la tabla se llamara sesiones
@Table(name = "sesiones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

// cuando pidamos por texto las sesiones esto excluira las compras del usuario
@ToString(exclude = "compras")
// dos sesiones son el mismo si tienen el mismo id en la bd
@EqualsAndHashCode(of = "id")
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pelicula_id", nullable = false)
    private Pelicula pelicula;

    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private Double precio;

    // una sesion puede tener muchas compras, porque en una misma sesion de cine a x hora puede
    // haber muchas entradas compradas y si borramos una sesion, borramos todas las entradas
    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL)
    private List<Compra> compras;
}
