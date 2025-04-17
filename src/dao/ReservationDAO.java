package dao;

import java.util.Date;
import java.util.List;

public interface ReservationDAO {

    /**
     * Renvoie la liste des dates (jour par jour) déjà réservées pour un logement donné.
     *
     * @param logementId identifiant du logement
     * @return liste des dates indisponibles (java.util.Date)
     */
    List<Date> getDatesReserveesPourLogement(int logementId);
}
