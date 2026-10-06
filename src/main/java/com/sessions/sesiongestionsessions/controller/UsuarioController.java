package com.sessions.sesiongestionsessions.controller;

import com.sessions.sesiongestionsessions.model.Usuario;
import com.sessions.sesiongestionsessions.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @GetMapping("/hacer-admin-directo")
    public String hacerAdminDirecto() {
        int filas = jdbcTemplate.update("UPDATE usuarios SET rol_acceso = 'ADMIN' WHERE correo = 'tovarandres79@gmail.com'");
        return "¡Filas actualizadas a ADMIN: " + filas + "!";
    }

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @GetMapping("/usuarios/ingenieros")
    public List<Usuario> getIngenieros() {
        return usuarioRepository.findByRol(Usuario.Rol.INGENIERO);
    }
}