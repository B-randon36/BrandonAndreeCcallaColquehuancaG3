package com.example.sistemaventas.model;

import java.time.LocalDateTime;

public class Product {
    private int id;
    private String marca;
    private int talla;
    private String categoria;
    private double precio;
    private int stock;
    private boolean eliminado;
    private LocalDateTime fechaEdicion;
    private LocalDateTime fechaEliminacion;

    public Product(int id, String marca, int talla, String categoria, double precio, int stock, boolean eliminado,
                   LocalDateTime fechaEdicion, LocalDateTime fechaEliminacion) {
        this.id = id;
        this.marca = marca;
        this.talla = talla;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.eliminado = eliminado;
        this.fechaEdicion = fechaEdicion;
        this.fechaEliminacion = fechaEliminacion;
    }

    public Product(String marca, int talla, String categoria, double precio, int stock) {
        this(0, marca, talla, categoria, precio, stock, false, null, null);
    }

    public int getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public int getTalla() {
        return talla;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public LocalDateTime getFechaEdicion() {
        return fechaEdicion;
    }

    public LocalDateTime getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setTalla(int talla) {
        this.talla = talla;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public void setFechaEdicion(LocalDateTime fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    public void setFechaEliminacion(LocalDateTime fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }
}