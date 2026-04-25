package com.cine.cinesmart.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cine.cinesmart.model.Asiento;
import com.cine.cinesmart.model.Compra;
import com.cine.cinesmart.model.Sesion;
import com.cine.cinesmart.model.Usuario;
import com.cine.cinesmart.service.CompraService;
import com.cine.cinesmart.service.SalaService;
import com.cine.cinesmart.service.SesionService;
import com.cine.cinesmart.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CompraController {

    private final SesionService sesionService;
    private final SalaService salaService;
    private final CompraService compraService;
    private final UsuarioService usuarioService;

    // este metodo es para la seleccion de butacas
    @GetMapping("/sesion/{id}/butacas")
    public String seleccionarButaca(@PathVariable Long id, Model model) {
        Sesion sesion = sesionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        // Si la sesión es en Sala 1, busca todos los asientos de Sala 1.
        List<Asiento> asientos = salaService.obtenerAsientos(sesion.getSala().getId());
        // buscamos los asientos que estan ocupados
        List<Long> ocupados = compraService.obtenerAsientosOcupados(id);

        // mandamos al html la session, todos los asientos, los ocupatos y la sala
        model.addAttribute("sesion", sesion);
        model.addAttribute("asientos", asientos);
        model.addAttribute("ocupados", ocupados);
        model.addAttribute("sala", sesion.getSala());
        return "seleccion-butacas";
    }
    // recibimos el formulario de la compra
    // el usuario no se manda desde el formulario, lo obtenemos del login con 
    // spring security
    @PostMapping("/comprar")
    public String comprar(@RequestParam Long sesionId,
                          @RequestParam Long asientoId,
                          @AuthenticationPrincipal UserDetails userDetails,
         
                          RedirectAttributes redirectAttributes) {
         // llamamos al servicio para realizar la compra
         // opasamos el email del usuario logeado, la sesion y el asiento que ha elelgido                   
        try {
            Compra compra = compraService.realizarCompra(userDetails.getUsername(), sesionId, asientoId);
            // si la compra se ha echo mandamos al usuario a la pantalla de confirmacion
            // de la compra
            return "redirect:/compra/confirmacion/" + compra.getId();
            // si la compra falla
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/sesion/" + sesionId + "/butacas";
        }
    }
    // Muestra la pantalla de confirmacion despues de comprar.
// Recibe el id de la compra, busca esa compra y la manda al HTML
    @GetMapping("/compra/confirmacion/{id}")
    public String confirmacion(@PathVariable Long id, Model model) {
        // buscamos en la bd la compra con el id que viene en la url
        Compra compra = compraService.findById(id);
        model.addAttribute("compra", compra);

        return "compra-confirmacion";
    }
    // Muestra el apartado Mis Entradas
    // solo se muestran las compras del usuario que ha iniciado sesion
    @GetMapping("/mis-compras")
    public String misCompras(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // obtenemos el usuario logueado usando el email que esta guardado en springsecurity
        Usuario usuario = usuarioService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                    // Buscamos las compras de ese usuario.
        List<Compra> compras = compraService.obtenerComprasUsuario(usuario.getId());
        model.addAttribute("compras", compras);
        return "mis-compras";
    }
}
