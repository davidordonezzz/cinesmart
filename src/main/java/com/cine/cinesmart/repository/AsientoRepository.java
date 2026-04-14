package com.cine.cinesmart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cine.cinesmart.model.Asiento;

public interface AsientoRepository extends JpaRepository<Asiento, Long> {

    // en asientos encontrar id de sala ordenado por las columnas fila y columna de manera ascendente
    // cuando el usuario entra a selecionar una butaca, tiene que mostrar la sala con filas y butacas
    // en pantalla 
    List<Asiento> findBySalaIdOrderByFilaAscColumnaAsc(Long salaId);
}
