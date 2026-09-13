package com.sessions.sesiongestionsessions.service;

import com.sessions.sesiongestionsessions.model.Sesiones;
import com.sessions.sesiongestionsessions.model.Usuario;
import com.sessions.sesiongestionsessions.repository.SesionesRepository;
import com.sessions.sesiongestionsessions.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        Usuario tecnicoReal = usuarioRepository.findById(sesion.getTecnico().getId())
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
        Usuario ingenieroReal = usuarioRepository.findById(sesion.getIngeniero().getId())
                .orElseThrow(() -> new RuntimeException("Ingeniero no encontrado"));

        if (tecnicoReal.getRol() != Usuario.Rol.TECNICO) {
            throw new RuntimeException("El usuario asignado como técnico no tiene el rol TECNICO");
        }
        if (ingenieroReal.getRol() != Usuario.Rol.INGENIERO) {
            throw new RuntimeException("El usuario asignado como ingeniero no tiene el rol INGENIERO");
        }

        boolean conflicto = hayConflicto(ingenieroReal.getId(), sesion.getFechaInicio(), sesion.getFechaFin());
        if (conflicto) {
            throw new RuntimeException("El ingeniero ya tiene una sesión en ese horario");
        }
        sesion.setTecnico(tecnicoReal);
        sesion.setIngeniero(ingenieroReal);

        return sesionesRepository.save(sesion);
    }

}