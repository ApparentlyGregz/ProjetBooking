package dao;

import outils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {

    private Connection connection;

    public ReservationDAOImpl() {
        try {
            this.connection = Database.getConnection(); // ta classe outils.Database
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Date> getDatesReserveesPourLogement(int logementId) {
        List<Date> datesReservees = new ArrayList<>();

        String sql = "SELECT date_debut, date_fin FROM reservation WHERE logement_id = ? AND statut IN ('confirmée', 'en_attente')";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, logementId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                java.sql.Date dateDebutSQL = rs.getDate("date_debut");
                java.sql.Date dateFinSQL = rs.getDate("date_fin");

                if (dateDebutSQL != null && dateFinSQL != null) {
                    // Conversion SQL → Util
                    Date dateDebut = new Date(dateDebutSQL.getTime());
                    Date dateFin = new Date(dateFinSQL.getTime());

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateDebut);

                    while (!cal.getTime().after(dateFin)) {
                        datesReservees.add(new Date(cal.getTimeInMillis())); // java.util.Date
                        cal.add(Calendar.DATE, 1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return datesReservees;
    }
}
