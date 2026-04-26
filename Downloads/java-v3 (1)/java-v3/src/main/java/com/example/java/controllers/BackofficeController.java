package com.example.java.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BackofficeController implements Initializable {

    // ─── Boutons de navigation (sidebar) ─────────────────────────────────────────
    // MODIF : navOrdres et navLignes supprimés — les commandes sont automatiques
    @FXML private Button navDashboard;
    @FXML private Button navMarketplace;

    // ─── Zone titre + contenu ─────────────────────────────────────────────────────
    @FXML private Label     pageTitle;
    @FXML private StackPane contentPane;

    private List<Button> navButtons;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        navButtons = List.of(navDashboard, navMarketplace);
        // Afficher le tableau de bord au démarrage
        showDashboard();
    }

    // ─── Navigation ───────────────────────────────────────────────────────────────

    @FXML
    public void showDashboard() {
        load("/com/example/java/backoffice-dashboard.fxml",
             "Tableau de bord", navDashboard);
    }

    @FXML
    public void showMarketplace() {
        load("/com/example/java/marketplace-post-list.fxml",
             "Modération des publications", navMarketplace);
    }
    // NOTE : showOrdres() et showLignes() supprimés.
    // Les commandes sont créées automatiquement lors du checkout utilisateur.
    // Les statistiques restent visibles sur le Dashboard.

    // ─── Loader interne ──────────────────────────────────────────────────────────

    private void load(String fxmlPath, String title, Button activeBtn) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();

            contentPane.getChildren().setAll(view);
            pageTitle.setText(title);

            // Mettre en surbrillance le bouton actif
            for (Button b : navButtons) {
                b.getStyleClass().remove("bo-nav-active");
            }
            if (activeBtn != null) {
                activeBtn.getStyleClass().add("bo-nav-active");
            }
        } catch (IOException e) {
            System.err.println("Erreur chargement " + fxmlPath + " : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
