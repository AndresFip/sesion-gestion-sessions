package com.sessions.sesiongestionsessions.repository;

import com.sessions.sesiongestionsessions.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}