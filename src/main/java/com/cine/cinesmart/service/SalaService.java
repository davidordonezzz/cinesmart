package com.cine.cinesmart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cine.cinesmart.model.Asiento;
import com.cine.cinesmart.model.Sala;
import com.cine.cinesmart.repository.AsientoRepository;
import com.cine.cinesmart.repository.SalaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalaService {

    private final SalaRepository salaRepository;
    private final AsientoRepository asientoRepository;

    // Devuelve todas las salas para mostrarlas en el admin
    public List<Sala> listarTodas() {
        return salaRepository.findAll();
    }

    // Busca una sala por su id.
    public Optional<Sala> findById(Long id) {
        return salaRepository.findById(id);
    }

    @Transactional
    public Sala guardar(Sala sala) {
        Sala saved = salaRepository.save(sala);
        // Generar asientos si no existen
        List<Asiento> existentes = asientoRepository.findBySalaIdOrderByFilaAscColumnaAsc(saved.getId());
        if (existentes.isEmpty()) {
            generarAsientos(saved);
        }
        return saved;
    }
    // Elimina una sala por su id
    @Transactional
    public void eliminar(Long id) {
        salaRepository.deleteById(id);
    }
    // Genera los asientos de la sala
// Recorremos filas y columnas, crea un asiento por cada posicion
// y lo guardamos en la base de datos.
    private void generarAsientos(Sala sala) {
        for (int f = 1; f <= sala.getFilas(); f++) {
            for (int c = 1; c <= sala.getColumnas(); c++) {
                Asiento asiento = Asiento.builder()
                        .sala(sala)
                        .fila(f)
                        .columna(c)
                        .build();
                asientoRepository.save(asiento);
            }
        }
    }

    public List<Asiento> obtenerAsientos(Long salaId) {
        return asientoRepository.findBySalaIdOrderByFilaAscColumnaAsc(salaId);
    }
}
