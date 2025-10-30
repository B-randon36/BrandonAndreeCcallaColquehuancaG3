package com.example.sistemaventas.service;

import com.example.sistemaventas.model.Product;
import com.example.sistemaventas.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM product")) {
            while (rs.next()) products.add(mapResultSetToProduct(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return products;
    }

    public void agregarProducto(Product p) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO product (marca, talla, categoria, precio, stock, eliminado) VALUES (?, ?, ?, ?, ?, FALSE)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getMarca());
            ps.setInt(2, p.getTalla());
            ps.setString(3, p.getCategoria());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) p.setId(rs.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void editarProducto(Product p) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE product SET marca=?, talla=?, categoria=?, precio=?, stock=?, fechaEdicion=? WHERE id=?")) {
            ps.setString(1, p.getMarca());
            ps.setInt(2, p.getTalla());
            ps.setString(3, p.getCategoria());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(7, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminarProducto(int productId) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE product SET eliminado=TRUE, fechaEliminacion=? WHERE id=?")) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void restaurarProducto(int productId) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE product SET eliminado=FALSE, fechaEliminacion=NULL WHERE id=?")) {
            ps.setInt(1, productId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Product> buscarProductos(String marca) {
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM product WHERE marca LIKE ?")) {
            ps.setString(1, "%" + marca + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) products.add(mapResultSetToProduct(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return products;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("marca"),
                rs.getInt("talla"),
                rs.getString("categoria"),
                rs.getDouble("precio"),
                rs.getInt("stock"),
                rs.getBoolean("eliminado"),
                rs.getTimestamp("fechaEdicion") != null ? rs.getTimestamp("fechaEdicion").toLocalDateTime() : null,
                rs.getTimestamp("fechaEliminacion") != null ? rs.getTimestamp("fechaEliminacion").toLocalDateTime() : null
        );
    }
}
