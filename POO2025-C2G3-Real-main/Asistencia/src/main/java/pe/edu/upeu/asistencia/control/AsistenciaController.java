package pe.edu.upeu.asistencia.control;

import javafx.fxml.FXML;
import org.springframework.stereotype.Controller;

import java.awt.*;

@Controller
public class AsistenciaController {

    @FXML
    TextField txt1,txt2;
    @FXML
    Label label1;

    @FXML
    public void sumar(){
        double num1 = Double.parseDouble(txt1.getText());
        double num2 = Double.parseDouble(txt2.getText());
        double resultado = num1 + num2;
        txt1.setText(String.valueOf(resultado));
    }
}
