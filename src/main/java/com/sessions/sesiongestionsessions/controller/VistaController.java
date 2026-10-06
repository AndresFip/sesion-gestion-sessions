package com.sessions.sesiongestionsessions.controller;

import com.sessions.sesiongestionsessions.model.Sesiones;
import com.sessions.sesiongestionsessions.model.Usuario;
import com.sessions.sesiongestionsessions.repository.SesionesRepository;
import com.sessions.sesiongestionsessions.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;

@Controller
public class VistaController {
    @Autowired
    private SesionesRepository sesionesRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/panel")
    public String mostrarPanel(Model model) {
        List<Sesiones> pendiente = sesionesRepository.findByEstado(Sesiones.Estado.PENDIENTE);
        List<Sesiones> en_proceso = sesionesRepository.findByEstado(Sesiones.Estado.EN_PROCESO);
        model.addAttribute("pendiente", pendiente);
        model.addAttribute("en_proceso", en_proceso);
        model.addAttribute("finalizado", sesionesRepository.findByEstado(Sesiones.Estado.FINALIZADO));
        model.addAttribute("sesiones", sesionesRepository.findAll());
        model.addAttribute("ingenieros", usuarioRepository.findAll());
        model.addAttribute("tipoSesion", Sesiones.TipoSesion.values());

        List<Usuario> listaIngenieros = usuarioRepository.findByRol(Usuario.Rol.INGENIERO);
        Map<Usuario, Long> conteo = new HashMap<>();
        for  (Usuario ing : listaIngenieros) {
            long enPendiente = pendiente.stream().filter(u -> u.getIngeniero().getId().equals(ing.getId())).count();
            long enProceso = en_proceso.stream().filter(u -> u.getIngeniero().getId().equals(ing.getId())).count();
            long cantidad = enPendiente + enProceso;
                    conteo.put(ing, cantidad);
        }
        model.addAttribute("conteoIngenieros", conteo);

        return "panel";
    }

    @Autowired
    private com.sessions.sesiongestionsessions.service.SesionService sesionService;

    @PostMapping("/panel/crear")
    public String crearSesionDesdeFormulario(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            @RequestParam(required = false) String tecnicoNombre,
            @RequestParam(required = false) String observaciones,
            @RequestParam(required = false) String tipoSesion,
            @RequestParam(required = false) String ticket,
            @RequestParam Long ingenieroId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            Sesiones sesion = new Sesiones();
            sesion.setFechaInicio(LocalDateTime.parse(fechaInicio));
            sesion.setFechaFin(LocalDateTime.parse(fechaFin));
            sesion.setEstado(Sesiones.Estado.PENDIENTE);

            Usuario ingeniero = new Usuario();
            ingeniero.setId(ingenieroId);
            sesion.setIngeniero(ingeniero);
            sesion.setTecnicoNombre(tecnicoNombre);
            sesion.setObservaciones(observaciones);
            sesion.setTicket(ticket);
            sesion.setTipoSesion(Sesiones.TipoSesion.valueOf(tipoSesion));

            Object rol = session.getAttribute("rolAcceso");
            if (rol != null && rol.toString().equals("INGENIERO")) {
                sesion.setIngenieroResponsable((String) session.getAttribute("usuarioNombre"));
            }

            sesionService.crearSesion(sesion);

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/panel/nueva";
        }

        return "redirect:/panel";
    }

    @PostMapping("/panel/actualizar-fila/{id}")
    public String actualizarFila(
            @PathVariable Long id,
            @RequestParam(required = false) String tecnicoNombre,
            @RequestParam(required = false) String observaciones,
            @RequestParam(required = false) String ticket,
            @RequestParam(required = false) String ingenieroResponsable) {

        Sesiones sesion = sesionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        sesion.setTecnicoNombre(tecnicoNombre);
        sesion.setObservaciones(observaciones);
        sesion.setTicket(ticket);
        sesion.setIngenieroResponsable(ingenieroResponsable);

        sesionesRepository.save(sesion);
        return "redirect:/panel";
    }

    @PostMapping("/panel/finalizar/{id}")
    public String finalizar(@PathVariable Long id) {

        Sesiones sesion = sesionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        sesion.setEstado(Sesiones.Estado.FINALIZADO);
        sesion.setFechaFin(java.time.LocalDateTime.now());

        sesionesRepository.save(sesion);
        return "redirect:/panel";
    }

    @GetMapping("/panel/editar/{id}")
    public String mostrarEditar(@PathVariable Long id, Model model) {
        Sesiones sesion = sesionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        model.addAttribute("sesion", sesion);
        model.addAttribute("ingenieros", usuarioRepository.findAll());
        model.addAttribute("tiposSesion", Sesiones.TipoSesion.values());
        return "editar";
    }


    @PostMapping("/panel/editar/{id}")
    public String guardarEdicion(
            @PathVariable Long id,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            @RequestParam Long ingenieroId,
            @RequestParam(required = false) String tecnicoNombre,
            @RequestParam(required = false) String ingenieroResponsable,
            @RequestParam(required = false) String observaciones,
            @RequestParam(required = false) String ticket,
            @RequestParam(required = false) String tipoSesion,
            RedirectAttributes redirectAttributes) {

        LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
        LocalDateTime fin = LocalDateTime.parse(fechaFin);

        if (!fin.isAfter(inicio)) {
            redirectAttributes.addFlashAttribute("error", "La fecha de fin debe ser posterior a la fecha de inicio");
            return "redirect:/panel/editar/" + id;
        }

        Sesiones sesion = sesionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        Usuario ingeniero = usuarioRepository.findById(ingenieroId)
                .orElseThrow(() -> new RuntimeException("Ingeniero no encontrado"));

        sesion.setFechaInicio(inicio);
        sesion.setFechaFin(fin);
        sesion.setIngeniero(ingeniero);
        sesion.setTecnicoNombre(tecnicoNombre);
        sesion.setIngenieroResponsable(ingenieroResponsable);
        sesion.setObservaciones(observaciones);
        sesion.setTicket(ticket);
        sesion.setTipoSesion(Sesiones.TipoSesion.valueOf(tipoSesion));

        sesionesRepository.save(sesion);
        return "redirect:/panel";
    }


    @GetMapping("/panel/nueva")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("tipoSesion", Sesiones.TipoSesion.values());
        model.addAttribute("ingenieros", usuarioRepository.findAll());
        return "crear";
    }



}
