package com.sessions.sesiongestionsessions.repository;

import com.sessions.sesiongestionsessions.model.Sesiones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SesionesRepository extends JpaRepository<Sesiones, Long> {

    @Query("SELECT s FROM Sesiones s " +
            "WHERE s.ingeniero.id = :ingenieroId " +
            "AND s.fechaInicio < :fechaFin " +
            "AND s.fechaFin > :fechaInicio")
    List<Sesiones> buscarConflictos(
            @Param("ingenieroId") Long ingenieroId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    List<Sesiones> findByEstado(Sesiones.Estado estado);
}
