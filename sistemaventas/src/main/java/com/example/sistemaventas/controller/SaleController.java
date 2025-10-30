package com.example.sistemaventas.controller;

import com.example.sistemaventas.model.Sale;
import com.example.sistemaventas.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class SaleController {
    public static void registrarVenta(int productId, int cantidad, double precioTotal) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO sale (productId, cantidad, precioTotal, fechaVenta) VALUES (?, ?, ?, ?)")) {
            ps.setInt(1, productId);
            ps.setInt(2, cantidad);
            ps.setDouble(3, precioTotal);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}