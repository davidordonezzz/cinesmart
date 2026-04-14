package com.cine.cinesmart.repository;

import com.cine.cinesmart.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface SesionRepository extends JpaRepository<Sesion, Long> {
    List<Sesion> findByPeliculaIdAndFechaHoraAfterOrderByFechaHoraAsc(Long peliculaId, LocalDateTime fecha);
    List<Sesion> findByPeliculaId(Long peliculaId);
}
