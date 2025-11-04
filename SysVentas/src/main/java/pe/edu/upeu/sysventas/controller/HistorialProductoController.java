package pe.edu.upeu.sysventas.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sysventas.components.Toast;
import pe.edu.upeu.sysventas.dto.SessionManager;
import pe.edu.upeu.sysventas.model.HistorialProducto;
import pe.edu.upeu.sysventas.service.IHistorialProductoService;
import pe.edu.upeu.sysventas.service.IUsuarioService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
public class HistorialProductoController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Autowired
    private IHistorialProductoService historialProductoService;

    @Autowired
    private IUsuarioService usuarioService;

    @FXML
    private TableView<HistorialProducto> tableView;

    @FXML
    private Button btnBorrarHistorial;

    @FXML
    private Button btnRefrescar;

    @FXML
    public void initialize() {
        // Verificación crítica: ¿Spring inyectó el servicio?
        if (historialProductoService == null) {
            System.err.println("FATAL: historialProductoService es null. La inyección de dependencias de Spring ha fallado.");
            // Opcional: Mostrar un Alert al usuario
            return;
        }
        configurarTabla();
        listarHistorial();
    }

    private void configurarTabla() {
        tableView.getColumns().clear(); // Limpiar columnas para evitar duplicados

        TableColumn<HistorialProducto, Long> idCol = new TableColumn<>("ID Historial");
        idCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getIdHistorialProducto()));

        TableColumn<HistorialProducto, Long> productoIdCol = new TableColumn<>("ID Producto");
        productoIdCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getIdProducto()));

        TableColumn<HistorialProducto, String> nombreProductoCol = new TableColumn<>("Nombre Producto");
        nombreProductoCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombreProducto()));
        nombreProductoCol.setPrefWidth(250);

        TableColumn<HistorialProducto, String> accionCol = new TableColumn<>("Acción");
        accionCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAccion()));

        TableColumn<HistorialProducto, String> fechaCol = new TableColumn<>("Fecha y Hora");
        fechaCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getFechaHora() != null ? cellData.getValue().getFechaHora().format(DATE_TIME_FORMATTER) : ""
        ));
        fechaCol.setPrefWidth(150);

        tableView.getColumns().addAll(idCol, productoIdCol, nombreProductoCol, accionCol, fechaCol);
    }

    private void listarHistorial() {
        try {
            List<HistorialProducto> historial = historialProductoService.findAll();
            Platform.runLater(() -> tableView.setItems(FXCollections.observableArrayList(historial)));
        } catch (Exception e) {
            System.err.println("Error al listar el historial de productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void borrarHistorial() {
        // Crear el diálogo de confirmación de contraseña
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Confirmar Eliminación");
        dialog.setHeaderText("Para borrar todo el historial, por favor ingrese su contraseña.");

        // Configurar los botones
        ButtonType confirmButtonType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // Crear el campo de contraseña
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");

        GridPane grid = new GridPane();
        grid.add(passwordField, 0, 0);
        dialog.getDialogPane().setContent(grid);

        // Habilitar el botón de confirmar solo si se escribe algo en la contraseña
        dialog.getDialogPane().lookupButton(confirmButtonType).setDisable(true);
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            dialog.getDialogPane().lookupButton(confirmButtonType).setDisable(newValue.trim().isEmpty());
        });

        // Convertir el resultado del diálogo a la contraseña ingresada
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return passwordField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(password -> {
            Long currentUserId = SessionManager.getInstance().getUserId();
            Stage stage = (Stage) btnBorrarHistorial.getScene().getWindow();

            if (usuarioService.verificarContraseña(currentUserId, password)) {
                historialProductoService.deleteAll();
                listarHistorial(); // Refrescar la tabla
                Toast.showToast(stage, "Historial de productos eliminado correctamente.", 2000);
            } else {
                Toast.showToast(stage, "Contraseña incorrecta. El historial no fue eliminado.", 3000);
            }
        });
    }

    @FXML
    private void refrescarTabla() {
        listarHistorial();
    }
}