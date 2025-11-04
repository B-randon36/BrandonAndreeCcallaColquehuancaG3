package pe.edu.upeu.sysventas.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sysventas.components.StageManager;
import pe.edu.upeu.sysventas.components.Toast;
import pe.edu.upeu.sysventas.dto.SessionManager;
import pe.edu.upeu.sysventas.model.Usuario;
import pe.edu.upeu.sysventas.service.IUsuarioService;

import java.io.IOException;

@Controller
//@Component
public class LoginController {

    @Autowired
    private ApplicationContext context;
    @Autowired
    IUsuarioService us;
    @FXML
    TextField txtUsuario;
    @FXML
    PasswordField txtClave;
    @FXML
    Button btnIngresar;

    @FXML
    public void initialize() {
        // Listener para la tecla Enter en el campo de usuario
        txtUsuario.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });

        // Listener para la tecla Enter en el campo de contraseña
        txtClave.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });
    }


    @FXML
    public void cerrar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        Platform.exit();
        System.exit(0);
    }


    @FXML
    public void login(ActionEvent event) {
        handleLogin();
    }

    private void handleLogin() {
        try {
            Usuario usu=us.loginUsuario(txtUsuario.getText(), new String(txtClave.getText()));
            if (usu!=null) {
                // --- INICIO: Mensaje de Bienvenida ---
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Inicio de Sesión Exitoso");
                alert.setHeaderText(null);
                alert.setContentText("¡Bienvenido, " + usu.getUser() + "!");
                alert.showAndWait();
                // --- FIN: Mensaje de Bienvenida ---

                SessionManager.getInstance().setUserId(usu.getIdUsuario());
                SessionManager.getInstance().setUserName(usu.getUser());
                SessionManager.getInstance().setUserPerfil(usu.getIdPerfil().getNombre());
                FXMLLoader loader = new  FXMLLoader(getClass().getResource("/view/maingui.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent mainRoot = loader.load();
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getBounds();
                Scene mainScene = new Scene(mainRoot,bounds.getWidth(), bounds.getHeight()-30);
                mainScene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                Stage stage = (Stage) btnIngresar.getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResource("/img/store.png").toExternalForm()));
                stage.setScene(mainScene);
                stage.setTitle("SysVentas SysCenterLife");
                stage.setX(bounds.getMinX());
                stage.setY(bounds.getMinY());
                stage.setResizable(true);
                StageManager.setPrimaryStage(stage);
                stage.setWidth(bounds.getWidth());
                stage.setHeight(bounds.getHeight());
                stage.show();
            } else {
                Stage stage = (Stage) btnIngresar.getScene().getWindow();
                double with=stage.getWidth()*2;
                double h=stage.getHeight()/2;
                System.out.println(with + " h:"+h);
                Toast.showToast(stage, "Credencial invalido!! intente nuevamente", 2000, with, h);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
