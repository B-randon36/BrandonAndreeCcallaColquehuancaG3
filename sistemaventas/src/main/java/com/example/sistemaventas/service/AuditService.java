package com.example.sistemaventas.service;

import com.example.sistemaventas.model.AuditLog;
import com.example.sistemaventas.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditService {

    public void registrarAccion(String descripcion) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO auditlog (descripcion, fechaHora) VALUES (?, ?)")) {
            ps.setString(1, descripcion);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<AuditLog> getAllLogs() {
        List<AuditLog> logs = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM auditlog ORDER BY fechaHora DESC")) {
            while (rs.next()) {
                logs.add(new AuditLog(
                        rs.getInt("id"),
                        rs.getString("descripcion"),
                        rs.getTimestamp("fechaHora").toLocalDateTime()
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return logs;
    }
}
