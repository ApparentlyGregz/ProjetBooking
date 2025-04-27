package dao;

import outils.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TarifDAOImpl implements TarifDAO {

    @Override
    public double getPrixParNuit(int logementId) {
        String sql = "SELECT prix_nuit FROM tarif WHERE logement_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, logementId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("prix_nuit");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0; // prix par défaut si non trouvé
    }

    @Override
    public boolean modifierTarif(int logementId, double nouveauPrix) {
        String updateTarifSQL = "UPDATE tarif SET prix_nuit = ? WHERE logement_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateTarifSQL)) {

            stmt.setDouble(1, nouveauPrix);
            stmt.setInt(2, logementId);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean ajouterTarif(int logementId, double prixParNuit) {
        String insertTarifSQL = "INSERT INTO tarif (logement_id, prix_nuit) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertTarifSQL)) {

            stmt.setInt(1, logementId);
            stmt.setDouble(2, prixParNuit);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
