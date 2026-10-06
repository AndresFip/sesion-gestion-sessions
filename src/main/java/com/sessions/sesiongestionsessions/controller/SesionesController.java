package com.sessions.sesiongestionsessions.controller;

import com.sessions.sesiongestionsessions.model.Sesiones;
import com.sessions.sesiongestionsessions.repository.SesionesRepository;
import com.sessions.sesiongestionsessions.service.SesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sesiones")
public class SesionesController {

    @Autowired
    private SesionesRepository sesionesRepository;

    @Autowired
    private SesionService sesionService;

    @GetMapping
    public List<Sesiones> findAll() {
        return sesionesRepository.findAll();
    }

    @PostMapping
    public Sesiones crearSesion(@RequestBody Sesiones sesion) {
        return sesionService.crearSesion(sesion);
    }

    @PutMapping("/{id}/estado")
    public Sesiones actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        Sesiones sesion = sesionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        sesion.setEstado(Sesiones.Estado.valueOf(estado));
        return sesionesRepository.save(sesion);
    }
}