package com.cine.cinesmart.controller;

import com.cine.cinesmart.model.Sesion;
import com.cine.cinesmart.service.PeliculaService;
import com.cine.cinesmart.service.SalaService;
import com.cine.cinesmart.service.SesionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/sesiones")
@RequiredArgsConstructor
public class AdminSesionController {

    private final SesionService sesionService;
    private final PeliculaService peliculaService;
    private final SalaService salaService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("sesiones", sesionService.listarTodas());
        return "admin/sesiones/lista";
    }

    @GetMapping("/nueva")
    public String nuevaForm(Model model) {
        model.addAttribute("sesion", new Sesion());
        model.addAttribute("peliculas", peliculaService.listarTodas());
        model.addAttribute("salas", salaService.listarTodas());
        return "admin/sesiones/nueva";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Sesion sesion,
                          @RequestParam Long peliculaId,
                          @RequestParam Long salaId,
                          RedirectAttributes ra) {
        sesion.setPelicula(peliculaService.findById(peliculaId)
                .orElseThrow(() -> new RuntimeException("Película no encontrada")));
        sesion.setSala(salaService.findById(salaId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada")));
        sesionService.guardar(sesion);
        ra.addFlashAttribute("exito", "Sesión guardada correctamente");
        return "redirect:/admin/sesiones";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Sesion sesion = sesionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        model.addAttribute("sesion", sesion);
        model.addAttribute("peliculas", peliculaService.listarTodas());
        model.addAttribute("salas", salaService.listarTodas());
        return "admin/sesiones/editar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        sesionService.eliminar(id);
        ra.addFlashAttribute("exito", "Sesión eliminada");
        return "redirect:/admin/sesiones";
    }
}
