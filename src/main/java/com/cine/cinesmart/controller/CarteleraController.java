package com.cine.cinesmart.controller;

import com.cine.cinesmart.model.Pelicula;
import com.cine.cinesmart.model.Sesion;
import com.cine.cinesmart.service.PeliculaService;
import com.cine.cinesmart.service.SesionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CarteleraController {

    private final PeliculaService peliculaService;
    private final SesionService sesionService;

    @GetMapping({"/", "/cartelera"})
    public String cartelera(@RequestParam(required = false) String titulo,
                            @RequestParam(required = false) String genero,
                            @RequestParam(required = false) String edad,
                            Model model) {
        List<Pelicula> peliculas = peliculaService.filtrar(titulo, genero, edad);
        model.addAttribute("peliculas", peliculas);
        model.addAttribute("generos", peliculaService.obtenerGeneros());
        model.addAttribute("edades", peliculaService.obtenerEdades());
        model.addAttribute("tituloFiltro", titulo);
        model.addAttribute("generoFiltro", genero);
        model.addAttribute("edadFiltro", edad);
        return "cartelera";
    }

    @GetMapping("/pelicula/{id}")
    public String detallePelicula(@PathVariable Long id, Model model) {
        Pelicula pelicula = peliculaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));
        List<Sesion> sesiones = sesionService.findByPeliculaFuturas(id);
        model.addAttribute("pelicula", pelicula);
        model.addAttribute("sesiones", sesiones);
        return "pelicula-detalle";
    }
}
