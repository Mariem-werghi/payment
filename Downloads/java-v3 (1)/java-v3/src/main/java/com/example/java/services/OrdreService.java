package com.example.java.services;

import com.example.java.entities.Ordre;
import com.example.java.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdreService implements IService<Ordre> {

    private final Connection connection;

    public OrdreService() {
        this.connection = MyDatabase.getInstance().getConnection();
    }

    // ─── AJOUTER ─────────────────────────────────────────────────────────────────

    @Override
    public void ajouter(Ordre ordre) {
        String sql = "INSERT INTO orders (total_amount, created_at, buyer_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, ordre.getTotalAmount());
            ps.setTimestamp(2, Timestamp.valueOf(
                ordre.getCreatedAt() != null ? ordre.getCreatedAt() : LocalDateTime.now()));
            ps.setInt(3, ordre.getBuyerId());
            ps.executeUpdate();
            System.out.println("Ordre ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur ajouter Ordre : " + e.getMessage());
        }
    }

    // ─── MODIFIER ────────────────────────────────────────────────────────────────

    @Override
    public void modifier(Ordre ordre) {
        String sql = "UPDATE orders SET total_amount=?, buyer_id=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, ordre.getTotalAmount());
            ps.setInt(2, ordre.getBuyerId());
            ps.setInt(3, ordre.getId());
            ps.executeUpdate();
            System.out.println("Ordre modifié avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur modifier Ordre : " + e.getMessage());
        }
    }

    // ─── SUPPRIMER ───────────────────────────────────────────────────────────────

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM orders WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Ordre supprimé avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur supprimer Ordre : " + e.getMessage());
        }
    }

    // ─── AFFICHER ALL ────────────────────────────────────────────────────────────

    @Override
    public List<Ordre> afficherAll() {
        List<Ordre> liste = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Ordre ordre = new Ordre();
                ordre.setId(rs.getInt("id"));
                ordre.setTotalAmount(rs.getDouble("total_amount"));
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) ordre.setCreatedAt(createdAt.toLocalDateTime());
                ordre.setBuyerId(rs.getInt("buyer_id"));
                liste.add(ordre);
            }
        } catch (SQLException e) {
            System.err.println("Erreur afficherAll Ordre : " + e.getMessage());
        }
        return liste;
    }

    // ─── TROUVER PAR ID ──────────────────────────────────────────────────────────

    public Ordre trouverParId(int id) {
        String sql = "SELECT * FROM orders WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Ordre ordre = new Ordre();
                ordre.setId(rs.getInt("id"));
                ordre.setTotalAmount(rs.getDouble("total_amount"));
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) ordre.setCreatedAt(createdAt.toLocalDateTime());
                ordre.setBuyerId(rs.getInt("buyer_id"));
                return ordre;
            }
        } catch (SQLException e) {
            System.err.println("Erreur trouverParId Ordre : " + e.getMessage());
        }
        return null;
    }

    // ─── ORDRES PAR BUYER ────────────────────────────────────────────────────────

    public List<Ordre> afficherParBuyer(int buyerId) {
        List<Ordre> liste = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE buyer_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, buyerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ordre ordre = new Ordre();
                ordre.setId(rs.getInt("id"));
                ordre.setTotalAmount(rs.getDouble("total_amount"));
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) ordre.setCreatedAt(createdAt.toLocalDateTime());
                ordre.setBuyerId(rs.getInt("buyer_id"));
                liste.add(ordre);
            }
        } catch (SQLException e) {
            System.err.println("Erreur afficherParBuyer : " + e.getMessage());
        }
        return liste;
    }
}
