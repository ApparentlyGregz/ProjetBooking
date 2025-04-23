package dao;

import outils.Database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TarifDAOImpl implements TarifDAO {

    @Override
    public double getPrixParNuitPourPeriode(int logementId, java.util.Date dateDebut) {
        String sql = "SELECT prix_nuit FROM tarif WHERE logement_id = ? AND ? BETWEEN date_debut AND date_fin LIMIT 1";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, logementId);
            stmt.setDate(2, new Date(dateDebut.getTime()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("prix_nuit");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}