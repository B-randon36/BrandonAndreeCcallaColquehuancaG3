package com.example.sistemaventas.model;

import java.time.LocalDateTime;

public class Sale {
    private int id;
    private int productId;
    private int cantidad;
    private double precioTotal;
    private LocalDateTime fechaVenta;

    public Sale(int id, int productId, int cantidad, double precioTotal, LocalDateTime fechaVenta) {
        this.id = id;
        this.productId = productId;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
        this.fechaVenta = fechaVenta;
    }

    public Sale(int productId, int cantidad, double precioTotal, LocalDateTime fechaVenta) {
        this(0, productId, cantidad, precioTotal, fechaVenta);
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }
}