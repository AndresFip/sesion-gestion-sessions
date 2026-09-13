package com.sessions.sesiongestionsessions.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "SESIONES")
public class Sesiones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaFin;

    @Column(nullable = false)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Usuario tecnico;

    @ManyToOne
    @JoinColumn(name = "ingeniero_id")
    private Usuario ingeniero;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public Usuario getTecnico() {
        return tecnico;
    }

    public void setTecnico(Usuario tecnico) {
        this.tecnico = tecnico;
    }

    public Usuario getIngeniero() {
        return ingeniero;
    }

    public void setIngeniero(Usuario ingeniero) {
        this.ingeniero = ingeniero;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sesiones sesiones = (Sesiones) o;
        return Objects.equals(id, sesiones.id) && Objects.equals(fechaInicio, sesiones.fechaInicio) && Objects.equals(fechaFin, sesiones.fechaFin) && Objects.equals(estado, sesiones.estado) && Objects.equals(tecnico, sesiones.tecnico) && Objects.equals(ingeniero, sesiones.ingeniero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaInicio, fechaFin, estado, tecnico, ingeniero);
    }
}

