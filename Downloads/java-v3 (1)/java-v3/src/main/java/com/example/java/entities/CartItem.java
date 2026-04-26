package com.example.java.entities;

/**
 * Représente un article dans le panier (en mémoire uniquement).
 * Pas de persistance en base – le panier est géré par CartService (Singleton).
 */
public class CartItem {

    private MarketplacePost post;
    private int quantity;

    // ─── Constructors ────────────────────────────────────────────────────────────

    public CartItem() {}

    public CartItem(MarketplacePost post, int quantity) {
        this.post     = post;
        this.quantity = quantity;
    }

    // ─── Business helpers ────────────────────────────────────────────────────────

    /** Montant de la ligne = prix unitaire × quantité */
    public double getSubtotal() {
        return post != null ? post.getPrice() * quantity : 0.0;
    }

    /** Nom d'affichage : type de produit + artwork id */
    public String getDisplayName() {
        if (post == null) return "—";
        return post.getProductType() + "  (Artwork #" + post.getArtworkId() + ")";
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public MarketplacePost getPost()                  { return post; }
    public void setPost(MarketplacePost post)         { this.post = post; }

    public int getQuantity()                          { return quantity; }
    public void setQuantity(int quantity)             { this.quantity = quantity; }

    @Override
    public String toString() {
        return "CartItem{postId=" + (post != null ? post.getId() : "null") +
               ", qty=" + quantity + ", subtotal=" + getSubtotal() + "}";
    }
}
