package com.cine.cinesmart.controller;

import com.cine.cinesmart.model.Sala;
import com.cine.cinesmart.service.SalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/salas")
@RequiredArgsConstructor
public class AdminSalaController {

    private final SalaService salaService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("salas", salaService.listarTodas());
        return "admin/salas/lista";
    }

    @GetMapping("/nueva")
    public String nuevaForm(Model model) {
        model.addAttribute("sala", new Sala());
        return "admin/salas/nueva";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Sala sala, RedirectAttributes ra) {
        salaService.guardar(sala);
        ra.addFlashAttribute("exito", "Sala guardada (asientos generados automáticamente)");
        return "redirect:/admin/salas";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Sala sala = salaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));
        model.addAttribute("sala", sala);
        return "admin/salas/editar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        salaService.eliminar(id);
        ra.addFlashAttribute("exito", "Sala eliminada");
        return "redirect:/admin/salas";
    }
}
