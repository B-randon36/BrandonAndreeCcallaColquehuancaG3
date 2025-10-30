package com.example.sistemaventas.controller;

import com.example.sistemaventas.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AuditController {
    public static void registrarAccion(String descripcion) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO auditlog (descripcion, fechaHora) VALUES (?, ?)")) {
            ps.setString(1, descripcion);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}