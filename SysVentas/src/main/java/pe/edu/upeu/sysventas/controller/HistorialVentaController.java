package pe.edu.upeu.sysventas.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sysventas.model.HistorialVenta;
import pe.edu.upeu.sysventas.service.IHistorialVentaService;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class HistorialVentaController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Autowired
    private IHistorialVentaService historialVentaService;

    @FXML
    private TableView<HistorialVenta> tableView;

    private ObservableList<HistorialVenta> listaHistorial;

    @FXML
    public void initialize() {
        listarHistorial();
    }

    private void listarHistorial() {
        Platform.runLater(() -> {
            try {
                List<HistorialVenta> historial = historialVentaService.findAll();
                listaHistorial = FXCollections.observableArrayList(historial);
                tableView.setItems(listaHistorial);

                // Limpiar columnas existentes para evitar duplicados
                tableView.getColumns().clear();

                // Columna ID Historial
                TableColumn<HistorialVenta, Long> idCol = new TableColumn<>("ID Historial");
                idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getIdHistorialVenta()));
                idCol.setPrefWidth(100);

                // Columna ID Venta (con manejo de nulos)
                TableColumn<HistorialVenta, String> ventaIdCol = new TableColumn<>("ID Venta");
                ventaIdCol.setCellValueFactory(cellData -> {
                    HistorialVenta data = cellData.getValue();
                    if (data != null && data.getVenta() != null && data.getVenta().getIdVenta() != null) {
                        return new SimpleStringProperty(data.getVenta().getIdVenta().toString());
                    }
                    return new SimpleStringProperty("N/A");
                });
                ventaIdCol.setPrefWidth(100);

                // Columna Acción
                TableColumn<HistorialVenta, String> accionCol = new TableColumn<>("Acción");
                accionCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAccion()));
                accionCol.setPrefWidth(150);

                // Columna Fecha y Hora (con formato)
                TableColumn<HistorialVenta, String> fechaCol = new TableColumn<>("Fecha y Hora");
                fechaCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaHora().format(DATE_TIME_FORMATTER)));
                fechaCol.setPrefWidth(200);

                tableView.getColumns().addAll(idCol, ventaIdCol, accionCol, fechaCol);
            } catch (Exception e) {
                System.err.println("Error al listar el historial de ventas: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}