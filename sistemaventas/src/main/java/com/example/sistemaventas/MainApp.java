package com.example.sistemaventas;

import com.example.sistemaventas.controller.ProductController;
import com.example.sistemaventas.util.DBUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        DBUtil.initDatabase();
        ProductController controller = new ProductController();
        Scene scene = new Scene(controller.getView(), 1000, 600);
        primaryStage.setTitle("Sistema de Venta de Zapatillas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}