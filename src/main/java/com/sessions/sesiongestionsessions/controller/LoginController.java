package com.sessions.sesiongestionsessions.controller;

import com.sessions.sesiongestionsessions.model.Usuario;
import com.sessions.sesiongestionsessions.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                @RequestParam String contrasena,
                                HttpSession session,
                                Model model) {

        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);

        if (usuario == null || usuario.getContrasena() == null || !usuario.getContrasena().equals(contrasena)) {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            return "login";
        }

        session.setAttribute("usuarioId", usuario.getId());
        session.setAttribute("usuarioNombre", usuario.getNombre());
        session.setAttribute("rolAcceso", usuario.getRolAcceso());

        return "redirect:/panel";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}