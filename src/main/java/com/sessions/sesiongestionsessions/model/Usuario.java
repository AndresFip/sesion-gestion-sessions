package com.sessions.sesiongestionsessions.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "USUARIOS")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column
    private String apellido;

    @Column(nullable = false, unique = true)
    private String correo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;
    @Column
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_acceso")
    private RolAcceso rolAcceso;

    public enum RolAcceso {
        ADMIN, COORDINADOR, INGENIERO, ESPECTADOR
    }

    public enum Rol {
        TECNICO,
        INGENIERO;
    }
    public Usuario() {
    }

    public Usuario(String nombre, String correo, Rol rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public RolAcceso getRolAcceso() {
        return rolAcceso;
    }

    public void setRolAcceso(RolAcceso rolAcceso) {
        this.rolAcceso = rolAcceso;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) && Objects.equals(nombre, usuario.nombre) && Objects.equals(correo, usuario.correo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, correo, rol);
    }
}