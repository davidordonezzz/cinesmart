package com.cine.cinesmart.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cine.cinesmart.model.Asiento;
import com.cine.cinesmart.model.Compra;
import com.cine.cinesmart.model.Sesion;
import com.cine.cinesmart.model.Usuario;
import com.cine.cinesmart.repository.AsientoRepository;
import com.cine.cinesmart.repository.CompraRepository;
import com.cine.cinesmart.repository.SesionRepository;
import com.cine.cinesmart.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final CompraRepository compraRepository;
    private final SesionRepository sesionRepository;
    private final AsientoRepository asientoRepository;
    private final UsuarioRepository usuarioRepository;

    
    // Devuelve los ids de los asientos ya comprados en una sesion.
// Se usa para pintar esas butacas como ocupadas en el mapa
    public List<Long> obtenerAsientosOcupados(Long sesionId) {
        return compraRepository.findAsientoIdsBySesionId(sesionId);
    }

// Comprueba si ya existe una compra para esa sesion y ese asiento
// Se usa para evitar que dos personas compren la misma butaca
    public boolean estaOcupado(Long sesionId, Long asientoId) {
        return compraRepository.existsBySesionIdAndAsientoId(sesionId, asientoId);
    }

    @Transactional // o se completa todo correctamente o se deshacee todos los cambios
    // Realiza la compra completa
    // Recibe el email del usuario logueado, la sesion elegida y el asiento seleccionado
    // La transaccion sirve para que la compra se guarde completa o no se guarde
    public Compra realizarCompra(String emailUsuario, Long sesionId, Long asientoId) {
        // Si la butaca ya esta comprada, no dejamos continuar con la compra.
        if (estaOcupado(sesionId, asientoId)) {
            throw new RuntimeException("La butaca ya está ocupada para esta sesión");
        }
        // buscamos el usuario que esta comprando a travez del email 
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // buscamos la sesion que ha elegido el usuario
        Sesion sesion = sesionRepository.findById(sesionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        // buscamos el asiento que ha elegido el usuario
        Asiento asiento = asientoRepository.findById(asientoId)
                .orElseThrow(() -> new RuntimeException("Asiento no encontrado"));

                
    // Creamos la compra con el usuario, la sesion, el asiento,
    // la fecha actual y el precio de la sesion
        Compra compra = Compra.builder()
                .usuario(usuario)
                .sesion(sesion)
                .asiento(asiento)
                .fechaCompra(LocalDateTime.now())
                .precio(sesion.getPrecio())
                .build();
        // guardamos la compra en la bd
        return compraRepository.save(compra);
    }
    // Busca una compra por su id
    // Se usa para mostrar la pantalla de confirmacion despues de comprar
    public Compra findById(Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
    }

    public List<Compra> obtenerComprasUsuario(Long usuarioId) {
        return compraRepository.findByUsuarioIdOrderByFechaCompraDesc(usuarioId);
    }
}
