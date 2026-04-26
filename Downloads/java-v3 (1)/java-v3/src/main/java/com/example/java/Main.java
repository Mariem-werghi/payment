package com.example.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/example/java/backoffice.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 700);

        // Appliquer le CSS de Mariem (artera.css)
        scene.getStylesheets().add(
            getClass().getResource("/com/example/java/artera.css").toExternalForm());

        primaryStage.setTitle("Artera – Backoffice Marketplace");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
