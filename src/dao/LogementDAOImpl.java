package dao;

import model.Logement;
import outils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogementDAOImpl implements LogementDAO {

    @Override
    public List<Logement> getAllLogementsAvecImages() {
        List<Logement> logements = new ArrayList<>();
        String sql = "SELECT l.id, l.nom, l.description, l.superficie, l.nb_personnes_max, l.nombre_etoiles, " +
                "l.date_creation, l.wifi, l.clim, l.parking, i.url_image " +
                "FROM logement l " +
                "LEFT JOIN logement_image i ON l.id = i.logement_id " +
                "ORDER BY l.id";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            int currentId = -1;
            Logement logement = null;

            while (rs.next()) {
                int id = rs.getInt("id");
                if (id != currentId) {
                    logement = new Logement();
                    logement.setId(id);
                    logement.setNom(rs.getString("nom"));
                    logement.setDescription(rs.getString("description"));
                    logement.setSuperficie(rs.getInt("superficie"));
                    logement.setNbPersonnesMax(rs.getInt("nb_personnes_max"));
                    logement.setNombreEtoiles(rs.getInt("nombre_etoiles"));
                    logement.setDateCreation(rs.getDate("date_creation"));
                    logement.setHasWifi(rs.getInt("wifi") == 1);
                    logement.setHasClim(rs.getInt("clim") == 1);
                    logement.setHasParking(rs.getInt("parking") == 1);

                    logements.add(logement);
                    currentId = id;
                }
                if (logement != null) {
                    String image = rs.getString("url_image");
                    if (image != null) {
                        logement.addImage(image);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logements;
    }

    @Override
    public boolean ajouterLogement(Logement logement) {
        String insertLogementSQL = "INSERT INTO logement (nom, description, superficie, nb_personnes_max, nombre_etoiles, date_creation, wifi, clim, parking, type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertImageSQL = "INSERT INTO logement_image (logement_id, url_image) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmtLogement = conn.prepareStatement(insertLogementSQL, Statement.RETURN_GENERATED_KEYS)) {

            // Insertion du logement
            stmtLogement.setString(1, logement.getNom());
            stmtLogement.setString(2, logement.getDescription());
            stmtLogement.setInt(3, logement.getSuperficie());
            stmtLogement.setInt(4, logement.getNbPersonnesMax());
            stmtLogement.setInt(5, logement.getNombreEtoiles());
            stmtLogement.setDate(6, new java.sql.Date(logement.getDateCreation().getTime()));
            stmtLogement.setBoolean(7, logement.hasWifi());
            stmtLogement.setBoolean(8, logement.hasClim());
            stmtLogement.setBoolean(9, logement.hasParking());
            stmtLogement.setString(10, logement.getType());

            int rows = stmtLogement.executeUpdate();
            if (rows == 0) return false;

            // Récupération de l'ID généré
            ResultSet generatedKeys = stmtLogement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int logementId = generatedKeys.getInt(1);

                // Insertion des images
                if (logement.getImages() != null && !logement.getImages().isEmpty()) {
                    try (PreparedStatement stmtImage = conn.prepareStatement(insertImageSQL)) {
                        for (String image : logement.getImages()) {
                            stmtImage.setInt(1, logementId);
                            stmtImage.setString(2, image);
                            stmtImage.addBatch();
                        }
                        stmtImage.executeBatch();
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


}
