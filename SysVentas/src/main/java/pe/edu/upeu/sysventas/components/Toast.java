package pe.edu.upeu.sysventas.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Toast {

    public static void showToast(Stage ownerStage, String message, int durationInMillis) {
        // Crear una etiqueta con el mensaje del toast
        Label label = new Label(message);
        label.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; " // Un verde más oscuro para mejor contraste
                + "-fx-padding: 12px; -fx-border-radius: 6px; -fx-background-radius: 6px; "
                + "-fx-font-size: 14px; -fx-font-weight: bold;");
        label.setOpacity(0);  // Inicialmente invisible

        // Crear un Popup para mostrar el toast
        Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);

        // Añadir la etiqueta al Popup
        StackPane pane = new StackPane(label);
        pane.setPadding(new Insets(10));
        pane.setAlignment(Pos.CENTER);
        popup.getContent().add(pane);

        // Calcular la posición para la esquina superior derecha
        double toastX = ownerStage.getX() + ownerStage.getWidth() - pane.getWidth() - 20;
        double toastY = ownerStage.getY() + 20;

        // Mostrar el Popup en la posición calculada
        popup.show(ownerStage, toastX, toastY);

        // Crear una animación para el toast (fade in -> esperar -> fade out)
        Timeline fadeIn = new Timeline(new KeyFrame(Duration.millis(300), new KeyValue(label.opacityProperty(), 1)));
        Timeline fadeOut = new Timeline(new KeyFrame(Duration.millis(300), new KeyValue(label.opacityProperty(), 0)));

        // Programar el tiempo que el toast será visible
        Timeline delay = new Timeline(new KeyFrame(Duration.millis(durationInMillis)));
        delay.setOnFinished(event -> fadeOut.play());

        // Ejecutar la animación (fade in -> delay -> fade out)
        fadeIn.play();
        fadeIn.setOnFinished(event -> delay.play());
        fadeOut.setOnFinished(event -> popup.hide());  // Ocultar popup al finalizar fade out
    }

    // Sobrecarga para mantener compatibilidad con el código existente, aunque no se usen x e y.
    public static void showToast(Stage ownerStage, String message, int durationInMillis, double x, double y) {
        showToast(ownerStage, message, durationInMillis);
    }
}
