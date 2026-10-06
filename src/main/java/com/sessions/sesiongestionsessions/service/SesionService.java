package com.sessions.sesiongestionsessions.service;

import com.sessions.sesiongestionsessions.model.Sesiones;
import com.sessions.sesiongestionsessions.model.Usuario;
import com.sessions.sesiongestionsessions.repository.SesionesRepository;
import com.sessions.sesiongestionsessions.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class SesionService {

    @Autowired
    private SesionesRepository sesionesRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean hayConflicto(Long ingenieroId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Sesiones> conflictos = sesionesRepository.buscarConflictos(ingenieroId, fechaInicio, fechaFin);
        return !conflictos.isEmpty();
    }

    public Sesiones crearSesion(Sesiones sesion) {
        if (sesion.getFechaInicio().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La fecha de inicio no puede ser anterior a la actual");
        }

        if (!sesion.getFechaFin().isAfter(sesion.getFechaInicio())) {
            throw new RuntimeException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        Usuario ingenieroReal = usuarioRepository.findById(sesion.getIngeniero().getId())
                .orElseThrow(() -> new RuntimeException("Ingeniero no encontrado"));

        boolean conflicto = hayConflicto(ingenieroReal.getId(), sesion.getFechaInicio(), sesion.getFechaFin());
        if (conflicto) {
            throw new RuntimeException("El ingeniero ya tiene una sesión en ese horario");
        }
        sesion.setIngeniero(ingenieroReal);

        return sesionesRepository.save(sesion);
    }
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void cambiarEstado() {
        LocalDateTime ahora = LocalDateTime.now();

        List<Sesiones> pendientes = sesionesRepository.findByEstado(Sesiones.Estado.PENDIENTE);
        for (Sesiones s : pendientes) {
            if (ahora.isAfter(s.getFechaFin())) {
                s.setEstado(Sesiones.Estado.FINALIZADO);
                sesionesRepository.save(s);
            } else if (ahora.isAfter(s.getFechaInicio())) {
                s.setEstado(Sesiones.Estado.EN_PROCESO);
                sesionesRepository.save(s);
            }
        }
    }


}