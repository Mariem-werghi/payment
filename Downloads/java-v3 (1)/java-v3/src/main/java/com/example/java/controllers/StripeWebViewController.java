package com.example.java.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Contrôleur de la fenêtre de paiement Stripe embarquée (WebView).
 *
 * Détection de paiement :
 *  1. Automatique — écoute les changements d'URL / titre de page pour des
 *     mots-clés de succès Stripe ("success", "thank", "confirmed", etc.)
 *  2. Manuelle  — bouton "✅ Paiement effectué" cliqué par l'utilisateur
 *
 * Usage depuis MarketplaceCartController :
 *   boolean paid = StripeWebViewController.openPaymentWindow(parentStage, stripeUrl);
 */
public class StripeWebViewController implements Initializable {

    // ── FXML ────────────────────────────────────────────────────────────────────
    @FXML private WebView            webView;
    @FXML private Label              lblUrl;
    @FXML private Label              lblStatus;
    @FXML private ProgressIndicator  progressIndicator;
    @FXML private HBox               successBanner;
    @FXML private Button             btnConfirm;
    @FXML private Button             btnCancel;

    // ── État ────────────────────────────────────────────────────────────────────
    private boolean paymentConfirmed = false;
    private Stage   stage;

    // Mots-clés dans l'URL ou le titre qui signalent un paiement Stripe réussi
    private static final String[] SUCCESS_KEYWORDS = {
        "success", "thank", "confirmed", "merci", "paiement-ok",
        "payment_intent", "paid", "commande-confirmee"
    };

    // ─── Initialisation ──────────────────────────────────────────────────────────

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Rien ici — loadUrl() est appelé manuellement après inject de stage
    }

    /** Injecte la référence à la Stage contenante. */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /** Charge l'URL Stripe dans le WebView et démarre la surveillance. */
    public void loadUrl(String stripeUrl) {
        WebEngine engine = webView.getEngine();

        // ── Surveiller les changements d'URL ──────────────────────────────────
        engine.locationProperty().addListener((obs, oldLoc, newLoc) -> {
            if (newLoc != null) {
                Platform.runLater(() -> lblUrl.setText(newLoc));
                verifierSucces(newLoc);
            }
        });

        // ── Surveiller le titre de la page ────────────────────────────────────
        engine.titleProperty().addListener((obs, oldTitle, newTitle) -> {
            if (newTitle != null) verifierSucces(newTitle);
        });

        // ── Indicateur de chargement ──────────────────────────────────────────
        engine.getLoadWorker().runningProperty().addListener((obs, wasRunning, isRunning) ->
            Platform.runLater(() -> progressIndicator.setVisible(isRunning))
        );

        // ── Charger l'URL ─────────────────────────────────────────────────────
        lblUrl.setText(stripeUrl.length() > 70 ? stripeUrl.substring(0, 70) + "…" : stripeUrl);
        engine.load(stripeUrl);
    }

    // ─── Détection automatique du succès ─────────────────────────────────────────

    private void verifierSucces(String text) {
        if (paymentConfirmed) return;
        String lower = text.toLowerCase();
        for (String kw : SUCCESS_KEYWORDS) {
            if (lower.contains(kw)) {
                Platform.runLater(this::confirmerPaiementAuto);
                return;
            }
        }
    }

    private void confirmerPaiementAuto() {
        if (paymentConfirmed) return;
        paymentConfirmed = true;

        // Afficher la bannière verte
        successBanner.setVisible(true);
        successBanner.setManaged(true);

        lblStatus.setText("✅  Paiement confirmé automatiquement — fermeture dans 2 secondes…");
        lblStatus.setStyle("-fx-font-size:12px; -fx-text-fill:#2d6a4f; -fx-font-weight:bold;");

        btnConfirm.setDisable(true);
        btnCancel.setDisable(true);

        // Fermer automatiquement après 2 secondes
        new Thread(() -> {
            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            Platform.runLater(() -> { if (stage != null) stage.close(); });
        }).start();
    }

    // ─── Actions boutons ─────────────────────────────────────────────────────────

    @FXML
    public void handleConfirm() {
        paymentConfirmed = true;
        if (stage != null) stage.close();
    }

    @FXML
    public void handleCancel() {
        paymentConfirmed = false;
        if (stage != null) stage.close();
    }

    // ─── Résultat ────────────────────────────────────────────────────────────────

    public boolean isPaymentConfirmed() {
        return paymentConfirmed;
    }

    // ─── Méthode statique utilitaire ─────────────────────────────────────────────

    /**
     * Ouvre la fenêtre de paiement Stripe en modal et attend la fermeture.
     *
     * @param stripeUrl URL Stripe complète (avec email pré-rempli)
     * @return true si le paiement a été confirmé, false si annulé
     */
    public static boolean openPaymentWindow(String stripeUrl) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                StripeWebViewController.class.getResource(
                    "/com/example/java/stripe-payment.fxml"));
            javafx.scene.Parent root = loader.load();
            StripeWebViewController ctrl = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("💳  Paiement Stripe — ARTERA");
            stage.setScene(new javafx.scene.Scene(root, 720, 640));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setResizable(true);

            ctrl.setStage(stage);
            ctrl.loadUrl(stripeUrl);

            stage.showAndWait();   // bloque jusqu'à la fermeture

            return ctrl.isPaymentConfirmed();

        } catch (Exception e) {
            System.err.println("[StripeWebView] Erreur : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
