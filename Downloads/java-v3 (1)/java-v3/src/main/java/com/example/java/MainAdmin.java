package com.example.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Point d'entrée de l'application Backoffice (administrateur).
 * Lance le tableau de bord d'administration Artera.
 *
 * Run : mvn javafx:run -Djavafx.mainClass=com.example.java.MainAdmin
 */
public class MainAdmin extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/example/java/backoffice.fxml"));
        Scene scene = new Scene(loader.load(), 1280, 750);

        scene.getStylesheets().add(
            getClass().getResource("/com/example/java/artera.css").toExternalForm());

        primaryStage.setTitle("Artera – Administration Backoffice");
        primaryStage.setMinWidth(960);
        primaryStage.setMinHeight(640);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
