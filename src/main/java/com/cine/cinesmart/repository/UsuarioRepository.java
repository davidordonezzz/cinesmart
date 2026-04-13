package com.cine.cinesmart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cine.cinesmart.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // buscamos en usuario a travez del email que le demos
    // Lo usamos para el login, compra y mis Entradas para identificar al usuario.
    Optional<Usuario> findByEmail(String email);

    // comprueba si ese email existe o no, para al registrar un nuevo usuario ,evitar que haya dos usuarios con
    // el mismo correo
    boolean existsByEmail(String email);
}
