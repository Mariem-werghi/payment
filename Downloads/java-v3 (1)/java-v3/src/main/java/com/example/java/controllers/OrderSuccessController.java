package com.example.java.controllers;

import com.example.java.entities.LigneOrdre;
import com.example.java.entities.Ordre;
import com.example.java.services.LigneOrdreService;
import com.example.java.services.OrdreService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.awt.Desktop;
import java.io.File;
import java.util.List;

/**
 * Contrôleur de l'écran de confirmation de commande.
 * Affiche le récapitulatif et permet d'ouvrir/télécharger la facture PDF générée.
 */
public class OrderSuccessController {

    // ─── FXML ─────────────────────────────────────────────────────────────────────
    @FXML private Label  lblOrdreId;
    @FXML private Label  lblTotal;
    @FXML private Label  lblDate;
    @FXML private Label  lblNbLignes;
    @FXML private VBox   boxLignes;
    @FXML private Button btnRetour;
    @FXML private Button btnPdf;

    // ─── Services ─────────────────────────────────────────────────────────────────
    private final OrdreService      ordreService = new OrdreService();
    private final LigneOrdreService ligneService = new LigneOrdreService();

    /** Référence au fichier PDF généré pendant le checkout (peut être null) */
    private File pdfFile;

    // ─── Initialisation depuis UserShellController ────────────────────────────────

    /**
     * Appelé par UserShellController après le load FXML.
     * @param ordreId identifiant de la commande qui vient d'être créée
     */
    public void setOrdreId(int ordreId) {
        try {
            Ordre ordre = ordreService.trouverParId(ordreId);
            if (ordre == null) {
                lblOrdreId.setText("Commande #" + ordreId);
                lblTotal.setText("Montant non disponible");
                return;
            }

            lblOrdreId.setText("Commande  #" + ordre.getId());
            lblTotal.setText(String.format("Montant total : %.2f €", ordre.getTotalAmount()));
            lblDate.setText("Passée le : " +
                (ordre.getCreatedAt() != null
                    ? ordre.getCreatedAt().toLocalDate().toString()
                    : "—"));

            // Lignes de la commande
            List<LigneOrdre> lignes = ligneService.afficherParOrdre(ordreId);
            lblNbLignes.setText(lignes.size() + " article(s)");

            boxLignes.getChildren().clear();
            for (LigneOrdre l : lignes) {
                Label row = new Label(String.format(
                    "  • Post #%d  —  %d × %.2f €  =  %.2f €",
                    l.getMarketplacePostId(),
                    l.getQuantity(),
                    l.getPriceAtPurchase(),
                    l.getQuantity() * l.getPriceAtPurchase()
                ));
                row.setStyle("-fx-font-size:13px; -fx-text-fill:#4a3728;");
                boxLignes.getChildren().add(row);
            }

        } catch (Exception e) {
            lblOrdreId.setText("Commande #" + ordreId);
            lblTotal.setText("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reçoit le fichier PDF généré pendant le checkout.
     * Active le bouton de téléchargement si le fichier existe.
     */
    public void setPdfFile(File pdf) {
        this.pdfFile = pdf;
        if (btnPdf != null) {
            btnPdf.setDisable(pdf == null || !pdf.exists());
        }
    }

    // ─── Actions ──────────────────────────────────────────────────────────────────

    @FXML
    public void handleDownloadPdf() {
        if (pdfFile == null || !pdfFile.exists()) {
            new Alert(Alert.AlertType.WARNING, "Le fichier PDF n'est pas disponible.", ButtonType.OK)
                .showAndWait();
            return;
        }
        try {
            if (Desktop.isDesktopSupported()
                    && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                new Alert(Alert.AlertType.INFORMATION,
                    "Fichier disponible ici :\n" + pdfFile.getAbsolutePath(), ButtonType.OK)
                    .showAndWait();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Impossible d'ouvrir le PDF :\n" + e.getMessage(),
                ButtonType.OK).showAndWait();
        }
    }

    @FXML
    public void handleRetour() {
        UserShellController shell = UserShellController.getInstance();
        if (shell != null) shell.showMarketplace();
    }

    @FXML
    public void handleVoirCommandes() {
        UserShellController shell = UserShellController.getInstance();
        if (shell != null) shell.showOrders();
    }
}
