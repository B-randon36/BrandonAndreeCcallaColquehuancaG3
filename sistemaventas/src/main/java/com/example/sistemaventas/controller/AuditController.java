package com.example.sistemaventas.controller;

import com.example.sistemaventas.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

// Controlador encargado de registrar las acciones del sistema en la tabla auditlog
// Aplica los principios de POO
// Responsabilidad única (SRP): solo se encarga de registrar eventos en la base de datos.
// Abstracción: oculta los detalles de cómo se guarda el registro.
// Reutilización: puede ser llamado desde cualquier otra clase del sistema.
public class AuditController {

    // estatico para registrar una accion en la base de datos
    // notificaciones para confirmar
    public static void registrarAccion(String descripcion) {
        try (
                // Obtiene la conexión activa a la base de datos usando DBUtil (encapsulación)
                Connection conn = DBUtil.getConnection();

                // Prepara la consulta SQL para insertar un registro en la tabla auditlog
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO auditlog (descripcion, fechaHora) VALUES (?, ?)")
        ) {
            // Asigna el valor del primer parámetro (?) con la descripción que recibe el método
            ps.setString(1, descripcion);

            // Asigna el valor del segundo parámetro con la fecha y hora actual del sistema
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

            // Ejecuta la instrucción SQL (INSERT) para guardar el registro en la base de datos
            ps.executeUpdate();

        } catch (SQLException ex) {
            // Si ocurre algún error en la conexión o en el SQL, se imprime la traza para depuración
            ex.printStackTrace();
        }
    }
}