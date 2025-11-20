package pe.edu.upeu.sysventas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysventas.model.Historial;
import pe.edu.upeu.sysventas.service.IHistorialService;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

@Component
public class HistorialController implements Initializable {

    @FXML
    private TableView<Historial> tableView;

    @Autowired
    private IHistorialService historialService;

    private ObservableList<Historial> historialList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        historialList = FXCollections.observableArrayList();
        setupTableView();
        listarHistorial();
    }

    private void setupTableView() {
        TableColumn<Historial, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Historial, String> entityCol = new TableColumn<>("Entidad");
        entityCol.setCellValueFactory(new PropertyValueFactory<>("entityName"));

        TableColumn<Historial, String> actionCol = new TableColumn<>("Acción");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("actionType"));

        TableColumn<Historial, LocalDateTime> timestampCol = new TableColumn<>("Fecha y Hora");
        timestampCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        TableColumn<Historial, String> detailsCol = new TableColumn<>("Detalles");
        detailsCol.setCellValueFactory(new PropertyValueFactory<>("details"));

        tableView.getColumns().addAll(idCol, entityCol, actionCol, timestampCol, detailsCol);
        tableView.setItems(historialList);
    }

    private void listarHistorial() {
        historialList.setAll(historialService.findAll());
    }
}
