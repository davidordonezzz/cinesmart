package com.cine.cinesmart.service;

import com.cine.cinesmart.model.Rol;
import com.cine.cinesmart.model.Usuario;
import com.cine.cinesmart.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios de UsuarioService")
class UsuarioServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    // ── Test 1: Registro correcto ────────────────────────────────
    @Test
    @DisplayName("registrar() crea el usuario con rol USER y contraseña encriptada")
    void registrar_datosValidos_creaUsuario() {
        when(usuarioRepository.existsByEmail("david@correo.com")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("$2a$hash");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            return Usuario.builder().id(1L).nombre(u.getNombre())
                    .email(u.getEmail()).password(u.getPassword()).rol(u.getRol()).build();
        });

        Usuario resultado = usuarioService.registrar("David", "david@correo.com", "pass123");

        assertThat(resultado.getRol()).isEqualTo(Rol.USER);
        assertThat(resultado.getPassword()).isEqualTo("$2a$hash");
        assertThat(resultado.getEmail()).isEqualTo("david@correo.com");
        verify(passwordEncoder).encode("pass123");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    // ── Test 2: Email duplicado ──────────────────────────────────
    @Test
    @DisplayName("registrar() lanza excepción cuando el email ya existe")
    void registrar_emailDuplicado_lanzaExcepcion() {
        when(usuarioRepository.existsByEmail("david@correo.com")).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.registrar("David", "david@correo.com", "pass123"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ya existe un usuario con ese email");

        verify(usuarioRepository, never()).save(any());
    }

    // ── Test 3: Login con email correcto ─────────────────────────
    @Test
    @DisplayName("loadUserByUsername() devuelve UserDetails cuando el email existe")
    void loadUserByUsername_emailExiste_devuelveUserDetails() {
        Usuario usuario = Usuario.builder().id(1L).nombre("David")
                .email("david@correo.com").password("$2a$hash").rol(Rol.USER).build();
        when(usuarioRepository.findByEmail("david@correo.com")).thenReturn(Optional.of(usuario));

        var userDetails = usuarioService.loadUserByUsername("david@correo.com");

        assertThat(userDetails.getUsername()).isEqualTo("david@correo.com");
        assertThat(userDetails.getAuthorities()).anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
    }

    // ── Test 4: Login con email inexistente ──────────────────────
    @Test
    @DisplayName("loadUserByUsername() lanza UsernameNotFoundException si el email no existe")
    void loadUserByUsername_emailNoExiste_lanzaExcepcion() {
        when(usuarioRepository.findByEmail("noexiste@correo.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.loadUserByUsername("noexiste@correo.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
