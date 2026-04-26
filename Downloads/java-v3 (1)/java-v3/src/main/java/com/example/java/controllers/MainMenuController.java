package com.example.java.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    public void ouvrirMarketplacePosts() {
        ouvrirFenetre("/com/example/java/marketplace-post-list.fxml",
                      "Gestion des MarketplacePosts", 900, 550);
    }

    @FXML
    public void ouvrirOrdres() {
        ouvrirFenetre("/com/example/java/ordre-list.fxml",
                      "Gestion des Ordres", 650, 450);
    }

    @FXML
    public void ouvrirLignesOrdre() {
        ouvrirFenetre("/com/example/java/ligne-ordre-list.fxml",
                      "Gestion des Lignes d'Ordre", 700, 450);
    }

    private void ouvrirFenetre(String fxmlPath, String titre, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titre);
            stage.setScene(new Scene(root, width, height));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur ouverture " + titre + " : " + e.getMessage());
        }
    }
}
