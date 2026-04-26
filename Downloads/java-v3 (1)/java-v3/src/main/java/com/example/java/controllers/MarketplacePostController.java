package com.example.java.controllers;

import com.example.java.entities.MarketplacePost;
import com.example.java.services.MarketplacePostService;
import javafx.collections.FXCollections;
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

public class MarketplacePostController {

    // ─── Vue Liste (cartes) ───────────────────────────────────────────────────────
    @FXML private VBox cardContainer;

    // ─── Vue Formulaire ──────────────────────────────────────────────────────────
    @FXML private TextField        tfPrice;
    @FXML private ComboBox<String> cbStatus;
    @FXML private TextField        tfAvailableQty;
    @FXML private TextField        tfSoldQty;
    @FXML private ComboBox<String> cbProductType;
    @FXML private CheckBox         chkUnlimitedStock;
    @FXML private TextField        tfArtworkId;
    @FXML private TextField        tfSellerId;
    @FXML private Label            lblTitle;

    private final MarketplacePostService service = new MarketplacePostService();
    private MarketplacePost selectedPost              = null;
    private MarketplacePost postEnCoursDeModification = null;

    // ─── STYLE COMMUN ────────────────────────────────────────────────────────────
    private static final String CARD_STYLE =
        "-fx-background-color: white;" +
        "-fx-border-color: #d4cfc7;" +
        "-fx-border-width: 1;" +
        "-fx-border-radius: 10;" +
        "-fx-background-radius: 10;" +
        "-fx-effect: dropshadow(gaussian, rgba(74,55,40,0.06), 6, 0, 0, 2);" +
        "-fx-cursor: hand;";

    private static final String CARD_SELECTED_STYLE =
        "-fx-background-color: #fdf9f5;" +
        "-fx-border-color: #4a3728;" +
        "-fx-border-width: 2;" +
        "-fx-border-radius: 10;" +
        "-fx-background-radius: 10;" +
        "-fx-effect: dropshadow(gaussian, rgba(74,55,40,0.14), 10, 0, 0, 3);" +
        "-fx-cursor: hand;";

    // ─── INITIALISATION ──────────────────────────────────────────────────────────

    @FXML
    public void initialize() {
        if (cardContainer != null) chargerDonnees();
        if (cbStatus != null)
            cbStatus.setItems(FXCollections.observableArrayList("active", "inactive", "sold"));
        if (cbProductType != null)
            cbProductType.setItems(FXCollections.observableArrayList(
                "original","print","digital","painting","sculpture","photography","drawing","other"));
    }

    // ─── CHARGER EN CARTES ────────────────────────────────────────────────────────

    @FXML
    public void chargerDonnees() {
        List<MarketplacePost> liste = service.afficherAll();
        cardContainer.getChildren().clear();
        selectedPost = null;

        if (liste.isEmpty()) {
            Label vide = new Label("Aucune publication trouvée.");
            vide.setStyle("-fx-text-fill: #7a6a5a; -fx-font-size: 14px;");
            cardContainer.getChildren().add(vide);
            return;
        }
        for (MarketplacePost p : liste) cardContainer.getChildren().add(buildCard(p));
    }

    // ─── CONSTRUIRE UNE CARTE ─────────────────────────────────────────────────────

    private HBox buildCard(MarketplacePost post) {
        HBox card = new HBox(16);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(16, 20, 16, 20));
        card.setStyle(CARD_STYLE);

        // Badge type produit
        Label badge = new Label(post.getProductType().toUpperCase());
        badge.setMinWidth(76);
        badge.setAlignment(Pos.CENTER);
        badge.setStyle(
            "-fx-background-color: #e8e4dc; -fx-background-radius: 20;" +
            "-fx-text-fill: #4a3728; -fx-font-size: 10px; -fx-font-weight: bold;" +
            "-fx-padding: 5 10 5 10;");

        // Infos
        VBox infos = new VBox(5);
        HBox.setHgrow(infos, Priority.ALWAYS);

        // Ligne 1 — prix + statut + stock
        HBox row1 = new HBox(12);
        row1.setAlignment(Pos.CENTER_LEFT);

        Label lblPrix = new Label(String.format("%.2f €", post.getPrice()));
        lblPrix.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #4a3728;");

        Label lblStat = statusBadge(post.getStatus());

        String stockTxt = post.isUnlimitedStock()
            ? "∞  stock illimité"
            : post.getAvailableQuantity() + " disponible(s)  ·  " + post.getSoldQuantity() + " vendu(s)";
        Label lblStock = new Label("📦  " + stockTxt);
        lblStock.setStyle("-fx-font-size: 12px; -fx-text-fill: #7a6a5a;");

        row1.getChildren().addAll(lblPrix, lblStat, lblStock);

        // Ligne 2 — date
        String date = post.getCreatedAt() != null
            ? "Mis en vente le  " + post.getCreatedAt().toLocalDate() : "Date inconnue";
        Label lblDate = new Label(date);
        lblDate.setStyle("-fx-font-size: 12px; -fx-text-fill: #9a8a7a;");

        infos.getChildren().addAll(row1, lblDate);

        // Boutons inline
        HBox btns = new HBox(8);
        btns.setAlignment(Pos.CENTER_RIGHT);

        Button btnEdit = btnEdit("✏️  Modifier");
        btnEdit.setOnAction(e -> ouvrirFormModification(post));

        Button btnDel = btnDel("🗑️");
        btnDel.setOnAction(e -> supprimerPost(post));

        btns.getChildren().addAll(btnEdit, btnDel);

        // Sélection au clic
        card.setOnMouseClicked(e -> selectCard(card, post));

        card.getChildren().addAll(badge, infos, btns);
        return card;
    }

    private void selectCard(HBox clicked, MarketplacePost post) {
        // Réinitialiser toutes les cartes
        cardContainer.getChildren().forEach(n -> n.setStyle(CARD_STYLE));
        // Surligner la carte cliquée
        clicked.setStyle(CARD_SELECTED_STYLE);
        selectedPost = post;
    }

    // ─── BADGES ET BOUTONS RÉUTILISABLES ─────────────────────────────────────────

    private Label statusBadge(String status) {
        Label l = new Label(status);
        String s = "-fx-background-radius: 20; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 10 3 10;";
        s += switch (status.toLowerCase()) {
            case "active"   -> "-fx-background-color: #d8f3dc; -fx-text-fill: #2d6a4f;";
            case "sold"     -> "-fx-background-color: #faeeda; -fx-text-fill: #854f0b;";
            default         -> "-fx-background-color: #e8e4dc; -fx-text-fill: #5f5e5a;";
        };
        l.setStyle(s);
        return l;
    }

    private Button btnEdit(String txt) {
        Button b = new Button(txt);
        b.setStyle("-fx-background-color: transparent; -fx-border-color: #d4cfc7;" +
            "-fx-border-radius: 7; -fx-background-radius: 7; -fx-border-width: 1;" +
            "-fx-text-fill: #6b4f3a; -fx-font-size: 12px; -fx-padding: 5 12 5 12; -fx-cursor: hand;");
        return b;
    }

    private Button btnDel(String txt) {
        Button b = new Button(txt);
        b.setStyle("-fx-background-color: transparent; -fx-border-color: #f0b0b0;" +
            "-fx-border-radius: 7; -fx-background-radius: 7; -fx-border-width: 1;" +
            "-fx-text-fill: #c0392b; -fx-font-size: 12px; -fx-padding: 5 10 5 10; -fx-cursor: hand;");
        return b;
    }

    // ─── ACTIONS TOOLBAR ─────────────────────────────────────────────────────────

    @FXML public void handleAjouter()   { ouvrirFormAjout(); }

    @FXML public void handleModifier() {
        if (selectedPost == null) { alerte(Alert.AlertType.WARNING,
            "Cliquez d'abord sur une carte pour la sélectionner."); return; }
        ouvrirFormModification(selectedPost);
    }

    @FXML public void handleSupprimer() {
        if (selectedPost == null) { alerte(Alert.AlertType.WARNING,
            "Cliquez d'abord sur une carte pour la sélectionner."); return; }
        supprimerPost(selectedPost);
    }

    // ─── FORMULAIRE ──────────────────────────────────────────────────────────────

    private void ouvrirFormAjout() {
        try {
            FXMLLoader l = new FXMLLoader(getClass()
                .getResource("/com/example/java/marketplace-post-form.fxml"));
            Parent root = l.load();
            MarketplacePostController ctrl = l.getController();
            ctrl.preparerPourAjout();
            Stage s = new Stage(); s.setTitle("Ajouter une publication");
            s.setScene(new Scene(root)); s.showAndWait(); chargerDonnees();
        } catch (IOException ex) { alerte(Alert.AlertType.ERROR, ex.getMessage()); }
    }

    private void ouvrirFormModification(MarketplacePost post) {
        try {
            FXMLLoader l = new FXMLLoader(getClass()
                .getResource("/com/example/java/marketplace-post-form.fxml"));
            Parent root = l.load();
            MarketplacePostController ctrl = l.getController();
            ctrl.preparerPourModification(post);
            Stage s = new Stage(); s.setTitle("Modifier — " + post.getProductType());
            s.setScene(new Scene(root)); s.showAndWait(); chargerDonnees();
        } catch (IOException ex) { alerte(Alert.AlertType.ERROR, ex.getMessage()); }
    }

    private void supprimerPost(MarketplacePost post) {
        Optional<ButtonType> r = new Alert(Alert.AlertType.CONFIRMATION,
            "Supprimer « " + post.getProductType() + " — " +
            String.format("%.2f €", post.getPrice()) + " » ?",
            ButtonType.YES, ButtonType.NO).showAndWait();
        if (r.isPresent() && r.get() == ButtonType.YES) {
            service.supprimer(post.getId());
            selectedPost = null;
            chargerDonnees();
        }
    }

    // ─── FORMULAIRE : préparer ────────────────────────────────────────────────────

    public void preparerPourAjout() {
        postEnCoursDeModification = null;
        if (lblTitle != null) lblTitle.setText("Nouvelle publication");
    }

    public void preparerPourModification(MarketplacePost mp) {
        postEnCoursDeModification = mp;
        if (lblTitle != null) lblTitle.setText("Modifier la publication");
        tfPrice.setText(String.valueOf(mp.getPrice()));
        cbStatus.setValue(mp.getStatus());
        tfAvailableQty.setText(String.valueOf(mp.getAvailableQuantity()));
        tfSoldQty.setText(String.valueOf(mp.getSoldQuantity()));
        cbProductType.setValue(mp.getProductType());
        chkUnlimitedStock.setSelected(mp.isUnlimitedStock());
        tfArtworkId.setText(String.valueOf(mp.getArtworkId()));
        tfSellerId.setText(String.valueOf(mp.getSellerId()));
    }

    @FXML
    public void handleSauvegarder() {
        if (!validerFormulaire()) return;
        double price       = Double.parseDouble(tfPrice.getText().trim());
        String status      = cbStatus.getValue();
        int    availQty    = Integer.parseInt(tfAvailableQty.getText().trim());
        int    soldQty     = Integer.parseInt(tfSoldQty.getText().trim());
        String type        = cbProductType.getValue();
        boolean unlimited  = chkUnlimitedStock.isSelected();
        int    artworkId   = Integer.parseInt(tfArtworkId.getText().trim());
        int    sellerId    = Integer.parseInt(tfSellerId.getText().trim());

        if (postEnCoursDeModification == null) {
            service.ajouter(new MarketplacePost(price, status, availQty, soldQty,
                type, unlimited, LocalDateTime.now(), null, artworkId, sellerId));
            alerte(Alert.AlertType.INFORMATION, "Publication ajoutée !");
        } else {
            postEnCoursDeModification.setPrice(price);
            postEnCoursDeModification.setStatus(status);
            postEnCoursDeModification.setAvailableQuantity(availQty);
            postEnCoursDeModification.setSoldQuantity(soldQty);
            postEnCoursDeModification.setProductType(type);
            postEnCoursDeModification.setUnlimitedStock(unlimited);
            postEnCoursDeModification.setArtworkId(artworkId);
            postEnCoursDeModification.setSellerId(sellerId);
            service.modifier(postEnCoursDeModification);
            alerte(Alert.AlertType.INFORMATION, "Publication modifiée !");
        }
        fermerFenetre();
    }

    @FXML public void handleAnnuler() { fermerFenetre(); }

    private boolean validerFormulaire() {
        StringBuilder e = new StringBuilder();
        try { Double.parseDouble(tfPrice.getText().trim()); }
        catch (Exception ex) { e.append("- Prix invalide.\n"); }
        if (cbStatus.getValue() == null)      e.append("- Statut requis.\n");
        if (cbProductType.getValue() == null) e.append("- Type requis.\n");
        try { Integer.parseInt(tfAvailableQty.getText().trim()); }
        catch (Exception ex) { e.append("- Qté disponible invalide.\n"); }
        try { Integer.parseInt(tfArtworkId.getText().trim()); }
        catch (Exception ex) { e.append("- ID Artwork invalide.\n"); }
        try { Integer.parseInt(tfSellerId.getText().trim()); }
        catch (Exception ex) { e.append("- ID Vendeur invalide.\n"); }
        if (!e.isEmpty()) { alerte(Alert.AlertType.ERROR, e.toString()); return false; }
        return true;
    }

    private void fermerFenetre() {
        ((Stage) tfPrice.getScene().getWindow()).close();
    }

    private void alerte(Alert.AlertType t, String msg) {
        new Alert(t, msg, ButtonType.OK).showAndWait();
    }
}
