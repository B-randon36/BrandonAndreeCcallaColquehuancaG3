package com.example.sistemaventas.service;

import com.example.sistemaventas.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class SaleService {

    public void registrarVenta(int productId, int cantidad, double precioTotal) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO sale (productId, cantidad, precioTotal, fechaVenta) VALUES (?, ?, ?, ?)")) {
            ps.setInt(1, productId);
            ps.setInt(2, cantidad);
            ps.setDouble(3, precioTotal);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void actualizarStock(int productId, int cantidadVendida) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE product SET stock=stock-? WHERE id=?")) {
            ps.setInt(1, cantidadVendida);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
