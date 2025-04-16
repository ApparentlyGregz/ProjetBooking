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
        String sql = "INSERT INTO logement (nom, description, superficie, nb_personnes_max, nombre_etoiles, date_creation, wifi, clim, parking, type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, logement.getNom());
            stmt.setString(2, logement.getDescription());
            stmt.setInt(3, logement.getSuperficie());
            stmt.setInt(4, logement.getNbPersonnesMax());
            stmt.setInt(5, logement.getNombreEtoiles());
            stmt.setDate(6, new java.sql.Date(logement.getDateCreation().getTime()));
            stmt.setBoolean(7, logement.hasWifi());
            stmt.setBoolean(8, logement.hasClim());
            stmt.setBoolean(9, logement.hasParking());
            stmt.setString(10, logement.getType());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
