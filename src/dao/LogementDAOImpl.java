package dao;

import model.Logement;
import utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogementDAOImpl implements LogementDAO {

    @Override
    public List<Logement> getAllLogementsAvecImages() {
        List<Logement> logements = new ArrayList<>();
        String sql = "SELECT l.id, l.nom, l.description, i.url_image " +
                     "FROM logement l LEFT JOIN logement_image i ON l.id = i.logement_id " +
                     "ORDER BY l.id";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            int currentId = -1;
            Logement logement = null;

            while (rs.next()) {
                int id = rs.getInt("id");
                if (id != currentId) {
                    logement = new Logement(id, rs.getString("nom"), rs.getString("description"));
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
}
