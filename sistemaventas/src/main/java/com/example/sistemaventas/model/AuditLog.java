package com.example.sistemaventas.model;

import java.time.LocalDateTime;

public class AuditLog {
    private int id;
    private String descripcion;
    private LocalDateTime fechaHora;

    public AuditLog(int id, String descripcion, LocalDateTime fechaHora) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
    }

    public AuditLog(String descripcion, LocalDateTime fechaHora) {
        this(0, descripcion, fechaHora);
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}