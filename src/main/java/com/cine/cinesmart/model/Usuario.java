package com.cine.cinesmart.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
//nombre de la table
@Table(name = "usuarios")
@Getter
@Setter
// generamos un constructor vacio
@NoArgsConstructor

// generamos un constructor en todos los atributos que tenemos
@AllArgsConstructor

// esto es para crear objetos de una manera mas clara, con su campo al lado
@Builder

// para evitar errores cuando vayamos a imprimir informacion del usuario este no imprimira
// sus compras 
@ToString(exclude = "compras")

// dos usuarios son el mismo si tienen el mismo id en la bd
@EqualsAndHashCode(of = "id")
public class Usuario {

    // el id se autoincrementa
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // el nombre no puede ser nulo
    @Column(nullable = false)
    private String nombre;

    // el correo no puede ser nulo, y tiene que ser unico
    @Column(nullable = false, unique = true)
    private String email;

    // tiene que tener contraseña
    @Column(nullable = false)
    private String password;

    // tiene que tener un rol
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    // un usuario compra muchas entradas, si el usuario se borra del sistema se borra todas sus
    // entradas 
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    // usuario compra entradas, list porque pueden ser muchas entradas
    private List<Compra> compras;
}
