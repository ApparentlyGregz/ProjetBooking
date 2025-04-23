package dao;

import model.Reservation;
import outils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {

    private Connection connection;

    public ReservationDAOImpl() {
        try {
            this.connection = Database.getConnection();
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
                    Date dateDebut = new Date(dateDebutSQL.getTime());
                    Date dateFin = new Date(dateFinSQL.getTime());

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateDebut);

                    while (!cal.getTime().after(dateFin)) {
                        datesReservees.add(new Date(cal.getTimeInMillis()));
                        cal.add(Calendar.DATE, 1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return datesReservees;
    }

    public boolean insertReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation (logement_id, utilisateur_id, date_debut, date_fin, nombre_adultes, nombre_enfants, statut, prix_total, promotion_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservation.getLogementId());
            stmt.setInt(2, reservation.getUtilisateurId());
            stmt.setDate(3, new java.sql.Date(reservation.getDateDebut().getTime()));
            stmt.setDate(4, new java.sql.Date(reservation.getDateFin().getTime()));
            stmt.setInt(5, reservation.getNombreAdultes());
            stmt.setInt(6, reservation.getNombreEnfants());
            stmt.setString(7, reservation.getStatut());
            stmt.setDouble(8, reservation.getPrixTotal());

            if (reservation.getPromotionId() != null) {
                stmt.setInt(9, reservation.getPromotionId());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reservation> getReservationsEnAttente(int utilisateurId) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE utilisateur_id = ? AND statut = 'en_attente'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reservation r = new Reservation();
                r.setId(rs.getInt("id"));
                r.setLogementId(rs.getInt("logement_id"));
                r.setUtilisateurId(rs.getInt("utilisateur_id"));
                r.setDateDebut(rs.getDate("date_debut"));
                r.setDateFin(rs.getDate("date_fin"));
                r.setNombreAdultes(rs.getInt("nombre_adultes"));
                r.setNombreEnfants(rs.getInt("nombre_enfants"));
                r.setStatut(rs.getString("statut"));
                r.setPrixTotal(rs.getDouble("prix_total"));
                r.setPromotionId((Integer) rs.getObject("promotion_id"));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean confirmerReservation(int reservationId) {
        String sql = "UPDATE reservation SET statut = 'confirmée' WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
