package com.sessions.sesiongestionsessions.controller;

import com.sessions.sesiongestionsessions.model.Usuario;
import com.sessions.sesiongestionsessions.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/panel/usuarios")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listarUsuarios(HttpSession session, Model model) {
        if (!esAdmin(session)) {
            return "redirect:/panel";
        }
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("roles", Usuario.RolAcceso.values());
        return "usuarios";
    }

    @PostMapping("/actualizar-rol/{id}")
    public String actualizarRol(@PathVariable Long id,
                                @RequestParam String rolAcceso,
                                HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/panel";
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setRolAcceso(Usuario.RolAcceso.valueOf(rolAcceso));
        usuarioRepository.save(usuario);

        return "redirect:/panel/usuarios";
    }
    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!esAdmin(session)) {
            return "redirect:/panel";
        }

        try {
            usuarioRepository.deleteById(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar: este usuario tiene sesiones asociadas");
        }

        return "redirect:/panel/usuarios";
    }

    private boolean esAdmin(HttpSession session) {
        Object rol = session.getAttribute("rolAcceso");
        return rol != null && rol.toString().equals("ADMIN");
    }
}