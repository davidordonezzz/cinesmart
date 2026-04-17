package com.cine.cinesmart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cine.cinesmart.model.Pelicula;
import com.cine.cinesmart.repository.PeliculaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PeliculaService {

    private final PeliculaRepository peliculaRepository;

    public List<Pelicula> listarTodas() {
        return peliculaRepository.findAll();
    }

    public List<Pelicula> filtrar(String titulo, String genero, String edad) {
        // si titulo tiene algo escrito pero esta en blanco es nulo, no se
        // envia nada a filtrar
        String t = (titulo != null && titulo.isBlank()) ? null : titulo;
        String g = (genero != null && genero.isBlank()) ? null : genero;
        String e = (edad != null && edad.isBlank()) ? null : edad;
        return peliculaRepository.filtrar(t, g, e);
    }

    public Optional<Pelicula> findById(Long id) {
        return peliculaRepository.findById(id);
    }

    public Pelicula guardar(Pelicula pelicula) {
        return peliculaRepository.save(pelicula);
    }

    public void eliminar(Long id) {
        peliculaRepository.deleteById(id);
    }

    public List<String> obtenerGeneros() {
        return peliculaRepository.findAllGeneros();
    }

    public List<String> obtenerEdades() {
        return peliculaRepository.findAllEdades();
    }
}
