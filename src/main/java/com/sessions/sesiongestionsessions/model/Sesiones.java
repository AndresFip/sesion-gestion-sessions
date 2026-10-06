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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;

    public enum Estado {
        PENDIENTE,
        EN_PROCESO,
        FINALIZADO
    }

    @Column(name = "tecnico_nombre")
    private String tecnicoNombre;

    @Column(name = "ingeniero_responsable")
    private String ingenieroResponsable;

    @ManyToOne
    @JoinColumn(name = "ingeniero_id", nullable = false)
    private Usuario ingeniero;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private  TipoSesion tipoSesion;

    public enum TipoSesion {
        Sesion_Cliente,
        Visita_Tecnica
    }

    @Column(columnDefinition = "TEXT")
    private String ticket;

    public long getDuracionMinutos() {
        if (fechaInicio == null || fechaFin == null) {
            return 0;
        }
        return java.time.Duration.between(fechaInicio, fechaFin).toMinutes();
    }


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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getTecnicoNombre() {
        return tecnicoNombre;
    }

    public void setTecnicoNombre(String tecnicoNombre) {
        this.tecnicoNombre = tecnicoNombre;
    }

    public String getIngenieroResponsable() {
        return ingenieroResponsable;
    }

    public void setIngenieroResponsable(String ingenieroResponsable) {
        this.ingenieroResponsable = ingenieroResponsable;
    }

    public Usuario getIngeniero() {
        return ingeniero;
    }

    public void setIngeniero(Usuario ingeniero) {
        this.ingeniero = ingeniero;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public TipoSesion getTipoSesion() {
        return tipoSesion;
    }

    public void setTipoSesion(TipoSesion tipoSesion) {
        this.tipoSesion = tipoSesion;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sesiones sesiones = (Sesiones) o;
        return Objects.equals(id, sesiones.id) && Objects.equals(fechaInicio, sesiones.fechaInicio) && Objects.equals(fechaFin, sesiones.fechaFin) && estado == sesiones.estado && Objects.equals(tecnicoNombre, sesiones.tecnicoNombre) && Objects.equals(ingenieroResponsable, sesiones.ingenieroResponsable) && Objects.equals(ingeniero, sesiones.ingeniero) && Objects.equals(observaciones, sesiones.observaciones) && tipoSesion == sesiones.tipoSesion && Objects.equals(ticket, sesiones.ticket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaInicio, fechaFin, estado, tecnicoNombre, ingenieroResponsable, ingeniero, observaciones, tipoSesion, ticket);
    }
}




