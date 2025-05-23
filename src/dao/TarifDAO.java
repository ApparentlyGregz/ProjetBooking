package dao;

import java.util.Date;

public interface TarifDAO {
    /**
     * Récupère le prix par nuit pour un logement donné à une date donnée (date d'arrivée).
     * @param logementId ID du logement
     * @param dateDebut Date d'arrivée de la réservation
     * @return prix par nuit applicable
     */
    double getPrixParNuit(int logementId);
    boolean ajouterTarif(int logementId, double prixParNuit);
    boolean modifierTarif(int logementId, double nouveauPrix);

}