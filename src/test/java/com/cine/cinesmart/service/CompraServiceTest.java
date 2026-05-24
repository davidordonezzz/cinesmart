package com.cine.cinesmart.service;

import com.cine.cinesmart.model.*;
import com.cine.cinesmart.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios de CompraService")
class CompraServiceTest {

    @Mock private CompraRepository compraRepository;
    @Mock private SesionRepository sesionRepository;
    @Mock private AsientoRepository asientoRepository;
    @Mock private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CompraService compraService;

    private Usuario usuario;
    private Sala sala;
    private Pelicula pelicula;
    private Asiento asiento;
    private Sesion sesion;

    @BeforeEach
    void setUp() {
        sala = Sala.builder().id(1L).nombre("Sala 1").filas(8).columnas(10).build();
        pelicula = Pelicula.builder().id(1L).titulo("Interstellar").genero("Ciencia Ficción")
                .duracion(169).sinopsis("Test").edadRecomendada("12")
                .imagenUrl("https://example.com/img.jpg").build();
        asiento = Asiento.builder().id(5L).sala(sala).fila(3).columna(4).build();
        sesion = Sesion.builder().id(10L).pelicula(pelicula).sala(sala)
                .fechaHora(LocalDateTime.now().plusDays(1)).precio(8.50).build();
        usuario = Usuario.builder().id(1L).nombre("David").email("david@correo.com")
                .password("hash").rol(Rol.USER).build();
    }

    // ── Test 1: Compra correcta ──────────────────────────────────
    @Test
    @DisplayName("realizarCompra() guarda la compra cuando la butaca está libre")
    void realizarCompra_butacaLibre_guardaCompra() {
        when(compraRepository.existsBySesionIdAndAsientoId(10L, 5L)).thenReturn(false);
        when(usuarioRepository.findByEmail("david@correo.com")).thenReturn(Optional.of(usuario));
        when(sesionRepository.findById(10L)).thenReturn(Optional.of(sesion));
        when(asientoRepository.findById(5L)).thenReturn(Optional.of(asiento));
        when(compraRepository.save(any(Compra.class))).thenAnswer(inv -> {
            Compra c = inv.getArgument(0);
            return Compra.builder().id(1L).usuario(c.getUsuario())
                    .sesion(c.getSesion()).asiento(c.getAsiento())
                    .fechaCompra(c.getFechaCompra()).precio(c.getPrecio()).build();
        });

        Compra resultado = compraService.realizarCompra("david@correo.com", 10L, 5L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getPrecio()).isEqualTo(8.50);
        assertThat(resultado.getUsuario().getEmail()).isEqualTo("david@correo.com");
        assertThat(resultado.getSesion().getId()).isEqualTo(10L);
        assertThat(resultado.getAsiento().getId()).isEqualTo(5L);
        verify(compraRepository, times(1)).save(any(Compra.class));
    }

    // ── Test 2: Doble reserva bloqueada ─────────────────────────
    @Test
    @DisplayName("realizarCompra() lanza excepción cuando la butaca ya está ocupada")
    void realizarCompra_butacaOcupada_lanzaExcepcion() {
        when(compraRepository.existsBySesionIdAndAsientoId(10L, 5L)).thenReturn(true);

        assertThatThrownBy(() -> compraService.realizarCompra("david@correo.com", 10L, 5L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ya está ocupada");

        verify(compraRepository, never()).save(any());
    }

    // ── Test 3: Lista de asientos ocupados ──────────────────────
    @Test
    @DisplayName("obtenerAsientosOcupados() devuelve los IDs correctos para una sesión")
    void obtenerAsientosOcupados_devuelveLista() {
        when(compraRepository.findAsientoIdsBySesionId(10L)).thenReturn(List.of(5L, 8L, 12L));

        List<Long> ocupados = compraService.obtenerAsientosOcupados(10L);

        assertThat(ocupados).hasSize(3).containsExactly(5L, 8L, 12L);
    }

    // ── Test 4: Sesión no encontrada ────────────────────────────
    @Test
    @DisplayName("realizarCompra() lanza excepción cuando la sesión no existe")
    void realizarCompra_sesionNoExiste_lanzaExcepcion() {
        when(compraRepository.existsBySesionIdAndAsientoId(99L, 5L)).thenReturn(false);
        when(usuarioRepository.findByEmail("david@correo.com")).thenReturn(Optional.of(usuario));
        when(sesionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> compraService.realizarCompra("david@correo.com", 99L, 5L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Sesión no encontrada");
    }

    // ── Test 5: Usuario no encontrado ───────────────────────────
    @Test
    @DisplayName("realizarCompra() lanza excepción cuando el usuario no existe")
    void realizarCompra_usuarioNoExiste_lanzaExcepcion() {
        when(compraRepository.existsBySesionIdAndAsientoId(10L, 5L)).thenReturn(false);
        when(usuarioRepository.findByEmail("fantasma@correo.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> compraService.realizarCompra("fantasma@correo.com", 10L, 5L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }
}
