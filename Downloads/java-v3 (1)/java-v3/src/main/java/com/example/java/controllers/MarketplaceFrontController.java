package com.example.java.controllers;

import com.example.java.entities.MarketplacePost;
import com.example.java.services.CartService;
import com.example.java.services.MarketplacePostService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Vitrine Marketplace côté utilisateur.
 * Affiche des cartes produits lisibles, SANS IDs bruts.
 * Scénario Symfony : /marketplace/front
 */
public class MarketplaceFrontController implements Initializable {

    @FXML private TextField        searchField;
    @FXML private ComboBox<String> filterType;
    @FXML private Button           btnSearch;
    @FXML private FlowPane         productGrid;
    @FXML private Label            resultCount;

    private final MarketplacePostService postService = new MarketplacePostService();
    private List<MarketplacePost> allPosts;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filterType.getItems().addAll(
            "Tous les types", "original", "print", "digital",
            "painting", "sculpture", "photography", "drawing", "other"
        );
        filterType.setValue("Tous les types");
        chargerPosts();
    }

    // ─── Chargement & filtrage ────────────────────────────────────────────────────

    @FXML
    public void chargerPosts() {
        try {
            allPosts = postService.afficherAll().stream()
                .filter(p -> "active".equalsIgnoreCase(p.getStatus()))
                .collect(Collectors.toList());
            appliquerFiltres();
        } catch (Exception e) {
            showError("Impossible de charger les produits : " + e.getMessage());
        }
    }

    @FXML
    public void appliquerFiltres() {
        if (allPosts == null) return;
        String recherche = searchField.getText() == null ? ""
            : searchField.getText().trim().toLowerCase();
        String type = filterType.getValue();

        List<MarketplacePost> filtered = allPosts.stream()
            .filter(p -> {
                boolean matchSearch = recherche.isEmpty()
                    || nomProduit(p.getProductType()).toLowerCase().contains(recherche)
                    || p.getProductType().toLowerCase().contains(recherche);
                boolean matchType = "Tous les types".equals(type)
                    || p.getProductType().equalsIgnoreCase(type);
                return matchSearch && matchType;
            })
            .collect(Collectors.toList());

        afficherCartes(filtered);
        resultCount.setText(filtered.size() + " œuvre(s) disponible(s)");
    }

    // ─── Rendu des cartes (sans IDs) ─────────────────────────────────────────────

    private void afficherCartes(List<MarketplacePost> posts) {
        productGrid.getChildren().clear();

        if (posts.isEmpty()) {
            Label empty = new Label("Aucune œuvre disponible pour ces critères.");
            empty.setStyle("-fx-text-fill: #7a6a5a; -fx-font-size: 14px;");
            productGrid.getChildren().add(empty);
            return;
        }
        for (MarketplacePost p : posts) productGrid.getChildren().add(buildCard(p));
    }

    private VBox buildCard(MarketplacePost post) {
        VBox card = new VBox(12);
        card.getStyleClass().add("u-product-card");
        card.setPrefWidth(230);
        card.setPadding(new Insets(18));

        // Badge type (lisible, pas en majuscules abruptes)
        Label badge = new Label(nomProduit(post.getProductType()));
        badge.getStyleClass().add("u-badge");

        // Titre humain — type de produit formaté, pas "Artwork #5"
        Label titre = new Label(nomProduit(post.getProductType()));
        titre.getStyleClass().add("u-card-title");
        titre.setWrapText(true);

        // Sous-titre discret — catégorie
        Label categorie = new Label(post.getProductType().toLowerCase());
        categorie.setStyle("-fx-font-size: 11px; -fx-text-fill: #9a8a7a;");

        // Prix
        Label prix = new Label(String.format("%.2f €", post.getPrice()));
        prix.getStyleClass().add("u-card-price");

        // Stock (sans ID)
        String stockTxt = post.isUnlimitedStock()
            ? "∞  Stock illimité"
            : post.getAvailableQuantity() + " disponibles";
        Label stock = new Label(stockTxt);
        stock.getStyleClass().add("u-card-stock");

        // Statut disponibilité (sans "Vendeur #X")
        Label dispo = new Label("✅  Disponible à la vente");
        dispo.setStyle("-fx-font-size: 11px; -fx-text-fill: #2d6a4f;");

        // Boutons
        HBox btns = new HBox(8);
        btns.setAlignment(Pos.CENTER_LEFT);

        Button btnDetail = new Button("Voir le détail");
        btnDetail.getStyleClass().add("u-btn-outline");
        btnDetail.setOnAction(e -> {
            UserShellController shell = UserShellController.getInstance();
            if (shell != null) shell.showProductDetail(post.getId());
        });

        Button btnCart = new Button("🛒  Ajouter");
        btnCart.getStyleClass().add("u-btn-primary");
        btnCart.setOnAction(e -> {
            CartService.getInstance().addPost(post, 1);
            UserShellController shell = UserShellController.getInstance();
            if (shell != null) shell.refreshCartBadge();
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "« " + nomProduit(post.getProductType()) + " » ajouté au panier !",
                ButtonType.OK);
            alert.setHeaderText(null);
            alert.setTitle("Panier mis à jour");
            alert.showAndWait();
        });

        btns.getChildren().addAll(btnDetail, btnCart);
        card.getChildren().addAll(badge, titre, categorie, prix, stock, dispo, btns);
        return card;
    }

    // ─── Nom lisible du type (sans ID, sans tout-caps) ────────────────────────────

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

    // ─── Erreur ───────────────────────────────────────────────────────────────────

    private void showError(String msg) {
        productGrid.getChildren().clear();
        Label err = new Label("⚠  " + msg);
        err.setStyle("-fx-text-fill: #c0392b; -fx-font-size: 13px;");
        productGrid.getChildren().add(err);
    }
}
