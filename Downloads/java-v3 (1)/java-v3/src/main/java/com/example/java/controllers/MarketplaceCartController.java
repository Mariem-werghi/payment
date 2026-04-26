package com.example.java.controllers;

import com.example.java.entities.CartItem;
import com.example.java.entities.LigneOrdre;
import com.example.java.entities.Ordre;
import com.example.java.services.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Contrôleur du panier — flux complet :
 *   1. Affichage articles
 *   2. Saisie code promo reçu par email (30% de réduction)
 *   3. Paiement Stripe (WebView intégré dans l'app)
 *   4. Création Ordre + LigneOrdre en BDD
 *   5. Génération PDF avec signature ARTERA
 *   6. Génération QR code promo (nouveau code pour la prochaine commande)
 *   7. Envoi email avec PDF + QR code en pièce jointe
 *   8. Affichage page de succès
 */
public class MarketplaceCartController implements Initializable {

    private static final String STRIPE_URL =
        "https://buy.stripe.com/test_7sYfZa4rC1Am3xf7mB1wY02";

    // ── Articles ─────────────────────────────────────────────────────────────────
    @FXML private VBox    cartContainer;
    @FXML private Label   lblTotal;
    @FXML private Label   lblTotalWithPromo;
    @FXML private Button  btnCommander;
    @FXML private Button  btnContinuer;
    @FXML private Button  btnVider;

    // ── Zone promo ────────────────────────────────────────────────────────────────
    @FXML private TextField tfPromoCode;
    @FXML private Button    btnApplyPromo;
    @FXML private Label     lblPromoStatus;

    // ── Services ──────────────────────────────────────────────────────────────────
    private final CartService       cartService  = CartService.getInstance();
    private final OrdreService      ordreService = new OrdreService();
    private final LigneOrdreService ligneService = new LigneOrdreService();
    private final PromoService      promoService = PromoService.getInstance();
    private final PdfService        pdfService   = new PdfService();
    private final EmailService      emailService = new EmailService();

    // ── État promo utilisée sur cette commande ────────────────────────────────────
    private boolean promoApplied     = false;
    private String  appliedPromoCode = null;
    private double  discountedTotal  = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chargerPanier();
    }

    // ─── Chargement des cartes articles ──────────────────────────────────────────

    @FXML
    public void chargerPanier() {
        cartContainer.getChildren().clear();
        List<CartItem> items = cartService.getItems();

        if (items.isEmpty()) {
            Label vide = new Label("🛒  Votre panier est vide — parcourez la marketplace !");
            vide.setStyle("-fx-text-fill: #7a6a5a; -fx-font-size: 14px;");
            vide.setPadding(new Insets(32));
            cartContainer.getChildren().add(vide);
        } else {
            for (CartItem item : items) {
                cartContainer.getChildren().add(buildCartCard(item));
            }
        }

        refreshTotal();
        boolean empty = cartService.isEmpty();
        btnCommander.setDisable(empty);
        btnVider.setDisable(empty);

        if (empty) resetPromo();
    }

    // ─── Carte article ────────────────────────────────────────────────────────────

    private HBox buildCartCard(CartItem item) {
        HBox card = new HBox(16);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(16, 20, 16, 20));
        card.setStyle("-fx-background-color:white;-fx-border-color:#d4cfc7;-fx-border-width:1;" +
            "-fx-border-radius:10;-fx-background-radius:10;" +
            "-fx-effect:dropshadow(gaussian,rgba(74,55,40,0.06),6,0,0,2);");

        Label badge = new Label(item.getPost().getProductType().toUpperCase());
        badge.setMinWidth(80);
        badge.setAlignment(Pos.CENTER);
        badge.setStyle("-fx-background-color:#e8e4dc;-fx-background-radius:20;" +
            "-fx-text-fill:#4a3728;-fx-font-size:10px;-fx-font-weight:bold;-fx-padding:5 10 5 10;");

        VBox infos = new VBox(4);
        HBox.setHgrow(infos, Priority.ALWAYS);
        String stockInfo = item.getPost().isUnlimitedStock()
            ? "Stock illimité" : item.getPost().getAvailableQuantity() + " disponibles";
        Label lblNom   = new Label(nomProduit(item.getPost().getProductType()));
        lblNom.setStyle("-fx-font-size:15px;-fx-font-weight:bold;-fx-text-fill:#2c2218;");
        Label lblStock = new Label("📦  " + stockInfo);
        lblStock.setStyle("-fx-font-size:12px;-fx-text-fill:#7a6a5a;");
        infos.getChildren().addAll(lblNom, lblStock);

        VBox prixZone = new VBox(4);
        prixZone.setAlignment(Pos.CENTER_RIGHT);
        prixZone.setMinWidth(160);
        Label lblPrixUnit  = new Label(String.format("%.2f € / unité", item.getPost().getPrice()));
        lblPrixUnit.setStyle("-fx-font-size:12px;-fx-text-fill:#7a6a5a;");
        Label lblSousTotal = new Label(String.format("%.2f €", item.getSubtotal()));
        lblSousTotal.setStyle("-fx-font-size:17px;-fx-font-weight:bold;-fx-text-fill:#4a3728;");

        HBox qteBox = new HBox(6);
        qteBox.setAlignment(Pos.CENTER_RIGHT);
        Button btnMoins   = qteBtn("➖");
        Label  lblQte     = new Label(String.valueOf(item.getQuantity()));
        lblQte.setStyle("-fx-font-size:14px;-fx-font-weight:bold;-fx-min-width:24;-fx-alignment:CENTER;");
        Button btnPlus    = qteBtn("➕");
        Button btnRetirer = new Button("🗑");
        btnRetirer.setStyle("-fx-background-color:transparent;-fx-border-color:#f0b0b0;" +
            "-fx-border-radius:6;-fx-background-radius:6;-fx-border-width:1;" +
            "-fx-text-fill:#c0392b;-fx-font-size:12px;-fx-padding:4 8 4 8;-fx-cursor:hand;");

        btnMoins.setOnAction(e -> { cartService.updateQuantity(item.getPost().getId(), item.getQuantity() - 1); chargerPanier(); mettreAJourBadge(); });
        btnPlus.setOnAction( e -> { cartService.updateQuantity(item.getPost().getId(), item.getQuantity() + 1); chargerPanier(); });
        btnRetirer.setOnAction(e -> { cartService.removePost(item.getPost().getId()); chargerPanier(); mettreAJourBadge(); });

        qteBox.getChildren().addAll(btnMoins, lblQte, btnPlus, btnRetirer);
        prixZone.getChildren().addAll(lblPrixUnit, lblSousTotal, qteBox);
        card.getChildren().addAll(badge, infos, prixZone);
        return card;
    }

    // ─── CODE PROMO ──────────────────────────────────────────────────────────────

    @FXML
    public void handleApplyPromo() {
        if (promoApplied) { setPromoStatus("⚠  Un code promo est déjà appliqué.", false); return; }
        String code = (tfPromoCode != null) ? tfPromoCode.getText().trim() : "";
        if (code.isEmpty()) { setPromoStatus("⚠  Entrez un code promo.", false); return; }

        if (promoService.validateCode(code)) {
            promoApplied     = true;
            appliedPromoCode = code.toUpperCase();
            discountedTotal  = promoService.applyDiscount(cartService.getTotal());
            setPromoStatus("✅  Code « " + appliedPromoCode + " » appliqué !  −30% 🎉", true);
            refreshTotal();
            if (tfPromoCode   != null) tfPromoCode.setDisable(true);
            if (btnApplyPromo != null) btnApplyPromo.setDisable(true);
        } else {
            setPromoStatus("❌  Code invalide ou expiré.", false);
        }
    }

    private void setPromoStatus(String msg, boolean success) {
        if (lblPromoStatus != null) {
            lblPromoStatus.setText(msg);
            lblPromoStatus.setStyle(success
                ? "-fx-text-fill:#2d6a4f;-fx-font-size:12px;"
                : "-fx-text-fill:#c0392b;-fx-font-size:12px;");
            lblPromoStatus.setVisible(true);
        }
    }

    private void resetPromo() {
        promoApplied = false; appliedPromoCode = null; discountedTotal = 0;
        if (lblPromoStatus != null) lblPromoStatus.setVisible(false);
        if (tfPromoCode    != null) { tfPromoCode.clear(); tfPromoCode.setDisable(false); }
        if (btnApplyPromo  != null) btnApplyPromo.setDisable(false);
    }

    // ─── TOTAL ───────────────────────────────────────────────────────────────────

    private void refreshTotal() {
        double raw = cartService.getTotal();
        if (lblTotal != null)
            lblTotal.setText(String.format("Sous-total : %.2f €", raw));
        if (lblTotalWithPromo != null) {
            if (promoApplied) {
                lblTotalWithPromo.setText(String.format("Total après réduction : %.2f €", discountedTotal));
                lblTotalWithPromo.setStyle("-fx-font-size:16px;-fx-font-weight:bold;-fx-text-fill:#2d6a4f;");
            } else {
                lblTotalWithPromo.setText(String.format("Total : %.2f €", raw));
                lblTotalWithPromo.setStyle("-fx-font-size:16px;-fx-font-weight:bold;-fx-text-fill:#4a3728;");
            }
            lblTotalWithPromo.setVisible(true);
        }
    }

    // ─── COMMANDER — Flux complet ─────────────────────────────────────────────────

    @FXML
    public void handleCommander() {
        if (cartService.isEmpty()) {
            alert(Alert.AlertType.WARNING, "Panier vide", "Ajoutez des articles avant de commander.");
            return;
        }

        // ── Étape 1 : Email ───────────────────────────────────────────────────────
        String userEmail = demanderEmail();
        if (userEmail == null) return;

        // ── Étape 2 : Stripe (WebView intégré dans l'app) ────────────────────────
        if (!ouvrirStripeInApp(userEmail)) return;

        // ── Étape 3 : Créer Ordre + Lignes ───────────────────────────────────────
        double finalAmount   = promoApplied ? discountedTotal : cartService.getTotal();
        double originalTotal = cartService.getTotal();

        try {
            Ordre ordre = new Ordre();
            ordre.setTotalAmount(finalAmount);
            ordre.setCreatedAt(LocalDateTime.now());
            ordre.setBuyerId(1);
            ordreService.ajouter(ordre);

            int ordreId = ordreService.afficherAll().stream()
                .mapToInt(Ordre::getId).max()
                .orElseThrow(() -> new Exception("Ordre introuvable après insertion"));

            List<CartItem> items = List.copyOf(cartService.getItems());
            for (CartItem item : items) {
                LigneOrdre ligne = new LigneOrdre();
                double prixLigne = promoApplied
                    ? item.getPost().getPrice() * 0.70
                    : item.getPost().getPrice();
                ligne.setPriceAtPurchase(prixLigne);
                ligne.setQuantity(item.getQuantity());
                ligne.setOrdreId(ordreId);
                ligne.setMarketplacePostId(item.getPost().getId());
                ligneService.ajouter(ligne);
            }

            // ── Étape 4 : PDF ─────────────────────────────────────────────────────
            Ordre ordreEnreg = ordreService.trouverParId(ordreId);
            File pdfFile = null;
            try {
                pdfFile = pdfService.generateOrderPDF(
                    ordreEnreg, items, userEmail, appliedPromoCode, originalTotal);
            } catch (Exception pdfEx) {
                System.err.println("[Cart] PDF : " + pdfEx.getMessage());
            }

            // ── Étape 5 : Générer un NOUVEAU code promo pour la prochaine commande ─
            //    (invisible dans l'app — envoyé uniquement par email avec QR code)
            promoService.generateNewPromo();
            final String nextPromoCode = promoService.getCurrentCode();
            File qrFile = null;
            try {
                qrFile = promoService.generateQRCodeFile(220);
            } catch (Exception qrEx) {
                System.err.println("[Cart] QR code file : " + qrEx.getMessage());
            }

            // ── Étape 6 : Email (thread séparé) ──────────────────────────────────
            final File   pdfFinal       = pdfFile;
            final File   qrFinal        = qrFile;
            final String emailFinal     = userEmail;
            final String promoUtilisee  = appliedPromoCode;
            final Ordre  ordreFinal     = ordreEnreg;
            final String nextPromoFinal = nextPromoCode;

            new Thread(() -> {
                try {
                    emailService.sendOrderConfirmation(
                        emailFinal, ordreFinal, pdfFinal, promoUtilisee,
                        nextPromoFinal, qrFinal);
                    Platform.runLater(() ->
                        alert(Alert.AlertType.INFORMATION, "📧 Email envoyé",
                              "Confirmation + facture PDF + code promo envoyés à :\n" + emailFinal));
                } catch (Exception mailEx) {
                    System.err.println("[Cart] Email : " + mailEx.getMessage());
                    Platform.runLater(() ->
                        alert(Alert.AlertType.WARNING, "Email non envoyé",
                              "Commande confirmée, mais l'email a échoué.\n" + mailEx.getMessage()));
                }
            }).start();

            // ── Étape 7 : Succès ──────────────────────────────────────────────────
            cartService.clear();
            resetPromo();

            UserShellController shell = UserShellController.getInstance();
            if (shell != null) shell.showOrderSuccess(ordreId, pdfFile);

        } catch (Exception e) {
            alert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
            e.printStackTrace();
        }
    }

    // ─── Helpers checkout ─────────────────────────────────────────────────────────

    private String demanderEmail() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("💳  Paiement Stripe");
        dlg.setHeaderText("Entrez votre adresse email");
        dlg.setContentText("Email :");
        Optional<String> res = dlg.showAndWait();
        if (res.isEmpty() || res.get().isBlank()) return null;
        String email = res.get().trim();
        if (!email.contains("@") || !email.contains(".")) {
            alert(Alert.AlertType.ERROR, "Email invalide", "Entrez une adresse email valide.");
            return null;
        }
        return email;
    }

    private boolean ouvrirStripeInApp(String email) {
        try {
            String encodedEmail = java.net.URLEncoder.encode(email, "UTF-8");
            String url = STRIPE_URL + "?prefilled_email=" + encodedEmail;
            return StripeWebViewController.openPaymentWindow(url);
        } catch (Exception e) {
            System.err.println("[Cart] Stripe WebView : " + e.getMessage());
            alert(Alert.AlertType.ERROR, "Erreur Stripe",
                  "Impossible d'ouvrir la page de paiement :\n" + e.getMessage());
            return false;
        }
    }

    // ─── Actions panier ───────────────────────────────────────────────────────────

    @FXML
    public void handleVider() {
        new Alert(Alert.AlertType.CONFIRMATION, "Vider le panier ?", ButtonType.YES, ButtonType.NO)
            .showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) { cartService.clear(); resetPromo(); chargerPanier(); mettreAJourBadge(); }
            });
    }

    @FXML
    public void handleContinuer() {
        UserShellController shell = UserShellController.getInstance();
        if (shell != null) shell.showMarketplace();
    }

    private void mettreAJourBadge() {
        UserShellController shell = UserShellController.getInstance();
        if (shell != null) shell.refreshCartBadge();
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────────

    private Button qteBtn(String txt) {
        Button b = new Button(txt);
        b.setStyle("-fx-background-color:#f5f3ef;-fx-border-color:#d4cfc7;" +
            "-fx-border-radius:6;-fx-background-radius:6;-fx-border-width:1;" +
            "-fx-font-size:11px;-fx-padding:4 8 4 8;-fx-cursor:hand;");
        return b;
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

    private void alert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type, msg, ButtonType.OK);
        a.setTitle(title); a.setHeaderText(null); a.showAndWait();
    }
}
