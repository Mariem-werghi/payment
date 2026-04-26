package com.example.java.controllers;

import com.example.java.entities.LigneOrdre;
import com.example.java.entities.MarketplacePost;
import com.example.java.entities.Ordre;
import com.example.java.services.LigneOrdreService;
import com.example.java.services.MarketplacePostService;
import com.example.java.services.OrdreService;
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
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class LigneOrdreController {

    // ─── Vue Liste (cartes) ───────────────────────────────────────────────────────
    @FXML private VBox cardContainer;

    // ─── Vue Formulaire ──────────────────────────────────────────────────────────
    @FXML private ComboBox<Ordre>          cbOrdreId;
    @FXML private ComboBox<MarketplacePost> cbMarketplacePostId;
    @FXML private TextField                tfPriceAtPurchase;
    @FXML private TextField                tfQuantity;
    @FXML private Label                    lblTitle;

    private final LigneOrdreService      service      = new LigneOrdreService();
    private final OrdreService           ordreService = new OrdreService();
    private final MarketplacePostService postService  = new MarketplacePostService();

    private LigneOrdre selectedLigne              = null;
    private LigneOrdre ligneEnCoursDeModification = null;

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
        if (cardContainer != null) {
            chargerDonnees();
        }
        // Formulaire — remplir les ComboBoxes
        if (cbOrdreId != null) {
            configurerComboOrdre();
        }
        if (cbMarketplacePostId != null) {
            configurerComboPost();
        }
        // Auto-remplir le prix quand un post est sélectionné
        if (cbMarketplacePostId != null && tfPriceAtPurchase != null) {
            cbMarketplacePostId.valueProperty().addListener((obs, old, post) -> {
                if (post != null) {
                    tfPriceAtPurchase.setText(String.format("%.2f", post.getPrice()));
                }
            });
        }
    }

    // ─── COMBO ORDRE ─────────────────────────────────────────────────────────────

    private void configurerComboOrdre() {
        List<Ordre> ordres = ordreService.afficherAll();
        cbOrdreId.setItems(FXCollections.observableArrayList(ordres));
        cbOrdreId.setConverter(new StringConverter<>() {
            @Override public String toString(Ordre o) {
                if (o == null) return "";
                return "Commande #" + o.getId() + "  —  " +
                       String.format("%.2f €", o.getTotalAmount()) +
                       "  (acheteur #" + o.getBuyerId() + ")";
            }
            @Override public Ordre fromString(String s) { return null; }
        });
    }

    // ─── COMBO MARKETPLACE POST ───────────────────────────────────────────────────

    private void configurerComboPost() {
        // Afficher tous les posts (actifs et inactifs pour l'admin)
        List<MarketplacePost> posts = postService.afficherAll();
        cbMarketplacePostId.setItems(FXCollections.observableArrayList(posts));
        cbMarketplacePostId.setConverter(new StringConverter<>() {
            @Override public String toString(MarketplacePost p) {
                if (p == null) return "";
                return p.getProductType().toUpperCase() + "  —  " +
                       String.format("%.2f €", p.getPrice()) +
                       "  (" + (p.isUnlimitedStock() ? "∞ stock" :
                           p.getAvailableQuantity() + " dispo") + ")";
            }
            @Override public MarketplacePost fromString(String s) { return null; }
        });
    }

    // ─── CHARGER EN CARTES ────────────────────────────────────────────────────────

    @FXML
    public void chargerDonnees() {
        List<LigneOrdre> liste = service.afficherAll();
        cardContainer.getChildren().clear();
        selectedLigne = null;

        if (liste.isEmpty()) {
            Label vide = new Label("Aucune ligne d'ordre trouvée.");
            vide.setStyle("-fx-text-fill: #7a6a5a; -fx-font-size: 14px;");
            cardContainer.getChildren().add(vide);
            return;
        }
        for (LigneOrdre lo : liste) cardContainer.getChildren().add(buildCard(lo));
    }

    private HBox buildCard(LigneOrdre lo) {
        HBox card = new HBox(16);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(16, 20, 16, 20));
        card.setStyle(CARD_STYLE);

        // Icône
        Label icone = new Label("📋");
        icone.setStyle("-fx-font-size: 24px;");

        // Infos
        VBox infos = new VBox(5);
        HBox.setHgrow(infos, Priority.ALWAYS);

        // Ligne 1 — quantité × prix = sous-total
        double sousTotal = lo.getQuantity() * lo.getPriceAtPurchase();
        Label lblCalc = new Label(
            lo.getQuantity() + "  ×  " +
            String.format("%.2f €", lo.getPriceAtPurchase()) + "  =  " +
            String.format("%.2f €", sousTotal));
        lblCalc.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #4a3728;");

        // Ligne 2 — liens vers commande et post
        // Récupérer les labels depuis les services
        String ordreLabel = "Commande #" + lo.getOrdreId();
        String postLabel  = "Publication #" + lo.getMarketplacePostId();
        try {
            Ordre o = ordreService.trouverParId(lo.getOrdreId());
            if (o != null) ordreLabel = "Commande #" + o.getId() +
                " (" + String.format("%.0f €", o.getTotalAmount()) + ")";
        } catch (Exception ignored) {}
        try {
            MarketplacePost p = postService.trouverParId(lo.getMarketplacePostId());
            if (p != null) postLabel = p.getProductType().toUpperCase() +
                " — " + String.format("%.2f €", p.getPrice());
        } catch (Exception ignored) {}

        Label lblLinks = new Label("📦  " + ordreLabel + "   ·   🛒  " + postLabel);
        lblLinks.setStyle("-fx-font-size: 12px; -fx-text-fill: #7a6a5a;");

        infos.getChildren().addAll(lblCalc, lblLinks);

        // Boutons
        HBox btns = new HBox(8);
        btns.setAlignment(Pos.CENTER_RIGHT);

        Button btnEdit = mkBtn("✏️  Modifier", false);
        btnEdit.setOnAction(e -> ouvrirFormModification(lo));

        Button btnDel = mkBtn("🗑️", true);
        btnDel.setOnAction(e -> supprimerLigne(lo));

        btns.getChildren().addAll(btnEdit, btnDel);

        card.setOnMouseClicked(e -> {
            cardContainer.getChildren().forEach(n -> n.setStyle(CARD_STYLE));
            card.setStyle(CARD_SELECTED);
            selectedLigne = lo;
        });

        card.getChildren().addAll(icone, infos, btns);
        return card;
    }

    // ─── ACTIONS TOOLBAR ─────────────────────────────────────────────────────────

    @FXML public void handleAjouter() { ouvrirFormAjout(); }

    @FXML public void handleModifier() {
        if (selectedLigne == null) {
            alerte(Alert.AlertType.WARNING, "Cliquez d'abord sur une carte."); return;
        }
        ouvrirFormModification(selectedLigne);
    }

    @FXML public void handleSupprimer() {
        if (selectedLigne == null) {
            alerte(Alert.AlertType.WARNING, "Cliquez d'abord sur une carte."); return;
        }
        supprimerLigne(selectedLigne);
    }

    // ─── FORMULAIRE ──────────────────────────────────────────────────────────────

    private void ouvrirFormAjout() {
        try {
            FXMLLoader l = new FXMLLoader(
                getClass().getResource("/com/example/java/ligne-ordre-form.fxml"));
            Parent root = l.load();
            LigneOrdreController ctrl = l.getController();
            ctrl.preparerPourAjout();
            Stage s = new Stage(); s.setTitle("Ajouter une ligne d'ordre");
            s.setScene(new Scene(root)); s.showAndWait(); chargerDonnees();
        } catch (IOException ex) { alerte(Alert.AlertType.ERROR, ex.getMessage()); }
    }

    private void ouvrirFormModification(LigneOrdre lo) {
        try {
            FXMLLoader l = new FXMLLoader(
                getClass().getResource("/com/example/java/ligne-ordre-form.fxml"));
            Parent root = l.load();
            LigneOrdreController ctrl = l.getController();
            ctrl.preparerPourModification(lo);
            Stage s = new Stage(); s.setTitle("Modifier la ligne #" + lo.getId());
            s.setScene(new Scene(root)); s.showAndWait(); chargerDonnees();
        } catch (IOException ex) { alerte(Alert.AlertType.ERROR, ex.getMessage()); }
    }

    private void supprimerLigne(LigneOrdre lo) {
        Optional<ButtonType> r = new Alert(Alert.AlertType.CONFIRMATION,
            "Supprimer cette ligne d'ordre ?", ButtonType.YES, ButtonType.NO).showAndWait();
        if (r.isPresent() && r.get() == ButtonType.YES) {
            service.supprimer(lo.getId());
            selectedLigne = null;
            chargerDonnees();
        }
    }

    // ─── PRÉPARER FORMULAIRE ─────────────────────────────────────────────────────

    public void preparerPourAjout() {
        ligneEnCoursDeModification = null;
        if (lblTitle != null) lblTitle.setText("Nouvelle ligne d'ordre");
    }

    public void preparerPourModification(LigneOrdre lo) {
        ligneEnCoursDeModification = lo;
        if (lblTitle != null) lblTitle.setText("Modifier la ligne #" + lo.getId());
        tfPriceAtPurchase.setText(String.valueOf(lo.getPriceAtPurchase()));
        tfQuantity.setText(String.valueOf(lo.getQuantity()));

        // Sélectionner le bon Ordre dans le ComboBox
        cbOrdreId.getItems().stream()
            .filter(o -> o.getId() == lo.getOrdreId())
            .findFirst()
            .ifPresent(o -> cbOrdreId.setValue(o));

        // Sélectionner le bon Post dans le ComboBox
        cbMarketplacePostId.getItems().stream()
            .filter(p -> p.getId() == lo.getMarketplacePostId())
            .findFirst()
            .ifPresent(p -> cbMarketplacePostId.setValue(p));
    }

    // ─── SAUVEGARDER ─────────────────────────────────────────────────────────────

    @FXML
    public void handleSauvegarder() {
        if (!valider()) return;

        double price   = Double.parseDouble(tfPriceAtPurchase.getText().trim());
        int    qty     = Integer.parseInt(tfQuantity.getText().trim());
        int    ordreId = cbOrdreId.getValue().getId();
        int    postId  = cbMarketplacePostId.getValue().getId();

        if (ligneEnCoursDeModification == null) {
            service.ajouter(new LigneOrdre(price, qty, ordreId, postId));
            alerte(Alert.AlertType.INFORMATION, "Ligne ajoutée avec succès !");
        } else {
            ligneEnCoursDeModification.setPriceAtPurchase(price);
            ligneEnCoursDeModification.setQuantity(qty);
            ligneEnCoursDeModification.setOrdreId(ordreId);
            ligneEnCoursDeModification.setMarketplacePostId(postId);
            service.modifier(ligneEnCoursDeModification);
            alerte(Alert.AlertType.INFORMATION, "Ligne modifiée avec succès !");
        }
        fermerFenetre();
    }

    @FXML public void handleAnnuler() { fermerFenetre(); }

    // ─── VALIDATION ──────────────────────────────────────────────────────────────

    private boolean valider() {
        StringBuilder e = new StringBuilder();
        if (cbOrdreId.getValue() == null)
            e.append("- Sélectionnez une commande.\n");
        if (cbMarketplacePostId.getValue() == null)
            e.append("- Sélectionnez une publication.\n");
        try { double p = Double.parseDouble(tfPriceAtPurchase.getText().trim());
              if (p <= 0) e.append("- Le prix doit être > 0.\n");
        } catch (Exception ex) { e.append("- Prix invalide.\n"); }
        try { int q = Integer.parseInt(tfQuantity.getText().trim());
              if (q <= 0) e.append("- La quantité doit être > 0.\n");
        } catch (Exception ex) { e.append("- Quantité invalide.\n"); }
        if (!e.isEmpty()) { alerte(Alert.AlertType.ERROR, e.toString()); return false; }
        return true;
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────────

    private void fermerFenetre() {
        ((Stage) tfPriceAtPurchase.getScene().getWindow()).close();
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
