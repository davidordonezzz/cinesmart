package com.cine.cinesmart.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
//la tabla se llamara
@Table(name = "peliculas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// cuando saquemos informacion de pelicula, excluiremos sesiones
@ToString(exclude = "sesiones")

// dos objetos pelicula se consideran la misma pelicula si tiene el mismo id,
// por ejemplo si cargamos dos peliculas con el mismo id
@EqualsAndHashCode(of = "id")
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String genero;

    private Integer duracion;

    // sipnosis sera tipo text
    @Column(columnDefinition = "TEXT")
    private String sinopsis;

    private String edadRecomendada;

    private String imagenUrl;

    // una pelicula puede tener varias sesiones
    // si borramos una pelicula se borra todas las sesiones
    @OneToMany(mappedBy = "pelicula", cascade = CascadeType.ALL)
    private List<Sesion> sesiones;
}
