package com.cine.cinesmart.controller;

import com.cine.cinesmart.service.PeliculaService;
import com.cine.cinesmart.service.SalaService;
import com.cine.cinesmart.service.SesionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Controlador principal del panel de administración
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PeliculaService peliculaService;
    private final SalaService salaService;
    private final SesionService sesionService;

    // Dashboard con resumen general del sistema
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalPeliculas", peliculaService.listarTodas().size());
        model.addAttribute("totalSalas", salaService.listarTodas().size());
        model.addAttribute("totalSesiones", sesionService.listarTodas().size());
        return "admin/dashboard";
    }
}
