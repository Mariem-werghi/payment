package com.example.java.controllers;

import com.example.java.entities.MarketplacePost;
import com.example.java.services.MarketplacePostService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Contrôleur du formulaire "Publier une œuvre" côté User (front).
 * L'ajout de MarketplacePost a été déplacé du backoffice vers ici.
 * sellerId = 1 (utilisateur connecté), statut = "active" automatiquement.
 */
public class MarketplaceSellController implements Initializable {

    private static final int CURRENT_USER_ID = 1; // utilisateur connecté

    // ─── FXML ─────────────────────────────────────────────────────────────────────
    @FXML private ComboBox<String> cbProductType;
    @FXML private TextField        tfPrice;
    @FXML private TextField        tfAvailableQty;
    @FXML private TextField        tfArtworkId;
    @FXML private CheckBox         chkUnlimited;
    @FXML private Label            lblQtyNote;
    @FXML private VBox             resultBox;
    @FXML private Label            lblResult;
    @FXML private Button           btnPublish;
    @FXML private Button           btnReset;

    private final MarketplacePostService service = new MarketplacePostService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbProductType.setItems(FXCollections.observableArrayList(
            "original", "print", "digital", "painting",
            "sculpture", "photography", "drawing", "other"
        ));
        cbProductType.setValue("original");

        // Masquer/afficher le champ quantité selon le checkbox
        chkUnlimited.selectedProperty().addListener((obs, old, val) -> {
            tfAvailableQty.setDisable(val);
            lblQtyNote.setVisible(val);
        });
        resultBox.setVisible(false);
    }

    // ─── Publication ──────────────────────────────────────────────────────────────

    @FXML
    public void handlePublish() {
        if (!valider()) return;

        try {
            double  price    = Double.parseDouble(tfPrice.getText().trim());
            int     qty      = chkUnlimited.isSelected()
                                   ? 0 : Integer.parseInt(tfAvailableQty.getText().trim());
            String  type     = cbProductType.getValue();
            boolean unlim    = chkUnlimited.isSelected();
            int     artworkId = Integer.parseInt(tfArtworkId.getText().trim());

            MarketplacePost post = new MarketplacePost(
                price,
                "active",          // statut auto
                qty,
                0,                 // soldQuantity = 0 au départ
                type,
                unlim,
                LocalDateTime.now(),
                null,
                artworkId,
                CURRENT_USER_ID   // sellerId = utilisateur connecté
            );

            service.ajouter(post);

            // Afficher le message de succès
            resultBox.setVisible(true);
            lblResult.setText("✅  Votre œuvre « " + nomProduit(type) + " » a été publiée avec succès !\n"
                            + "Elle est maintenant visible sur la marketplace.");
            lblResult.setStyle("-fx-text-fill: #2d6a4f; -fx-font-size: 13px;");
            btnPublish.setDisable(true);

        } catch (Exception e) {
            showError("Erreur lors de la publication : " + e.getMessage());
        }
    }

    @FXML
    public void handleReset() {
        tfPrice.clear();
        tfAvailableQty.clear();
        tfArtworkId.clear();
        cbProductType.setValue("original");
        chkUnlimited.setSelected(false);
        tfAvailableQty.setDisable(false);
        resultBox.setVisible(false);
        btnPublish.setDisable(false);
    }

    @FXML
    public void handleRetourMarket() {
        UserShellController shell = UserShellController.getInstance();
        if (shell != null) shell.showMarketplace();
    }

    // ─── Validation ──────────────────────────────────────────────────────────────

    private boolean valider() {
        StringBuilder err = new StringBuilder();

        try {
            double p = Double.parseDouble(tfPrice.getText().trim());
            if (p <= 0) err.append("• Le prix doit être supérieur à 0.\n");
        } catch (Exception e) { err.append("• Prix invalide (ex: 120.00).\n"); }

        if (!chkUnlimited.isSelected()) {
            try {
                int q = Integer.parseInt(tfAvailableQty.getText().trim());
                if (q <= 0) err.append("• La quantité doit être supérieure à 0.\n");
            } catch (Exception e) { err.append("• Quantité disponible invalide.\n"); }
        }

        try {
            Integer.parseInt(tfArtworkId.getText().trim());
        } catch (Exception e) { err.append("• ID de l'œuvre (Artwork ID) invalide.\n"); }

        if (cbProductType.getValue() == null) err.append("• Sélectionnez un type de produit.\n");

        if (!err.isEmpty()) {
            showError(err.toString());
            return false;
        }
        return true;
    }

    private void showError(String msg) {
        resultBox.setVisible(true);
        lblResult.setText("⚠  " + msg);
        lblResult.setStyle("-fx-text-fill: #c0392b; -fx-font-size: 12px;");
    }

    private String nomProduit(String type) {
        return switch (type.toLowerCase()) {
            case "original"    -> "Œuvre originale";
            case "print"       -> "Impression artistique";
            case "digital"     -> "Art numérique";
            case "painting"    -> "Peinture";
            case "sculpture"   -> "Sculpture";
            case "photography" -> "Photographie";
            case "drawing"     -> "Dessin";
            default            -> type.substring(0, 1).toUpperCase() + type.substring(1);
        };
    }
}
