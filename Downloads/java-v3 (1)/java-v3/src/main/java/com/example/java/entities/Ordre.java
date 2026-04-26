package com.example.java.entities;

import java.time.LocalDateTime;

public class Ordre {

    private int id;
    private double totalAmount;
    private LocalDateTime createdAt;
    private int buyerId;

    // ─── Constructors ────────────────────────────────────────────────────────────

    public Ordre() {}

    public Ordre(double totalAmount, LocalDateTime createdAt, int buyerId) {
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.buyerId = buyerId;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getBuyerId() { return buyerId; }
    public void setBuyerId(int buyerId) { this.buyerId = buyerId; }

    @Override
    public String toString() {
        return "Ordre{id=" + id + ", totalAmount=" + totalAmount +
               ", createdAt=" + createdAt + ", buyerId=" + buyerId + "}";
    }
}
