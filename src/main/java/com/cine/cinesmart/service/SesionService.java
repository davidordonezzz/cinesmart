// gestiona las sesiones de cine: 
// listarlas, buscarlas, guardar, eliminar 
// y obtener las sesiones futuras de una pelicula.
package com.cine.cinesmart.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cine.cinesmart.model.Sesion;
import com.cine.cinesmart.repository.SesionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SesionService {

    private final SesionRepository sesionRepository;
    // Devuelve todas las sesiones
// Se usa en el panel admin
    public List<Sesion> listarTodas() {
        return sesionRepository.findAll();
    }
    // Busca una sesion por su id
    public Optional<Sesion> findById(Long id) {
        return sesionRepository.findById(id);
    }

    // pedimos en sesion repository un metodo y buscamos la sesion con el id de la pelicula y la fecha de inicio
    // de hoy
    public List<Sesion> findByPeliculaFuturas(Long peliculaId) {
        return sesionRepository.findByPeliculaIdAndFechaHoraAfterOrderByFechaHoraAsc(
                peliculaId, LocalDateTime.now().toLocalDate().atStartOfDay());
    }
    // Busca todas las sesiones de una pelicula concreta
    public List<Sesion> findByPelicula(Long peliculaId) {
        return sesionRepository.findByPeliculaId(peliculaId);
    }
    // Guarda una sesion nueva o actualiza una existente
    public Sesion guardar(Sesion sesion) {
        return sesionRepository.save(sesion);
    }
    
    public void eliminar(Long id) {
        sesionRepository.deleteById(id);
    }
}
