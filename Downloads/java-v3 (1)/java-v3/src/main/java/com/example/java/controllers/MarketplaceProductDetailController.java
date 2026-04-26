package com.example.java.controllers;

import com.example.java.entities.MarketplacePost;
import com.example.java.services.CartService;
import com.example.java.services.MarketplacePostService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Contrôleur de la fiche détail d'un produit (vue utilisateur).
 * Réplique le scénario Symfony : /marketplace/product/{id}
 */
public class MarketplaceProductDetailController {

    // ─── FXML ─────────────────────────────────────────────────────────────────────
    @FXML private Label   lblType;
    @FXML private Label   lblArtwork;
    @FXML private Label   lblPrice;
    @FXML private Label   lblStock;
    @FXML private Label   lblSeller;
    @FXML private Label   lblStatus;
    @FXML private Label   lblCreatedAt;
    @FXML private Spinner<Integer> spinQty;
    @FXML private Button  btnAddCart;
    @FXML private Button  btnBack;

    private final MarketplacePostService postService = new MarketplacePostService();
    private MarketplacePost currentPost;

    // ─── Chargement du produit ────────────────────────────────────────────────────

    /**
     * Appelé par UserShellController juste après le load du FXML.
     * @param postId identifiant du MarketplacePost à afficher
     */
    public void loadPost(int postId) {
        try {
            currentPost = postService.trouverParId(postId);
            if (currentPost == null) {
                showError("Produit introuvable (id=" + postId + ")");
                return;
            }
            afficherPost(currentPost);
        } catch (Exception e) {
            showError("Erreur chargement produit : " + e.getMessage());
        }
    }

    private void afficherPost(MarketplacePost p) {
        lblType.setText(p.getProductType().toUpperCase());
        lblArtwork.setText("Artwork  #" + p.getArtworkId());
        lblPrice.setText(String.format("%.2f €", p.getPrice()));
        lblSeller.setText("Vendu par : Vendeur #" + p.getSellerId());
        lblStatus.setText("Statut : " + p.getStatus());
        lblCreatedAt.setText("Mis en vente le : " +
            (p.getCreatedAt() != null ? p.getCreatedAt().toLocalDate() : "—"));

        if (p.isUnlimitedStock()) {
            lblStock.setText("Stock : illimité");
        } else {
            lblStock.setText("Stock disponible : " + p.getAvailableQuantity());
        }

        // Spinner quantité (min 1, max disponible ou 99 si illimité)
        int max = p.isUnlimitedStock() ? 99 : Math.max(1, p.getAvailableQuantity());
        SpinnerValueFactory<Integer> svf =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, max, 1);
        spinQty.setValueFactory(svf);
    }

    // ─── Actions ──────────────────────────────────────────────────────────────────

    @FXML
    public void handleAddCart() {
        if (currentPost == null) return;
        int qty = spinQty.getValue();
        CartService.getInstance().addPost(currentPost, qty);

        UserShellController shell = UserShellController.getInstance();
        if (shell != null) shell.refreshCartBadge();

        Alert alert = new Alert(Alert.AlertType.INFORMATION,
            qty + " × \"" + currentPost.getProductType() + "\" ajouté(s) au panier.",
            ButtonType.OK);
        alert.setHeaderText(null);
        alert.setTitle("Panier mis à jour");
        alert.showAndWait();
    }

    @FXML
    public void handleBack() {
        UserShellController shell = UserShellController.getInstance();
        if (shell != null) shell.showMarketplace();
    }

    @FXML
    public void handleGoCart() {
        UserShellController shell = UserShellController.getInstance();
        if (shell != null) shell.showCart();
    }

    // ─── Erreur ───────────────────────────────────────────────────────────────────

    private void showError(String msg) {
        lblArtwork.setText("⚠  " + msg);
    }
}
