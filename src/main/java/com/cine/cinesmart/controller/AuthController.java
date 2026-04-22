package com.cine.cinesmart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cine.cinesmart.dto.RegistroDTO;
import com.cine.cinesmart.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// nos encargamos de mostrar el login, mostramos el formulario de registro
// y procesar el registro de usuarios
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    // mostramos la pagina de login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

   // Muestra el formulario de registro 
   // Mandamos un RegistroDTO vacio al HTML para que el formulario
   // pueda guardar los datos que escriba el usuario
  // RegistroDTO solo recoge los datos del formulario: 
  // nombre, email y contraseña
    //Luego el servicio usara esos datos
    //  para crear un Usuario real en la base de datos.

    @GetMapping("/registro")
    public String registroForm(Model model) {
        // creamos un registroDTo vacio para que podamos enlazar los campos
        // nombre, email y password
        if (!model.containsAttribute("registroDTO")) {
            model.addAttribute("registroDTO", new RegistroDTO());
        }
        return "registro";
    }

    // Procesamos el formulario del registro cuando el usuario lo envia
    // Valid le dice a Spring qe revise los datos del formulario para ver
    // que se cumplen el DTO
    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("registroDTO") RegistroDTO dto,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        // Si hay errores de validacion por el DTO volvemos al formulario registro
        
        if (bindingResult.hasErrors()) {
            return "registro";
        }

        try {
            // Llamamos al servicio para registrar el usuario.
        // Le pasamos nombre, email y contraseña que vienen del formulario
            usuarioService.registrar(
            // quitamos espacio tanto al principio como al final del nombre
                    dto.getNombre().trim(),
                    // quitamos espacios y lo pasamos a minusculas
                    dto.getEmail().trim().toLowerCase(),
            // cogemos la contraseña que se cifrara con BCrypt
                    dto.getPassword()
            );
            redirectAttributes.addFlashAttribute("exito", "Cuenta creada correctamente. Inicia sesión.");
            // una vez ya esta registrado el usuario lo mandamos al login
            return "redirect:/login";
            
        } catch (RuntimeException e) {
            // si el servicio no registra al usuario por ejemplo ha introducido 
            // un email ya existente, marcamos el campo email como incorrecto para mostrar
            // el error al usuario, este catch es unicamente para el correo
            bindingResult.rejectValue("email", "error.email", e.getMessage());
            return "registro";
        }
    }
}
