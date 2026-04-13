package com.cine.cinesmart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cine.cinesmart.model.Pelicula;

public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {

    
    // Consulta para filtrar peliculas en la cartelera
    // Si titulo, genero o edad son nulos es decir esta vacio, ese filtro se ignora
    // es decir que el usuario no ha selecionado el filtro por lo cual se ignora 
    // con distinct evitamos que una pelicula salga repetida
    @Query("SELECT DISTINCT p FROM Pelicula p LEFT JOIN p.sesiones s WHERE " +
           "(:titulo IS NULL OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) AND " +
           "(:genero IS NULL OR p.genero = :genero) AND " +
           "(:edad IS NULL OR p.edadRecomendada = :edad)")
    // metodo que recibe los filtros desde la cartelera y nos la muestra
    List<Pelicula> filtrar(@Param("titulo") String titulo,
                           @Param("genero") String genero,
                           @Param("edad") String edad);

    @Query("SELECT DISTINCT p.genero FROM Pelicula p ORDER BY p.genero")
    List<String> findAllGeneros();

    @Query("SELECT DISTINCT p.edadRecomendada FROM Pelicula p WHERE p.edadRecomendada IS NOT NULL ORDER BY p.edadRecomendada")
    List<String> findAllEdades();
}
