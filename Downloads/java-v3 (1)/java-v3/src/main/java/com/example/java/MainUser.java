package com.example.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Point d'entrée de l'application Front-End (utilisateur / acheteur).
 * Lance la marketplace Artera côté client.
 *
 * Run : mvn javafx:run -Djavafx.mainClass=com.example.java.MainUser
 */
public class MainUser extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/example/java/marketplace-user-shell.fxml"));
        Scene scene = new Scene(loader.load(), 1280, 780);

        // CSS front-end utilisateur
        scene.getStylesheets().add(
            getClass().getResource("/com/example/java/artera-user.css").toExternalForm());

        primaryStage.setTitle("Artera – Marketplace");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(620);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
