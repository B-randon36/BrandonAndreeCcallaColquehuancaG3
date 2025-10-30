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

// Clase Controladora para productos
// Responsable de manejar la lógica del CRUD y la interacción con la vista (JavaFX)
// Conceptos POO:
// Encapsulación: atributos privados de la clase (table, botones, campos de texto)
// Abstracción: métodos que realizan operaciones complejas (CRUD) ocultando detalles
// SRP (Single Responsibility Principle): esta clase solo controla los productos y sus interacciones

import java.sql.*;
import java.time.LocalDateTime;

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

        // Asocia columnas a la tabla y define su comportamiento
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

        HBox headerBox = new HBox(12);
        headerBox.setPadding(new Insets(12));
        Label title = new Label("Zapatillas · Panel");
        title.setStyle("-fx-font-size:18px; -fx-font-weight:700; -fx-text-fill: #0f172a;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox searchGroup = new HBox(8, buscarField, buscarBtn, limpiarBtn);
        searchGroup.setPadding(new Insets(4));
        headerBox.getChildren().addAll(title, spacer, searchGroup, historialBtn);
        headerBox.setStyle("-fx-background-color: linear-gradient(to bottom right, #f8fafc, #eef2ff); -fx-background-radius:10; -fx-border-radius:10;");

        // Style search field and buttons (only UI changes, no logic changes)
        buscarField.setStyle("-fx-background-radius:8; -fx-border-radius:8; -fx-padding:6 8 6 8; -fx-border-color:#e6e9ef;");
        buscarBtn.setStyle("-fx-background-color: #0ea5e9; -fx-text-fill:white; -fx-background-radius:8; -fx-border-radius:8;");
        limpiarBtn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill:#334155;");

        historialBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #334155; -fx-font-weight:600;");

        VBox tableContainer = new VBox(8);
        tableContainer.setPadding(new Insets(10));
        ToolBar tableToolbar = new ToolBar();
        Button tbSell = new Button("Vender");
        Button tbRestore = new Button("Restaurar");
        Button tbDelete = new Button("Eliminar");
        tbSell.setOnAction(e -> registrarVenta());
        tbRestore.setOnAction(e -> restaurarProducto());
        tbDelete.setOnAction(e -> eliminarProducto());

        tbSell.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-background-radius:6;");
        tbRestore.setStyle("-fx-background-color: transparent; -fx-border-color: #e6e9ef; -fx-background-radius:6;");
        tbDelete.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-background-radius:6;");

        tableToolbar.getItems().addAll(tbSell, new Separator(), tbRestore, new Separator(), tbDelete);
        tableContainer.getChildren().addAll(tableToolbar, table);
        tableContainer.setStyle("-fx-background-color: white; -fx-padding:10; -fx-background-radius:10;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(8);
        formGrid.setVgap(8);
        formGrid.setPadding(new Insets(10));
        Label lblMarca = new Label("Marca");
        Label lblTalla = new Label("Talla");
        Label lblCategoria = new Label("Categoría");
        Label lblPrecio = new Label("Precio");
        Label lblStock = new Label("Stock");
        lblMarca.setStyle("-fx-font-weight:600; -fx-text-fill:#0f172a;");
        lblTalla.setStyle("-fx-font-weight:600; -fx-text-fill:#0f172a;");
        lblCategoria.setStyle("-fx-font-weight:600; -fx-text-fill:#0f172a;");
        lblPrecio.setStyle("-fx-font-weight:600; -fx-text-fill:#0f172a;");
        lblStock.setStyle("-fx-font-weight:600; -fx-text-fill:#0f172a;");
        formGrid.add(lblMarca, 0, 0);
        formGrid.add(marcaField, 0, 1);
        formGrid.add(lblTalla, 0, 2);
        formGrid.add(tallaField, 0, 3);
        formGrid.add(lblCategoria, 0, 4);
        formGrid.add(categoriaField, 0, 5);
        formGrid.add(lblPrecio, 0, 6);
        formGrid.add(precioField, 0, 7);
        formGrid.add(lblStock, 0, 8);
        formGrid.add(stockField, 0, 9);

        // style input fields
        String fieldStyle = "-fx-background-color: rgba(251,253,255,0.16); -fx-background-radius:8; -fx-border-radius:8; -fx-border-color:#e6e9ef; -fx-padding:6 8 6 8;";
        marcaField.setStyle(fieldStyle);
        tallaField.setStyle(fieldStyle);
        categoriaField.setStyle(fieldStyle);
        precioField.setStyle(fieldStyle);
        stockField.setStyle(fieldStyle);

        // style main action buttons without changing their behavior
        agregarBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #0ea5e9, #0284c7); -fx-text-fill: white; -fx-background-radius:8;");
        editarBtn.setStyle("-fx-background-color: #ffffff; -fx-border-color:#e6e9ef; -fx-background-radius:8;");
        eliminarBtn.setStyle("-fx-background-color: linear-gradient(to bottom right,#fb7185,#ef4444); -fx-text-fill:white; -fx-background-radius:8;");
        restaurarBtn.setStyle("-fx-background-color: #ffffff; -fx-border-color:#e6e9ef; -fx-background-radius:8;");
        ventaBtn.setStyle("-fx-background-color: linear-gradient(to bottom right,#34d399,#10b981); -fx-text-fill:white; -fx-background-radius:8;");

        HBox primaryActions = new HBox(8, agregarBtn, editarBtn);
        HBox secondaryActions = new HBox(8, eliminarBtn, restaurarBtn);
        HBox miscActions = new HBox(8, ventaBtn);
        VBox rightColumn = new VBox(12, formGrid, primaryActions, secondaryActions, miscActions);
        rightColumn.setPadding(new Insets(10));
        rightColumn.setPrefWidth(320);
        rightColumn.setStyle("-fx-background-color: transparent;");

        HBox mainArea = new HBox(12, tableContainer, rightColumn);
        mainArea.setPadding(new Insets(10));
        vbox = new VBox(10, headerBox, mainArea);
        vbox.setPadding(new Insets(12));
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom right, #f5f7fb, #eef2ff);");

        table.setStyle("-fx-table-cell-border-color: transparent; -fx-background-color: transparent;");

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                marcaField.setText(newSel.getMarca() != null ? newSel.getMarca() : "");
                tallaField.setText(String.valueOf(newSel.getTalla()));
                categoriaField.setText(newSel.getCategoria() != null ? newSel.getCategoria() : "");
                precioField.setText(String.valueOf(newSel.getPrecio()));
                stockField.setText(String.valueOf(newSel.getStock()));
            }
        });
        marcaField.focusedProperty().addListener((o, oldv, newv) -> { if (newv) marcaField.selectAll(); });
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
    // Create: Agregar nuevo producto

    private void agregarProducto() {
        String marca = marcaField.getText();
        String tallaStr = tallaField.getText();
        String categoria = categoriaField.getText();
        String precioStr = precioField.getText();
        String stockStr = stockField.getText();

        if (marca.isEmpty() || tallaStr.isEmpty() || categoria.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al agregar producto");
            alert.setHeaderText(null);
            alert.setContentText("Todos los campos son obligatorios. Por favor, complételos.");
            alert.showAndWait();
            return;
        }

        int talla;
        double precio;
        int stock;
        try {
            talla = Integer.parseInt(tallaStr);
            precio = Double.parseDouble(precioStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException nfe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de formato");
            alert.setHeaderText(null);
            alert.setContentText("Talla, precio y stock deben ser valores numéricos válidos.");
            alert.showAndWait();
            return;
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO product (marca, talla, categoria, precio, stock, eliminado) VALUES (?, ?, ?, ?, ?, FALSE)",
                     Statement.RETURN_GENERATED_KEYS)) {

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

    // Update: Editar producto seleccionado
    private void editarProducto() {
        Product seleccionado = table.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Seleccione un producto antes de editar.", ButtonType.OK);
            a.showAndWait();
            return;
        }

        String marca = marcaField.getText();
        String tallaStr = tallaField.getText();
        String categoria = categoriaField.getText();
        String precioStr = precioField.getText();
        String stockStr = stockField.getText();

        if (marca.isEmpty() || tallaStr.isEmpty() || categoria.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error al editar producto");
            a.setHeaderText(null);
            a.setContentText("Todos los campos son obligatorios. Por favor, complételos.");
            a.showAndWait();
            return;
        }

        int talla;
        double precio;
        int stock;
        try {
            talla = Integer.parseInt(tallaStr);
            precio = Double.parseDouble(precioStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException nfe) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Talla, precio y stock deben ser valores numéricos válidos.", ButtonType.OK);
            a.showAndWait();
            return;
        }

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

            int count = ps.executeUpdate();
            if (count > 0) {
                AuditController.registrarAccion("Producto editado: " + marca + " (ID: " + seleccionado.getId() + ")");
                cargarProductos();
                limpiarCampos();
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Producto actualizado correctamente.", ButtonType.OK);
                a.showAndWait();
            } else {
                Alert a = new Alert(Alert.AlertType.WARNING, "No se actualizó ningún registro. Verifique el id seleccionado.", ButtonType.OK);
                a.showAndWait();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Error al editar: " + ex.getMessage(), ButtonType.OK);
            a.showAndWait();
        }
    }

    // Soft Delete: marcar producto como eliminado
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
    // Restore: restaurar producto eliminado

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