package com.cine.cinesmart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cine.cinesmart.model.Compra;

public interface CompraRepository extends JpaRepository<Compra, Long> {

  // Comprueba si esa butaca ya esta comprada en esa sesion
// Esto evita vender la misma butaca a dos personas distintas
    boolean existsBySesionIdAndAsientoId(Long sesionId, Long asientoId);

    // con esto sabemos que asiento estan ocupados en cada seccion
    // Esta consulta busca todas las compras de una sesión 
    // y devuelve solo los ids de los asientos comprados. Así sé qué butacas pintar en rojo.
    @Query("SELECT c.asiento.id FROM Compra c WHERE c.sesion.id = :sesionId")
    List<Long> findAsientoIdsBySesionId(@Param("sesionId") Long sesionId);

    // buscas todas las compras donde el usuarioid este ordenado por las compras de las fechas 
    // mas recientes, este metodo se usa para el apartado mis entradas
    List<Compra> findByUsuarioIdOrderByFechaCompraDesc(Long usuarioId);
}
