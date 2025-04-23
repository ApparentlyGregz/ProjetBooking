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
}
