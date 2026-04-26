package com.example.java.controllers;

import com.example.java.entities.LigneOrdre;
import com.example.java.entities.MarketplacePost;
import com.example.java.entities.Ordre;
import com.example.java.services.LigneOrdreService;
import com.example.java.services.MarketplacePostService;
import com.example.java.services.OrdreService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Historique des commandes côté utilisateur — affichage en cartes, sans IDs.
 */
public class UserOrdersController implements Initializable {

    private static final int BUYER_ID = 1;

    @FXML private VBox  orderContainer;
    @FXML private Label lblCount;

    private final OrdreService          ordreService = new OrdreService();
    private final LigneOrdreService     ligneService = new LigneOrdreService();
    private final MarketplacePostService postService  = new MarketplacePostService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chargerCommandes();
    }

    @FXML
    public void chargerCommandes() {
        List<Ordre> ordres = ordreService.afficherParBuyer(BUYER_ID);
        orderContainer.getChildren().clear();

        if (ordres.isEmpty()) {
            Label vide = new Label("Vous n'avez pas encore passé de commande.");
            vide.setStyle("-fx-text-fill: #7a6a5a; -fx-font-size: 14px;");
            vide.setPadding(new Insets(32));
            orderContainer.getChildren().add(vide);
            lblCount.setText("0 commande");
            return;
        }

        lblCount.setText(ordres.size() + " commande(s)");
        for (Ordre o : ordres) orderContainer.getChildren().add(buildOrderCard(o));
    }

    // ─── Carte commande ───────────────────────────────────────────────────────────

    private VBox buildOrderCard(Ordre ordre) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(18, 22, 18, 22));
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #d4cfc7; -fx-border-width: 1;" +
            "-fx-border-radius: 12; -fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(74,55,40,0.07), 8, 0, 0, 3);"
        );

        // ── En-tête de la carte ──
        HBox header = new HBox(14);
        header.setAlignment(Pos.CENTER_LEFT);

        Label icone = new Label("✅");
        icone.setStyle("-fx-font-size: 22px;");

        VBox titreZone = new VBox(2);
        HBox.setHgrow(titreZone, Priority.ALWAYS);

        Label lblTitre = new Label("Commande confirmée");
        lblTitre.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #2c2218;");

        String date = ordre.getCreatedAt() != null
            ? "Passée le  " + ordre.getCreatedAt().toLocalDate() : "Date inconnue";
        Label lblDate = new Label(date);
        lblDate.setStyle("-fx-font-size: 12px; -fx-text-fill: #7a6a5a;");

        titreZone.getChildren().addAll(lblTitre, lblDate);

        // Montant total (à droite)
        Label lblMontant = new Label(String.format("%.2f €", ordre.getTotalAmount()));
        lblMontant.setStyle(
            "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #4a3728;" +
            "-fx-font-family: Georgia, serif;");

        header.getChildren().addAll(icone, titreZone, lblMontant);

        // ── Séparateur ──
        Separator sep = new Separator();

        // ── Articles de la commande ──
        List<LigneOrdre> lignes = ligneService.afficherParOrdre(ordre.getId());
        VBox articlesBox = new VBox(6);

        Label lblArticlesTitle = new Label(lignes.size() + " article(s) commandé(s)");
        lblArticlesTitle.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #7a6a5a;");
        articlesBox.getChildren().add(lblArticlesTitle);

        for (LigneOrdre ligne : lignes) {
            HBox ligneCard = new HBox(12);
            ligneCard.setAlignment(Pos.CENTER_LEFT);
            ligneCard.setPadding(new Insets(8, 12, 8, 12));
            ligneCard.setStyle(
                "-fx-background-color: #f5f3ef; -fx-background-radius: 8;" +
                "-fx-border-color: #ebe8e2; -fx-border-radius: 8; -fx-border-width: 1;");

            // Récupérer les infos du post (sans afficher l'ID brut)
            String nomProduit = "Produit";
            String typeLabel  = "";
            try {
                MarketplacePost post = postService.trouverParId(ligne.getMarketplacePostId());
                if (post != null) {
                    nomProduit = nomProduit(post.getProductType());
                    typeLabel  = post.getProductType().toUpperCase();
                }
            } catch (Exception ignored) {}

            Label lgBadge = new Label(typeLabel.isEmpty() ? "ART" : typeLabel);
            lgBadge.setStyle(
                "-fx-background-color: #e8e4dc; -fx-background-radius: 20;" +
                "-fx-text-fill: #4a3728; -fx-font-size: 9px; -fx-font-weight: bold;" +
                "-fx-padding: 3 8 3 8; -fx-min-width: 60;");
            lgBadge.setAlignment(Pos.CENTER);

            Label lgNom = new Label(nomProduit);
            lgNom.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #2c2218;");
            HBox.setHgrow(lgNom, Priority.ALWAYS);

            double sousTotal = ligne.getQuantity() * ligne.getPriceAtPurchase();
            Label lgDetail = new Label(
                ligne.getQuantity() + " × " + String.format("%.2f €", ligne.getPriceAtPurchase()));
            lgDetail.setStyle("-fx-font-size: 12px; -fx-text-fill: #7a6a5a;");

            Label lgSousTotal = new Label(String.format("%.2f €", sousTotal));
            lgSousTotal.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #4a3728;");

            ligneCard.getChildren().addAll(lgBadge, lgNom, lgDetail, lgSousTotal);
            articlesBox.getChildren().add(ligneCard);
        }

        card.getChildren().addAll(header, sep, articlesBox);
        return card;
    }

    // ─── Nom lisible (sans ID) ────────────────────────────────────────────────────

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
