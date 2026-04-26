package com.example.java.services;

import com.example.java.entities.MarketplacePost;
import com.example.java.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MarketplacePostService implements IService<MarketplacePost> {

    private final Connection connection;

    public MarketplacePostService() {
        this.connection = MyDatabase.getInstance().getConnection();
    }

    // ─── AJOUTER ─────────────────────────────────────────────────────────────────

    @Override
    public void ajouter(MarketplacePost mp) {
        String sql = "INSERT INTO marketplace_post " +
                     "(price, status, available_quantity, sold_quantity, product_type, " +
                     "unlimited_stock, created_at, updated_at, artwork_id, seller_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, mp.getPrice());
            ps.setString(2, mp.getStatus());
            ps.setInt(3, mp.getAvailableQuantity());
            ps.setInt(4, mp.getSoldQuantity());
            ps.setString(5, mp.getProductType());
            ps.setBoolean(6, mp.isUnlimitedStock());
            ps.setTimestamp(7, Timestamp.valueOf(
                mp.getCreatedAt() != null ? mp.getCreatedAt() : LocalDateTime.now()));
            ps.setTimestamp(8, mp.getUpdatedAt() != null ? Timestamp.valueOf(mp.getUpdatedAt()) : null);
            ps.setInt(9, mp.getArtworkId());
            ps.setInt(10, mp.getSellerId());
            ps.executeUpdate();
            System.out.println("MarketplacePost ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur ajouter MarketplacePost : " + e.getMessage());
        }
    }

    // ─── MODIFIER ────────────────────────────────────────────────────────────────

    @Override
    public void modifier(MarketplacePost mp) {
        String sql = "UPDATE marketplace_post SET price=?, status=?, available_quantity=?, " +
                     "sold_quantity=?, product_type=?, unlimited_stock=?, updated_at=?, " +
                     "artwork_id=?, seller_id=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, mp.getPrice());
            ps.setString(2, mp.getStatus());
            ps.setInt(3, mp.getAvailableQuantity());
            ps.setInt(4, mp.getSoldQuantity());
            ps.setString(5, mp.getProductType());
            ps.setBoolean(6, mp.isUnlimitedStock());
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(8, mp.getArtworkId());
            ps.setInt(9, mp.getSellerId());
            ps.setInt(10, mp.getId());
            ps.executeUpdate();
            System.out.println("MarketplacePost modifié avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur modifier MarketplacePost : " + e.getMessage());
        }
    }

    // ─── SUPPRIMER ───────────────────────────────────────────────────────────────

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM marketplace_post WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("MarketplacePost supprimé avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur supprimer MarketplacePost : " + e.getMessage());
        }
    }

    // ─── AFFICHER ALL ────────────────────────────────────────────────────────────

    @Override
    public List<MarketplacePost> afficherAll() {
        List<MarketplacePost> liste = new ArrayList<>();
        String sql = "SELECT * FROM marketplace_post";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                MarketplacePost mp = new MarketplacePost();
                mp.setId(rs.getInt("id"));
                mp.setPrice(rs.getDouble("price"));
                mp.setStatus(rs.getString("status"));
                mp.setAvailableQuantity(rs.getInt("available_quantity"));
                mp.setSoldQuantity(rs.getInt("sold_quantity"));
                mp.setProductType(rs.getString("product_type"));
                mp.setUnlimitedStock(rs.getBoolean("unlimited_stock"));
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) mp.setCreatedAt(createdAt.toLocalDateTime());
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                if (updatedAt != null) mp.setUpdatedAt(updatedAt.toLocalDateTime());
                mp.setArtworkId(rs.getInt("artwork_id"));
                mp.setSellerId(rs.getInt("seller_id"));
                liste.add(mp);
            }
        } catch (SQLException e) {
            System.err.println("Erreur afficherAll MarketplacePost : " + e.getMessage());
        }
        return liste;
    }

    // ─── TROUVER PAR ID ──────────────────────────────────────────────────────────

    public MarketplacePost trouverParId(int id) {
        String sql = "SELECT * FROM marketplace_post WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                MarketplacePost mp = new MarketplacePost();
                mp.setId(rs.getInt("id"));
                mp.setPrice(rs.getDouble("price"));
                mp.setStatus(rs.getString("status"));
                mp.setAvailableQuantity(rs.getInt("available_quantity"));
                mp.setSoldQuantity(rs.getInt("sold_quantity"));
                mp.setProductType(rs.getString("product_type"));
                mp.setUnlimitedStock(rs.getBoolean("unlimited_stock"));
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) mp.setCreatedAt(createdAt.toLocalDateTime());
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                if (updatedAt != null) mp.setUpdatedAt(updatedAt.toLocalDateTime());
                mp.setArtworkId(rs.getInt("artwork_id"));
                mp.setSellerId(rs.getInt("seller_id"));
                return mp;
            }
        } catch (SQLException e) {
            System.err.println("Erreur trouverParId MarketplacePost : " + e.getMessage());
        }
        return null;
    }
}
