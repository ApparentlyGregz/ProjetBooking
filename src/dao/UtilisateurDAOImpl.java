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

            return rs.next(); // Si un utilisateur est trouvé, alors les identifiants sont corrects
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Si aucun utilisateur n'est trouvé, retournez false
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
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setIdentifiant(rs.getString("identifiant"));
                utilisateur.setMdp(rs.getString("mdp")); // Le mot de passe sera utilisé pour vérifier la connexion
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setTypeCompte(rs.getString("type_compte"));
                utilisateur.setAncienClient(rs.getInt("ancien_client"));
                return utilisateur;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Si l'utilisateur n'est pas trouvé, retourner null
    }

}
