package com.cine.cinesmart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cine.cinesmart.model.Rol;
import com.cine.cinesmart.model.Usuario;
import com.cine.cinesmart.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    // PasswordEncoder se usa para cifrar contraseñas con bcrypt
    private final PasswordEncoder passwordEncoder;

    @Override
    // Spring Security llama a este metodo cuando 
    // un usuario intenta iniciar sesion.
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    // Buscamos en la base de datos un usuario con ese email
    // Si no existe, el login falla
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        // Devolvemos a Spring Security el email, 
        // la contraseña cifrada y el rol del usuario.
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getPassword(),
        // si en la bd el rol es admin se convierte en ROLE_ADMIN
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }
    // registramos un usuario nuevo,comprobamos que el email no existe y
    // guardamos la contraseña cifrada

    // recibmos los datos del fomulario
    public Usuario registrar(String nombre, String email, String password) {
        //comprobamos si ya existe un usuario con ese email
        if (usuarioRepository.existsByEmail(email)) {
            //si existe no dejamos registrarlo
            throw new RuntimeException("Ya existe un usuario con ese email");
        }
        // creamos el objeto usuario
        Usuario usuario = Usuario.builder()
                .nombre(nombre)
                .email(email)
                .password(passwordEncoder.encode(password))
                .rol(Rol.USER)
                // terminamos de crear el usuario
                .build();
        // guardamos el usuario en la bd
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
