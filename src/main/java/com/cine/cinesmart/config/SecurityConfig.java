package com.cine.cinesmart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // usamos bcrypt para cifrar las contraseñas para la bd
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
              // la cartelera, el detalle de la pelicula el login, el registro y archivos estaticos 
              // se puede entrar sin tener un usuario iniciado
                .requestMatchers("/", "/cartelera/**", "/pelicula/**").permitAll()
                // permitimos tambien css,js y imagenes para que la pagina web pueda cargar con esas cosas
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                
                .requestMatchers("/registro", "/login").permitAll()
                // Solo el admin puede acceder al panel de administracion
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // El resto de url requiere estar autenticado
                .anyRequest().authenticated()
            )
            // usamos login con formulario y el formulario esta en /login
            .formLogin(form -> form
                .loginPage("/login")
            // si el login es corecto mandamos al usuario al endpoint /cartelera
                .defaultSuccessUrl("/cartelera", true)
                // cualquier persona este iniciado o no puede entrar en el login
                .permitAll()
            )
            // activamos el poder al usuario cerrar sesion de su cuenta
            // y que Spring Security lo deja de considerar autenticado
            .logout(logout -> logout
                // lo mandamos a /cartelera
                .logoutSuccessUrl("/cartelera")
               
                .permitAll()
            );
        // aplicamos todas las reglas de seguridad que hemos configurado arriba
        return http.build();
    }
}
