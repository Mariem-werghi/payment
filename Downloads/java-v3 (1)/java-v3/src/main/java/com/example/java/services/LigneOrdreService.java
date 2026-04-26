package com.example.java.services;

import com.example.java.entities.LigneOrdre;
import com.example.java.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigneOrdreService implements IService<LigneOrdre> {

    private final Connection connection;

    public LigneOrdreService() {
        this.connection = MyDatabase.getInstance().getConnection();
    }

    // ─── AJOUTER ─────────────────────────────────────────────────────────────────

    @Override
    public void ajouter(LigneOrdre lo) {
        String sql = "INSERT INTO ligne_order (price_at_purchase, quantity, order_id, marketplace_post_id) " +
                     "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, lo.getPriceAtPurchase());
            ps.setInt(2, lo.getQuantity());
            ps.setInt(3, lo.getOrdreId());
            ps.setInt(4, lo.getMarketplacePostId());
            ps.executeUpdate();
            System.out.println("LigneOrdre ajoutée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur ajouter LigneOrdre : " + e.getMessage());
        }
    }

    // ─── MODIFIER ────────────────────────────────────────────────────────────────

    @Override
    public void modifier(LigneOrdre lo) {
        String sql = "UPDATE ligne_order SET price_at_purchase=?, quantity=?, " +
                     "order_id=?, marketplace_post_id=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, lo.getPriceAtPurchase());
            ps.setInt(2, lo.getQuantity());
            ps.setInt(3, lo.getOrdreId());
            ps.setInt(4, lo.getMarketplacePostId());
            ps.setInt(5, lo.getId());
            ps.executeUpdate();
            System.out.println("LigneOrdre modifiée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur modifier LigneOrdre : " + e.getMessage());
        }
    }

    // ─── SUPPRIMER ───────────────────────────────────────────────────────────────

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM ligne_order WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("LigneOrdre supprimée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur supprimer LigneOrdre : " + e.getMessage());
        }
    }

    // ─── AFFICHER ALL ────────────────────────────────────────────────────────────

    @Override
    public List<LigneOrdre> afficherAll() {
        List<LigneOrdre> liste = new ArrayList<>();
        String sql = "SELECT * FROM ligne_order";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                LigneOrdre lo = new LigneOrdre();
                lo.setId(rs.getInt("id"));
                lo.setPriceAtPurchase(rs.getDouble("price_at_purchase"));
                lo.setQuantity(rs.getInt("quantity"));
                lo.setOrdreId(rs.getInt("order_id"));
                lo.setMarketplacePostId(rs.getInt("marketplace_post_id"));
                liste.add(lo);
            }
        } catch (SQLException e) {
            System.err.println("Erreur afficherAll LigneOrdre : " + e.getMessage());
        }
        return liste;
    }

    // ─── TROUVER PAR ID ──────────────────────────────────────────────────────────

    public LigneOrdre trouverParId(int id) {
        String sql = "SELECT * FROM ligne_order WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LigneOrdre lo = new LigneOrdre();
                lo.setId(rs.getInt("id"));
                lo.setPriceAtPurchase(rs.getDouble("price_at_purchase"));
                lo.setQuantity(rs.getInt("quantity"));
                lo.setOrdreId(rs.getInt("order_id"));
                lo.setMarketplacePostId(rs.getInt("marketplace_post_id"));
                return lo;
            }
        } catch (SQLException e) {
            System.err.println("Erreur trouverParId LigneOrdre : " + e.getMessage());
        }
        return null;
    }

    // ─── LIGNES PAR ORDRE ────────────────────────────────────────────────────────

    public List<LigneOrdre> afficherParOrdre(int ordreId) {
        List<LigneOrdre> liste = new ArrayList<>();
        String sql = "SELECT * FROM ligne_order WHERE order_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ordreId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LigneOrdre lo = new LigneOrdre();
                lo.setId(rs.getInt("id"));
                lo.setPriceAtPurchase(rs.getDouble("price_at_purchase"));
                lo.setQuantity(rs.getInt("quantity"));
                lo.setOrdreId(rs.getInt("order_id"));
                lo.setMarketplacePostId(rs.getInt("marketplace_post_id"));
                liste.add(lo);
            }
        } catch (SQLException e) {
            System.err.println("Erreur afficherParOrdre : " + e.getMessage());
        }
        return liste;
    }
}
