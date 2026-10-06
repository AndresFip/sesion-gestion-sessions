package com.sessions.sesiongestionsessions.repository;

import com.sessions.sesiongestionsessions.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByRol(Usuario.Rol rol);
    Optional<Usuario> findByCorreo(String correo);
}