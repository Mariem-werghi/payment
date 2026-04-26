package com.example.java.services;

import com.example.java.entities.CartItem;
import com.example.java.entities.MarketplacePost;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service panier en mémoire – Singleton.
 * Gère les articles ajoutés au panier par l'utilisateur avant la validation
 * de commande. Aucune persistance en base à ce stade.
 */
public class CartService {

    // ─── Singleton ────────────────────────────────────────────────────────────────
    private static CartService instance;

    private CartService() {}

    public static CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }
        return instance;
    }

    // ─── Panier en mémoire ────────────────────────────────────────────────────────
    private final List<CartItem> items = new ArrayList<>();

    // ─── API ─────────────────────────────────────────────────────────────────────

    /** Retourne tous les articles du panier (lecture seule conseillée). */
    public List<CartItem> getItems() {
        return items;
    }

    /** Ajoute un post ou incrémente sa quantité si déjà présent. */
    public void addPost(MarketplacePost post, int qty) {
        if (post == null || qty <= 0) return;
        Optional<CartItem> existing = items.stream()
                .filter(i -> i.getPost().getId() == post.getId())
                .findFirst();
        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + qty);
        } else {
            items.add(new CartItem(post, qty));
        }
    }

    /** Modifie la quantité d'un article. Si qty <= 0, supprime l'article. */
    public void updateQuantity(int postId, int qty) {
        if (qty <= 0) {
            removePost(postId);
            return;
        }
        items.stream()
             .filter(i -> i.getPost().getId() == postId)
             .findFirst()
             .ifPresent(i -> i.setQuantity(qty));
    }

    /** Retire un article du panier. */
    public void removePost(int postId) {
        items.removeIf(i -> i.getPost().getId() == postId);
    }

    /** Vide complètement le panier. */
    public void clear() {
        items.clear();
    }

    /** Calcule le montant total du panier. */
    public double getTotal() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    /** Nombre total d'articles (somme des quantités). */
    public int getTotalQuantity() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    /** Vrai si le panier est vide. */
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
