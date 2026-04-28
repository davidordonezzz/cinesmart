package com.cine.cinesmart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cine.cinesmart.model.Pelicula;
import com.cine.cinesmart.service.PeliculaService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/peliculas")
@RequiredArgsConstructor
public class AdminPeliculaController {

    private final PeliculaService peliculaService;
    
    @GetMapping
    public String lista(Model model) {
        model.addAttribute("peliculas", peliculaService.listarTodas());
        return "admin/peliculas/lista";
    }

    @GetMapping("/nueva")
    public String nuevaForm(Model model) {
        model.addAttribute("pelicula", new Pelicula());
        return "admin/peliculas/nueva";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Pelicula pelicula, RedirectAttributes ra) {
        peliculaService.guardar(pelicula);
        ra.addFlashAttribute("exito", "Película guardada correctamente");
        return "redirect:/admin/peliculas";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Pelicula pelicula = peliculaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));
        model.addAttribute("pelicula", pelicula);
        return "admin/peliculas/editar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        peliculaService.eliminar(id);
        ra.addFlashAttribute("exito", "Película eliminada");
        return "redirect:/admin/peliculas";
    }
}
