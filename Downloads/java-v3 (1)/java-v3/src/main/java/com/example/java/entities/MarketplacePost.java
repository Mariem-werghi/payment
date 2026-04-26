package com.example.java.entities;

import java.time.LocalDateTime;

public class MarketplacePost {

    private int id;
    private double price;
    private String status;
    private int availableQuantity;
    private int soldQuantity;
    private String productType;
    private boolean unlimitedStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int artworkId;
    private int sellerId;

    // ─── Constructors ────────────────────────────────────────────────────────────

    public MarketplacePost() {}

    public MarketplacePost(double price, String status, int availableQuantity,
                           int soldQuantity, String productType, boolean unlimitedStock,
                           LocalDateTime createdAt, LocalDateTime updatedAt,
                           int artworkId, int sellerId) {
        this.price = price;
        this.status = status;
        this.availableQuantity = availableQuantity;
        this.soldQuantity = soldQuantity;
        this.productType = productType;
        this.unlimitedStock = unlimitedStock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.artworkId = artworkId;
        this.sellerId = sellerId;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }

    public int getSoldQuantity() { return soldQuantity; }
    public void setSoldQuantity(int soldQuantity) { this.soldQuantity = soldQuantity; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public boolean isUnlimitedStock() { return unlimitedStock; }
    public void setUnlimitedStock(boolean unlimitedStock) { this.unlimitedStock = unlimitedStock; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public int getArtworkId() { return artworkId; }
    public void setArtworkId(int artworkId) { this.artworkId = artworkId; }

    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }

    @Override
    public String toString() {
        return "MarketplacePost{id=" + id + ", price=" + price + ", status='" + status +
               "', productType='" + productType + "', artworkId=" + artworkId +
               ", sellerId=" + sellerId + "}";
    }
}
