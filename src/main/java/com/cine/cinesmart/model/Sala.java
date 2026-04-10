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

@Table(name = "salas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// cuando pidamos datos de sala excluiermos asientos y sesiones
@ToString(exclude = {"asientos", "sesiones"})
@EqualsAndHashCode(of = "id")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private Integer filas;

    @Column(nullable = false)
    private Integer columnas;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL)
    private List<Asiento> asientos;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL)
    private List<Sesion> sesiones;
}
