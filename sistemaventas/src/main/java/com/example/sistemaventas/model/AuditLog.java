package com.example.sistemaventas.model;

import java.time.LocalDateTime;

public class AuditLog {
    private int id;
    private String descripcion;
    private LocalDateTime fechaHora;

    public AuditLog() {}

    public AuditLog(int id, String descripcion, LocalDateTime fechaHora) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
