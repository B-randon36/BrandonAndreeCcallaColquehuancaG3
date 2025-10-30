package com.example.sistemaventas.util;

import java.sql.*;

public class DBUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "sistemaventas";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";
    private static Connection connection;

    public static void initDatabase() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            stmt.close();
            conn.close();

            connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
            Statement st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS product (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "marca VARCHAR(50)," +
                    "talla INT," +
                    "categoria VARCHAR(50)," +
                    "precio DOUBLE," +
                    "stock INT," +
                    "eliminado BOOLEAN DEFAULT FALSE," +
                    "fechaEdicion DATETIME NULL," +
                    "fechaEliminacion DATETIME NULL" +
                    ")");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS sale (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "productId INT," +
                    "cantidad INT," +
                    "precioTotal DOUBLE," +
                    "fechaVenta DATETIME," +
                    "FOREIGN KEY (productId) REFERENCES product(id)" +
                    ")");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS auditlog (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "descripcion VARCHAR(255)," +
                    "fechaHora DATETIME" +
                    ")");
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
        }
        return connection;
    }
}