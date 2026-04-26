package com.example.java.controllers;

import com.example.java.entities.Ordre;
import com.example.java.services.LigneOrdreService;
import com.example.java.services.OrdreService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class OrdreController {

    // ─── Vue Liste (cartes) ───────────────────────────────────────────────────────
    @FXML private VBox cardContainer;

    // ─── Vue Formulaire ──────────────────────────────────────────────────────────
    @FXML private TextField tfTotalAmount;
    @FXML private TextField tfBuyerId;
    @FXML private Label     lblTitle;

    private final OrdreService      service       = new OrdreService();
    private final LigneOrdreService ligneService  = new LigneOrdreService();
    private Ordre selectedOrdre              = null;
    private Ordre ordreEnCoursDeModification = null;

    private static final String CARD_STYLE =
        "-fx-background-color: white; -fx-border-color: #d4cfc7; -fx-border-width: 1;" +
        "-fx-border-radius: 10; -fx-background-radius: 10;" +
        "-fx-effect: dropshadow(gaussian, rgba(74,55,40,0.06), 6, 0, 0, 2); -fx-cursor: hand;";

    private static final String CARD_SELECTED =
        "-fx-background-color: #fdf9f5; -fx-border-color: #4a3728; -fx-border-width: 2;" +
        "-fx-border-radius: 10; -fx-background-radius: 10;" +
        "-fx-effect: dropshadow(gaussian, rgba(74,55,40,0.14), 10, 0, 0, 3); -fx-cursor: hand;";

    // ─── INITIALISATION ──────────────────────────────────────────────────────────

    @FXML
    public void initialize() {
        if (cardContainer != null) chargerDonnees();
    }

    // ─── CHARGER EN CARTES ────────────────────────────────────────────────────────

    @FXML
    public void chargerDonnees() {
        List<Ordre> liste = service.afficherAll();
        cardContainer.getChildren().clear();
        selectedOrdre = null;

        if (liste.isEmpty()) {
            Label vide = new Label("Aucune commande trouvée.");
            vide.setStyle("-fx-text-fill: #7a6a5a; -fx-font-size: 14px;");
            cardContainer.getChildren().add(vide);
            return;
        }
        for (Ordre o : liste) cardContainer.getChildren().add(buildCard(o));
    }

    private HBox buildCard(Ordre ordre) {
        HBox card = new HBox(16);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(16, 20, 16, 20));
        card.setStyle(CARD_STYLE);

        // Icône
        Label icone = new Label("📦");
        icone.setStyle("-fx-font-size: 26px;");

        // Infos
        VBox infos = new VBox(5);
        HBox.setHgrow(infos, Priority.ALWAYS);

        // Ligne 1 — numéro + montant
        HBox row1 = new HBox(14);
        row1.setAlignment(Pos.CENTER_LEFT);

        Label lblNum = new Label("Commande  #" + ordre.getId());
        lblNum.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #2c2218;");

        Label lblMontant = new Label(String.format("%.2f €", ordre.getTotalAmount()));
        lblMontant.setStyle(
            "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4a3728;" +
            "-fx-font-family: Georgia, serif;");

        // Nombre de lignes
        int nbLignes = ligneService.afficherParOrdre(ordre.getId()).size();
        Label lblLignes = new Label(nbLignes + " article(s)");
        lblLignes.setStyle("-fx-font-size: 12px; -fx-text-fill: #7a6a5a;" +
            "-fx-background-color: #e8e4dc; -fx-background-radius: 20; -fx-padding: 3 10 3 10;");

        row1.getChildren().addAll(lblNum, lblMontant, lblLignes);

        // Ligne 2 — date + acheteur
        String date = ordre.getCreatedAt() != null
            ? ordre.getCreatedAt().toLocalDate().toString() : "—";
        Label lblDate = new Label("📅  " + date + "   ·   Acheteur #" + ordre.getBuyerId());
        lblDate.setStyle("-fx-font-size: 12px; -fx-text-fill: #9a8a7a;");

        infos.getChildren().addAll(row1, lblDate);

        // Boutons inline
        HBox btns = new HBox(8);
        btns.setAlignment(Pos.CENTER_RIGHT);

        Button btnEdit = mkBtn("✏️  Modifier", false);
        btnEdit.setOnAction(e -> ouvrirFormModification(ordre));

        Button btnDel = mkBtn("🗑️", true);
        btnDel.setOnAction(e -> supprimerOrdre(ordre));

        btns.getChildren().addAll(btnEdit, btnDel);

        card.setOnMouseClicked(e -> {
            cardContainer.getChildren().forEach(n -> n.setStyle(CARD_STYLE));
            card.setStyle(CARD_SELECTED);
            selectedOrdre = ordre;
        });

        card.getChildren().addAll(icone, infos, btns);
        return card;
    }

    // ─── ACTIONS TOOLBAR ─────────────────────────────────────────────────────────

    @FXML public void handleAjouter()   { ouvrirFormAjout(); }

    @FXML public void handleModifier() {
        if (selectedOrdre == null) {
            alerte(Alert.AlertType.WARNING, "Cliquez d'abord sur une carte."); return;
        }
        ouvrirFormModification(selectedOrdre);
    }

    @FXML public void handleSupprimer() {
        if (selectedOrdre == null) {
            alerte(Alert.AlertType.WARNING, "Cliquez d'abord sur une carte."); return;
        }
        supprimerOrdre(selectedOrdre);
    }

    // ─── FORMULAIRE ──────────────────────────────────────────────────────────────

    private void ouvrirFormAjout() {
        try {
            FXMLLoader l = new FXMLLoader(
                getClass().getResource("/com/example/java/ordre-form.fxml"));
            Parent root = l.load();
            OrdreController ctrl = l.getController();
            ctrl.preparerPourAjout();
            Stage s = new Stage(); s.setTitle("Ajouter une commande");
            s.setScene(new Scene(root)); s.showAndWait(); chargerDonnees();
        } catch (IOException ex) { alerte(Alert.AlertType.ERROR, ex.getMessage()); }
    }

    private void ouvrirFormModification(Ordre ordre) {
        try {
            FXMLLoader l = new FXMLLoader(
                getClass().getResource("/com/example/java/ordre-form.fxml"));
            Parent root = l.load();
            OrdreController ctrl = l.getController();
            ctrl.preparerPourModification(ordre);
            Stage s = new Stage(); s.setTitle("Modifier la commande #" + ordre.getId());
            s.setScene(new Scene(root)); s.showAndWait(); chargerDonnees();
        } catch (IOException ex) { alerte(Alert.AlertType.ERROR, ex.getMessage()); }
    }

    private void supprimerOrdre(Ordre ordre) {
        Optional<ButtonType> r = new Alert(Alert.AlertType.CONFIRMATION,
            "Supprimer la commande #" + ordre.getId() + " ?",
            ButtonType.YES, ButtonType.NO).showAndWait();
        if (r.isPresent() && r.get() == ButtonType.YES) {
            service.supprimer(ordre.getId());
            selectedOrdre = null;
            chargerDonnees();
        }
    }

    public void preparerPourAjout() {
        ordreEnCoursDeModification = null;
        if (lblTitle != null) lblTitle.setText("Nouvelle commande");
    }

    public void preparerPourModification(Ordre ordre) {
        ordreEnCoursDeModification = ordre;
        if (lblTitle != null) lblTitle.setText("Modifier la commande #" + ordre.getId());
        tfTotalAmount.setText(String.valueOf(ordre.getTotalAmount()));
        tfBuyerId.setText(String.valueOf(ordre.getBuyerId()));
    }

    @FXML
    public void handleSauvegarder() {
        if (!valider()) return;
        double total   = Double.parseDouble(tfTotalAmount.getText().trim());
        int    buyerId = Integer.parseInt(tfBuyerId.getText().trim());
        if (ordreEnCoursDeModification == null) {
            service.ajouter(new Ordre(total, LocalDateTime.now(), buyerId));
            alerte(Alert.AlertType.INFORMATION, "Commande ajoutée !");
        } else {
            ordreEnCoursDeModification.setTotalAmount(total);
            ordreEnCoursDeModification.setBuyerId(buyerId);
            service.modifier(ordreEnCoursDeModification);
            alerte(Alert.AlertType.INFORMATION, "Commande modifiée !");
        }
        fermerFenetre();
    }

    @FXML public void handleAnnuler() { fermerFenetre(); }

    private boolean valider() {
        try { Double.parseDouble(tfTotalAmount.getText().trim()); }
        catch (Exception e) { alerte(Alert.AlertType.ERROR, "Montant invalide."); return false; }
        try { Integer.parseInt(tfBuyerId.getText().trim()); }
        catch (Exception e) { alerte(Alert.AlertType.ERROR, "ID acheteur invalide."); return false; }
        return true;
    }

    private void fermerFenetre() {
        ((Stage) tfTotalAmount.getScene().getWindow()).close();
    }

    private Button mkBtn(String txt, boolean danger) {
        Button b = new Button(txt);
        b.setStyle(danger
            ? "-fx-background-color: transparent; -fx-border-color: #f0b0b0;" +
              "-fx-border-radius: 7; -fx-background-radius: 7; -fx-border-width: 1;" +
              "-fx-text-fill: #c0392b; -fx-font-size: 12px; -fx-padding: 5 10 5 10; -fx-cursor: hand;"
            : "-fx-background-color: transparent; -fx-border-color: #d4cfc7;" +
              "-fx-border-radius: 7; -fx-background-radius: 7; -fx-border-width: 1;" +
              "-fx-text-fill: #6b4f3a; -fx-font-size: 12px; -fx-padding: 5 12 5 12; -fx-cursor: hand;");
        return b;
    }

    private void alerte(Alert.AlertType t, String msg) {
        new Alert(t, msg, ButtonType.OK).showAndWait();
    }
}
