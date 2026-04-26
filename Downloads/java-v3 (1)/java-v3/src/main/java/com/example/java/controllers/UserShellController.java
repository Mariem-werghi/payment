package com.example.java.controllers;

import com.example.java.services.CartService;
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

/**
 * Contrôleur de la coquille principale de l'application utilisateur.
 * Gère la barre de navigation et charge les sous-vues dans contentPane.
 *
 * Scénario :
 *   Marketplace (liste produits) → Détail produit → Panier → Confirmation
 */
public class UserShellController implements Initializable {

    // ─── FXML ─────────────────────────────────────────────────────────────────────
    @FXML private Button     navMarket;
    @FXML private Button     navSell;
    @FXML private Button     navCart;
    @FXML private Button     navOrders;
    @FXML private Label      cartBadge;
    @FXML private StackPane  contentPane;
    @FXML private Label      pageTitle;

    // Référence statique pour permettre aux sous-contrôleurs d'accéder au shell
    private static UserShellController instance;

    public static UserShellController getInstance() { return instance; }

    private List<Button> navButtons;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance   = this;
        navButtons = List.of(navMarket, navSell, navCart, navOrders);
        showMarketplace();       // Vue de départ
        refreshCartBadge();
    }

    // ─── Navigation publique ──────────────────────────────────────────────────────

    @FXML
    public void showMarketplace() {
        load("/com/example/java/marketplace-front.fxml", "Marketplace", navMarket);
    }

    /** Nouveau : formulaire vendeur — publier une œuvre côté user */
    @FXML
    public void showSell() {
        load("/com/example/java/marketplace-sell-form.fxml", "Publier une œuvre 🎨", navSell);
    }

    @FXML
    public void showCart() {
        refreshCartBadge();
        load("/com/example/java/marketplace-cart.fxml", "Mon Panier", navCart);
    }

    @FXML
    public void showOrders() {
        load("/com/example/java/user-orders.fxml", "Mes Commandes", navOrders);
    }

    /** Charge la fiche détail d'un produit (appelé depuis MarketplaceFrontController). */
    public void showProductDetail(int postId) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/java/marketplace-product-detail.fxml"));
            Node view = loader.load();

            MarketplaceProductDetailController ctrl = loader.getController();
            ctrl.loadPost(postId);

            contentPane.getChildren().setAll(view);
            pageTitle.setText("Détail du Produit");
            highlightBtn(null);
        } catch (IOException e) {
            System.err.println("Erreur chargement détail produit : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Charge l'écran de confirmation après commande passée. */
    public void showOrderSuccess(int ordreId) {
        showOrderSuccess(ordreId, null);
    }

    /** Charge l'écran de confirmation après commande passée, avec PDF disponible. */
    public void showOrderSuccess(int ordreId, java.io.File pdfFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/java/order-success.fxml"));
            Node view = loader.load();

            OrderSuccessController ctrl = loader.getController();
            ctrl.setOrdreId(ordreId);
            ctrl.setPdfFile(pdfFile);

            contentPane.getChildren().setAll(view);
            pageTitle.setText("Commande Confirmée ✅");
            highlightBtn(null);
            refreshCartBadge();
        } catch (IOException e) {
            System.err.println("Erreur chargement success : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Met à jour le badge panier dans la nav. */
    public void refreshCartBadge() {
        int qty = CartService.getInstance().getTotalQuantity();
        if (cartBadge != null) {
            cartBadge.setText(qty > 0 ? String.valueOf(qty) : "");
            cartBadge.setVisible(qty > 0);
        }
    }

    // ─── Loader interne ───────────────────────────────────────────────────────────

    private void load(String fxmlPath, String title, Button activeBtn) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            contentPane.getChildren().setAll(view);
            pageTitle.setText(title);
            highlightBtn(activeBtn);
        } catch (IOException e) {
            System.err.println("Erreur chargement " + fxmlPath + " : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void highlightBtn(Button active) {
        for (Button b : navButtons) {
            b.getStyleClass().remove("u-nav-active");
        }
        if (active != null) {
            active.getStyleClass().add("u-nav-active");
        }
    }
}
