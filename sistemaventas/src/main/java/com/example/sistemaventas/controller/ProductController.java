package com.example.sistemaventas.controller;

import com.example.sistemaventas.model.Product;
import com.example.sistemaventas.model.AuditLog;
import com.example.sistemaventas.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
    private TableView<Product> table;
    private ObservableList<Product> productList;
    private TextField marcaField, tallaField, categoriaField, precioField, stockField, buscarField;
    private Button agregarBtn, editarBtn, eliminarBtn, restaurarBtn, buscarBtn, limpiarBtn, ventaBtn, historialBtn;
    private VBox vbox;

    public ProductController() {
        table = new TableView<>();
        productList = FXCollections.observableArrayList();
        cargarProductos();

        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Product, String> marcaCol = new TableColumn<>("Marca");
        marcaCol.setCellValueFactory(new PropertyValueFactory<>("marca"));
        TableColumn<Product, Integer> tallaCol = new TableColumn<>("Talla");
        tallaCol.setCellValueFactory(new PropertyValueFactory<>("talla"));
        TableColumn<Product, String> categoriaCol = new TableColumn<>("Categoría");
        categoriaCol.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        TableColumn<Product, Double> precioCol = new TableColumn<>("Precio");
        precioCol.setCellValueFactory(new PropertyValueFactory<>("precio"));
        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Product, Boolean> eliminadoCol = new TableColumn<>("Eliminado");
        eliminadoCol.setCellValueFactory(new PropertyValueFactory<>("eliminado"));

        table.getColumns().addAll(idCol, marcaCol, tallaCol, categoriaCol, precioCol, stockCol, eliminadoCol);
        table.setItems(productList);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        marcaField = new TextField();
        marcaField.setPromptText("Marca");
        tallaField = new TextField();
        tallaField.setPromptText("Talla");
        categoriaField = new TextField();
        categoriaField.setPromptText("Categoría");
        precioField = new TextField();
        precioField.setPromptText("Precio");
        stockField = new TextField();
        stockField.setPromptText("Stock");

        buscarField = new TextField();
        buscarField.setPromptText("Buscar por marca");

        agregarBtn = new Button("Agregar");
        editarBtn = new Button("Editar");
        eliminarBtn = new Button("Eliminar");
        restaurarBtn = new Button("Restaurar");
        buscarBtn = new Button("Buscar");
        limpiarBtn = new Button("Limpiar");
        ventaBtn = new Button("Registrar Venta");
        historialBtn = new Button("Ver Historial");

        agregarBtn.setOnAction(e -> agregarProducto());
        editarBtn.setOnAction(e -> editarProducto());
        eliminarBtn.setOnAction(e -> eliminarProducto());
        restaurarBtn.setOnAction(e -> restaurarProducto());
        buscarBtn.setOnAction(e -> buscarProducto());
        limpiarBtn.setOnAction(e -> cargarProductos());
        ventaBtn.setOnAction(e -> registrarVenta());
        historialBtn.setOnAction(e -> mostrarHistorial());

        HBox fieldsBox = new HBox(10, marcaField, tallaField, categoriaField, precioField, stockField, agregarBtn, editarBtn, eliminarBtn, restaurarBtn, ventaBtn);
        fieldsBox.setPadding(new Insets(10));
        HBox buscarBox = new HBox(10, buscarField, buscarBtn, limpiarBtn, historialBtn);
        buscarBox.setPadding(new Insets(10));
        vbox = new VBox(10, fieldsBox, buscarBox, table);
        vbox.setPadding(new Insets(15));
    }

    public VBox getView() {
        return vbox;
    }

    private void cargarProductos() {
        productList.clear();
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM product")) {
            while (rs.next()) {
                Product p = new Product(
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
                productList.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void agregarProducto() {
        String marca = marcaField.getText();
        String tallaStr = tallaField.getText();
        String categoria = categoriaField.getText();
        String precioStr = precioField.getText();
        String stockStr = stockField.getText();
        if (marca.isEmpty() || tallaStr.isEmpty() || categoria.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
            return;
        }
        int talla = Integer.parseInt(tallaStr);
        double precio = Double.parseDouble(precioStr);
        int stock = Integer.parseInt(stockStr);
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO product (marca, talla, categoria, precio, stock, eliminado) VALUES (?, ?, ?, ?, ?, FALSE)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, marca);
            ps.setInt(2, talla);
            ps.setString(3, categoria);
            ps.setDouble(4, precio);
            ps.setInt(5, stock);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = 0;
            if (rs.next()) id = rs.getInt(1);
            AuditController.registrarAccion("Producto agregado: " + marca + " (ID: " + id + ")");
            cargarProductos();
            limpiarCampos();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void editarProducto() {
        Product seleccionado = table.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        String marca = marcaField.getText();
        String tallaStr = tallaField.getText();
        String categoria = categoriaField.getText();
        String precioStr = precioField.getText();
        String stockStr = stockField.getText();
        if (marca.isEmpty() || tallaStr.isEmpty() || categoria.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
            return;
        }
        int talla = Integer.parseInt(tallaStr);
        double precio = Double.parseDouble(precioStr);
        int stock = Integer.parseInt(stockStr);
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE product SET marca=?, talla=?, categoria=?, precio=?, stock=?, fechaEdicion=? WHERE id=?")) {
            ps.setString(1, marca);
            ps.setInt(2, talla);
            ps.setString(3, categoria);
            ps.setDouble(4, precio);
            ps.setInt(5, stock);
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(7, seleccionado.getId());
            ps.executeUpdate();
            AuditController.registrarAccion("Producto editado: " + marca + " (ID: " + seleccionado.getId() + ")");
            cargarProductos();
            limpiarCampos();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void eliminarProducto() {
        Product seleccionado = table.getSelectionModel().getSelectedItem();
        if (seleccionado == null || seleccionado.isEliminado()) return;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE product SET eliminado=TRUE, fechaEliminacion=? WHERE id=?")) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, seleccionado.getId());
            ps.executeUpdate();
            AuditController.registrarAccion("Producto eliminado: " + seleccionado.getMarca() + " (ID: " + seleccionado.getId() + ")");
            cargarProductos();
            limpiarCampos();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void restaurarProducto() {
        Product seleccionado = table.getSelectionModel().getSelectedItem();
        if (seleccionado == null || !seleccionado.isEliminado()) return;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE product SET eliminado=FALSE, fechaEliminacion=NULL WHERE id=?")) {
            ps.setInt(1, seleccionado.getId());
            ps.executeUpdate();
            AuditController.registrarAccion("Producto restaurado: " + seleccionado.getMarca() + " (ID: " + seleccionado.getId() + ")");
            cargarProductos();
            limpiarCampos();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void buscarProducto() {
        String filtro = buscarField.getText();
        productList.clear();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM product WHERE marca LIKE ?")) {
            ps.setString(1, "%" + filtro + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product(
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
                productList.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void registrarVenta() {
        Product seleccionado = table.getSelectionModel().getSelectedItem();
        if (seleccionado == null || seleccionado.isEliminado() || seleccionado.getStock() <= 0) return;
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Registrar Venta");
        Label label = new Label("Cantidad a vender:");
        TextField cantidadField = new TextField();
        Button okBtn = new Button("OK");
        VBox vb = new VBox(10, label, cantidadField, okBtn);
        vb.setPadding(new Insets(20));
        Scene scene = new Scene(vb, 250, 120);
        dialog.setScene(scene);

        okBtn.setOnAction(e -> {
            String cantidadStr = cantidadField.getText();
            if (!cantidadStr.matches("\\d+")) return;
            int cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0 || cantidad > seleccionado.getStock()) return;
            SaleController.registrarVenta(seleccionado.getId(), cantidad, seleccionado.getPrecio() * cantidad);
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "UPDATE product SET stock=stock-? WHERE id=?")) {
                ps.setInt(1, cantidad);
                ps.setInt(2, seleccionado.getId());
                ps.executeUpdate();
                cargarProductos();
                AuditController.registrarAccion("Venta registrada de " + cantidad + " x " + seleccionado.getMarca() + " (ID: " + seleccionado.getId() + ")");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            dialog.close();
        });
        dialog.showAndWait();
    }

    private void mostrarHistorial() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Historial de Acciones y Ventas");
        TableView<AuditLog> tableLog = new TableView<>();
        TableColumn<AuditLog, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<AuditLog, String> descCol = new TableColumn<>("Descripción");
        descCol.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        TableColumn<AuditLog, String> fechaCol = new TableColumn<>("Fecha y Hora");
        fechaCol.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        tableLog.getColumns().addAll(idCol, descCol, fechaCol);
        tableLog.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ObservableList<AuditLog> logs = FXCollections.observableArrayList();
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM auditlog ORDER BY fechaHora DESC")) {
            while (rs.next()) {
                AuditLog log = new AuditLog(
                        rs.getInt("id"),
                        rs.getString("descripcion"),
                        rs.getTimestamp("fechaHora").toLocalDateTime()
                );
                logs.add(log);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tableLog.setItems(logs);
        VBox vb = new VBox(10, tableLog);
        vb.setPadding(new Insets(10));
        Scene scene = new Scene(vb, 600, 400);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void limpiarCampos() {
        marcaField.clear();
        tallaField.clear();
        categoriaField.clear();
        precioField.clear();
        stockField.clear();
    }
}