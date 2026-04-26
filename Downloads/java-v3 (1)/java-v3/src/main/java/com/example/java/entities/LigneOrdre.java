package com.example.java.entities;

public class LigneOrdre {

    private int id;
    private double priceAtPurchase;
    private int quantity;
    private int ordreId;
    private int marketplacePostId;

    // ─── Constructors ────────────────────────────────────────────────────────────

    public LigneOrdre() {}

    public LigneOrdre(double priceAtPurchase, int quantity, int ordreId, int marketplacePostId) {
        this.priceAtPurchase = priceAtPurchase;
        this.quantity = quantity;
        this.ordreId = ordreId;
        this.marketplacePostId = marketplacePostId;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getPriceAtPurchase() { return priceAtPurchase; }
    public void setPriceAtPurchase(double priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getOrdreId() { return ordreId; }
    public void setOrdreId(int ordreId) { this.ordreId = ordreId; }

    public int getMarketplacePostId() { return marketplacePostId; }
    public void setMarketplacePostId(int marketplacePostId) { this.marketplacePostId = marketplacePostId; }

    @Override
    public String toString() {
        return "LigneOrdre{id=" + id + ", priceAtPurchase=" + priceAtPurchase +
               ", quantity=" + quantity + ", ordreId=" + ordreId +
               ", marketplacePostId=" + marketplacePostId + "}";
    }
}
