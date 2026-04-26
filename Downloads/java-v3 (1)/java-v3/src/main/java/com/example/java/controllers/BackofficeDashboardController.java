package com.example.java.controllers;

import com.example.java.services.LigneOrdreService;
import com.example.java.services.MarketplacePostService;
import com.example.java.services.OrdreService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class BackofficeDashboardController implements Initializable {

    // ─── Labels métriques ────────────────────────────────────────────────────────
    @FXML private Label totalPostsLbl;
    @FXML private Label totalOrdresLbl;
    @FXML private Label totalLignesLbl;
    @FXML private Label activePostsLbl;
    @FXML private Label totalRevenusLbl;

    // ─── Services ────────────────────────────────────────────────────────────────
    private final MarketplacePostService postService   = new MarketplacePostService();
    private final OrdreService           ordreService  = new OrdreService();
    private final LigneOrdreService      ligneService  = new LigneOrdreService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chargerMetriques();
    }

    private void chargerMetriques() {
        // ── Total MarketplacePosts ──
        try {
            int total = postService.afficherAll().size();
            totalPostsLbl.setText(String.valueOf(total));
        } catch (Exception e) {
            totalPostsLbl.setText("—");
        }

        // ── Posts actifs ──
        try {
            long actifs = postService.afficherAll().stream()
                    .filter(p -> "active".equalsIgnoreCase(p.getStatus()))
                    .count();
            activePostsLbl.setText(String.valueOf(actifs));
        } catch (Exception e) {
            activePostsLbl.setText("—");
        }

        // ── Total Ordres ──
        try {
            int total = ordreService.afficherAll().size();
            totalOrdresLbl.setText(String.valueOf(total));
        } catch (Exception e) {
            totalOrdresLbl.setText("—");
        }

        // ── Total Lignes d'Ordre ──
        try {
            int total = ligneService.afficherAll().size();
            totalLignesLbl.setText(String.valueOf(total));
        } catch (Exception e) {
            totalLignesLbl.setText("—");
        }

        // ── Revenus totaux (somme des totalAmount de orders) ──
        try {
            double revenus = ordreService.afficherAll().stream()
                    .mapToDouble(o -> o.getTotalAmount())
                    .sum();
            totalRevenusLbl.setText(String.format("%.2f €", revenus));
        } catch (Exception e) {
            totalRevenusLbl.setText("—");
        }
    }
}
