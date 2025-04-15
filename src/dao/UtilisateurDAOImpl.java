package dao;

import model.Utilisateur;
import outils.Database;

import java.sql.*;

public class UtilisateurDAOImpl implements UtilisateurDAO {

    @Override
    public boolean checkLogin(String identifiant, String mdp) {
        String sql = "SELECT * FROM utilisateur WHERE identifiant = ? AND mdp = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, identifiant);
            stmt.setString(2, mdp);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insert(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur (identifiant, mdp, email, type_compte, nom, prenom, telephone) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utilisateur.getIdentifiant());
            stmt.setString(2, utilisateur.getMdp());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getTypeCompte());
            stmt.setString(5, utilisateur.getNom());
            stmt.setString(6, utilisateur.getPrenom());
            stmt.setString(7, utilisateur.getTelephone());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Utilisateur getByIdentifiant(String identifiant) {
        String sql = "SELECT * FROM utilisateur WHERE identifiant = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, identifiant);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setId(rs.getInt("id"));
                u.setIdentifiant(rs.getString("identifiant"));
                u.setMdp(rs.getString("mdp"));
                u.setEmail(rs.getString("email"));
                u.setTypeCompte(rs.getString("type_compte"));
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
