package com.sessions.sesiongestionsessions.controller;

import com.sessions.sesiongestionsessions.model.Usuario;
import com.sessions.sesiongestionsessions.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam String correo,
                                   @RequestParam String nombre,
                                   @RequestParam String apellido,
                                   @RequestParam String contrasena,
                                   Model model) {

        if (usuarioRepository.findByCorreo(correo).isPresent()) {
            model.addAttribute("error", "Ya existe un usuario con ese correo");
            return "registro";
        }

        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setContrasena(contrasena);
        usuario.setRol(Usuario.Rol.TECNICO);
        usuario.setRolAcceso(Usuario.RolAcceso.ESPECTADOR);

        usuarioRepository.save(usuario);

        return "redirect:/login";
    }
}