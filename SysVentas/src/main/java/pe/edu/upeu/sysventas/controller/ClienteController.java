package pe.edu.upeu.sysventas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysventas.model.Cliente;
import pe.edu.upeu.sysventas.service.IClienteService;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class ClienteController implements Initializable {

    @FXML
    private TextField txtDniRuc;
    @FXML
    private TextField txtNombres;
    @FXML
    private TextField txtDireccion;
    @FXML
    private Button btnGuardar;
    @FXML
    private TableView<Cliente> tableView;

    @Autowired
    private IClienteService clienteService;

    private ObservableList<Cliente> clienteList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteList = FXCollections.observableArrayList();
        setupTableView();
        listarClientes();

        btnGuardar.setOnAction(event -> guardarCliente());
    }

    private void setupTableView() {
        TableColumn<Cliente, String> dniCol = new TableColumn<>("DNI/RUC");
        dniCol.setCellValueFactory(new PropertyValueFactory<>("dniruc"));

        TableColumn<Cliente, String> nombresCol = new TableColumn<>("Nombres");
        nombresCol.setCellValueFactory(new PropertyValueFactory<>("nombres"));

        TableColumn<Cliente, String> direccionCol = new TableColumn<>("Dirección");
        direccionCol.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        tableView.getColumns().addAll(dniCol, nombresCol, direccionCol);
        tableView.setItems(clienteList);
    }

    private void listarClientes() {
        clienteList.setAll(clienteService.findAll());
    }

    @FXML
    private void guardarCliente() {
        Cliente cliente = new Cliente();
        cliente.setDniruc(txtDniRuc.getText());
        cliente.setNombres(txtNombres.getText());
        cliente.setDireccion(txtDireccion.getText());

        clienteService.save(cliente);

        limpiarFormulario();
        listarClientes();
    }

    private void limpiarFormulario() {
        txtDniRuc.clear();
        txtNombres.clear();
        txtDireccion.clear();
    }
}
