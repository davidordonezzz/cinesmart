// Clase que carga datos iniciales al arrancar la aplicacion
// Sirve para tener usuarios, peliculas, salas, asientos y sesiones
// sin tener que crearlos manualmente cada vez
package com.cine.cinesmart.init;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cine.cinesmart.model.Asiento;
import com.cine.cinesmart.model.Pelicula;
import com.cine.cinesmart.model.Rol;
import com.cine.cinesmart.model.Sala;
import com.cine.cinesmart.model.Sesion;
import com.cine.cinesmart.model.Usuario;
import com.cine.cinesmart.repository.AsientoRepository;
import com.cine.cinesmart.repository.PeliculaRepository;
import com.cine.cinesmart.repository.SalaRepository;
import com.cine.cinesmart.repository.SesionRepository;
import com.cine.cinesmart.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
// se ejecuta con CommandLineRunner para ejecutar codigo automaticamente
// al arrancar springboot
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepo;
    private final PeliculaRepository peliculaRepo;
    private final SalaRepository salaRepo;
    private final AsientoRepository asientoRepo;
    private final SesionRepository sesionRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    // Este metodo se ejecuta automaticamente cuando arracamos el proyecto
    public void run(String... args) {
        
        // solo carga los usuarios si no tenemos (es decir si son 0)
        if (usuarioRepo.count() == 0) {
            //Primera vezc rear usuarios, salas, asientos y películas
            usuarioRepo.save(Usuario.builder()
                    .nombre("Admin").email("admin@cinesmart.com")
                    .password(passwordEncoder.encode("admin123")).rol(Rol.ADMIN).build());
            usuarioRepo.save(Usuario.builder()
                    .nombre("David").email("david@correo.com")
                    .password(passwordEncoder.encode("user123")).rol(Rol.USER).build());

            Sala sala1 = salaRepo.save(Sala.builder().nombre("Sala 1").filas(8).columnas(10).build());
            Sala sala2 = salaRepo.save(Sala.builder().nombre("Sala 2").filas(6).columnas(8).build());
            Sala sala3 = salaRepo.save(Sala.builder().nombre("Sala 3").filas(10).columnas(12).build());
            generarAsientos(sala1);
            generarAsientos(sala2);
            generarAsientos(sala3);

            peliculaRepo.save(Pelicula.builder().titulo("Interstellar").genero("Ciencia Ficción").duracion(169)
                    .sinopsis("Un grupo de exploradores viaja a través de un agujero de gusano en busca de un nuevo hogar para la humanidad.")
                    .edadRecomendada("12").imagenUrl("https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg").build());
            peliculaRepo.save(Pelicula.builder().titulo("El Padrino").genero("Drama").duracion(175)
                    .sinopsis("La historia de la familia Corleone, una de las más poderosas dinastías del crimen organizado en América.")
                    .edadRecomendada("18").imagenUrl("https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg").build());
            peliculaRepo.save(Pelicula.builder().titulo("Gladiator II").genero("Acción").duracion(148)
                    .sinopsis("Lucius, sobrino de Cómodo, lucha por sobrevivir en la arena y vengar a quienes ama en la Roma Imperial.")
                    .edadRecomendada("16").imagenUrl("https://image.tmdb.org/t/p/w500/wTnV3PCVW5O92JMrFvvrRcV39RU.jpg").build());
            peliculaRepo.save(Pelicula.builder().titulo("Oppenheimer").genero("Drama").duracion(180)
                    .sinopsis("La historia del físico J. Robert Oppenheimer y su papel en el desarrollo de la bomba atómica.")
                    .edadRecomendada("16").imagenUrl("https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg").build());
            peliculaRepo.save(Pelicula.builder().titulo("Spider-Man: No Way Home").genero("Acción").duracion(148)
                    .sinopsis("Peter Parker pide ayuda al Doctor Strange cuando su identidad secreta es revelada al mundo.")
                    .edadRecomendada("12").imagenUrl("https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg").build());
            peliculaRepo.save(Pelicula.builder().titulo("Top Gun: Maverick").genero("Acción").duracion(130)
                    .sinopsis("El piloto Pete Mitchell desafía las normas y entrena a una nueva generación de as de la aviación.")
                    .edadRecomendada("7").imagenUrl("https://image.tmdb.org/t/p/w500/iiZZdoQBEYBv6id8su7ImL0oCbD.jpg").build());
            peliculaRepo.save(Pelicula.builder().titulo("Deadpool & Wolverine").genero("Acción").duracion(128)
                    .sinopsis("Deadpool recluta a un reticente Wolverine para salvar su universo de una amenaza imparable.")
                    .edadRecomendada("18").imagenUrl("https://image.tmdb.org/t/p/w500/8cdWjvZQUExUUTzyp4t6EDMubfO.jpg").build());
            peliculaRepo.save(Pelicula.builder().titulo("Shrek").genero("Animación").duracion(90)
                    .sinopsis("Un ogro solitario emprende una misión para rescatar a una princesa y recuperar su pantano.")
                    .edadRecomendada("TP").imagenUrl("https://image.tmdb.org/t/p/w500/iB64vpL3dIObOtMZgX3RqdVdQDc.jpg").build());
            peliculaRepo.save(Pelicula.builder().titulo("Joker").genero("Thriller").duracion(122)
                    .sinopsis("Arthur Fleck, un comediante fracasado en Gotham City, desciende a la locura y se convierte en el Joker.")
                    .edadRecomendada("18").imagenUrl("https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg").build());
            peliculaRepo.save(Pelicula.builder().titulo("El Señor de los Anillos: El Retorno del Rey").genero("Acción").duracion(201)
                    .sinopsis("La batalla por la Tierra Media llega a su épico final mientras Frodo se acerca al Monte del Destino.")
                    .edadRecomendada("12").imagenUrl("https://image.tmdb.org/t/p/w500/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg").build());

        }

        // Sesiones: solo crearlas si no existen todavía
        if (sesionRepo.count() == 0) {
            java.util.List<Pelicula> peliculas = peliculaRepo.findAll();
            java.util.List<Sala> salas = salaRepo.findAll();
        // solo creo las sesiones si tengo al menos 10 peli y 3 salas
            if (peliculas.size() >= 10 && salas.size() >= 3) {
                Pelicula p1  = peliculas.get(0);
                Pelicula p2  = peliculas.get(1);
                Pelicula p3  = peliculas.get(2);
                Pelicula p4  = peliculas.get(3);
                Pelicula p5  = peliculas.get(4);
                Pelicula p6  = peliculas.get(5);
                Pelicula p7  = peliculas.get(6);
                Pelicula p8  = peliculas.get(7);
                Pelicula p9  = peliculas.get(8);
                Pelicula p10 = peliculas.get(9);
                Sala sala1 = salas.get(0);
                Sala sala2 = salas.get(1);
                Sala sala3 = salas.get(2);

                // Fechas fijas de las sesiones hasta 2027
                sesionRepo.save(Sesion.builder().pelicula(p1).sala(sala1).fechaHora(LocalDateTime.of(2027, 6, 10, 16, 0)).precio(8.50).build());
                sesionRepo.save(Sesion.builder().pelicula(p1).sala(sala1).fechaHora(LocalDateTime.of(2027, 6, 10, 20, 30)).precio(9.50).build());
                sesionRepo.save(Sesion.builder().pelicula(p2).sala(sala2).fechaHora(LocalDateTime.of(2027, 6, 10, 18, 0)).precio(8.50).build());
                sesionRepo.save(Sesion.builder().pelicula(p3).sala(sala3).fechaHora(LocalDateTime.of(2027, 6, 11, 12, 0)).precio(7.00).build());
                sesionRepo.save(Sesion.builder().pelicula(p3).sala(sala1).fechaHora(LocalDateTime.of(2027, 6, 11, 16, 30)).precio(7.00).build());
                sesionRepo.save(Sesion.builder().pelicula(p4).sala(sala2).fechaHora(LocalDateTime.of(2027, 6, 10, 21, 0)).precio(9.50).build());
                sesionRepo.save(Sesion.builder().pelicula(p5).sala(sala3).fechaHora(LocalDateTime.of(2027, 6, 12, 17, 0)).precio(8.50).build());
                sesionRepo.save(Sesion.builder().pelicula(p6).sala(sala1).fechaHora(LocalDateTime.of(2027, 6, 11, 20, 0)).precio(10.00).build());
                sesionRepo.save(Sesion.builder().pelicula(p6).sala(sala3).fechaHora(LocalDateTime.of(2027, 6, 12, 20, 30)).precio(10.00).build());
                sesionRepo.save(Sesion.builder().pelicula(p7).sala(sala2).fechaHora(LocalDateTime.of(2027, 6, 10, 19, 0)).precio(9.50).build());
                sesionRepo.save(Sesion.builder().pelicula(p7).sala(sala3).fechaHora(LocalDateTime.of(2027, 6, 11, 22, 0)).precio(9.50).build());
                sesionRepo.save(Sesion.builder().pelicula(p8).sala(sala1).fechaHora(LocalDateTime.of(2027, 6, 11, 11, 0)).precio(7.00).build());
                sesionRepo.save(Sesion.builder().pelicula(p9).sala(sala2).fechaHora(LocalDateTime.of(2027, 6, 12, 21, 30)).precio(9.50).build());
                sesionRepo.save(Sesion.builder().pelicula(p10).sala(sala3).fechaHora(LocalDateTime.of(2027, 6, 10, 15, 0)).precio(8.50).build());

            }
        }
    }

    private void generarAsientos(Sala sala) {
        // empezamops por la fila 1 hasta el numero total de filas que existe en esa sala
        for (int f = 1; f <= sala.getFilas(); f++) {
        // Dentro de cada fila, vamos columna por columna
        // desde la columna 1 hasta la ultima columna de la sala
            for (int c = 1; c <= sala.getColumnas(); c++) {
                // Creamos y guardamos un asiento con la sala,
                // la fila actual y la columna actual.
                asientoRepo.save(Asiento.builder().sala(sala).fila(f).columna(c).build());
            }
        }
    }
}
