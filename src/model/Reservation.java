package model;

import java.util.Date;

public class Reservation {
    private int id;
    private int logementId;
    private int utilisateurId;
    private Date dateDebut;
    private Date dateFin;
    private int nombreAdultes;
    private int nombreEnfants;
    private String statut;
    private double prixTotal;
    private Integer promotionId; // nullable
    private Date dateReservation;

    // Getters et Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getLogementId() { return logementId; }
    public void setLogementId(int logementId) { this.logementId = logementId; }

    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

    public int getNombreAdultes() { return nombreAdultes; }
    public void setNombreAdultes(int nombreAdultes) { this.nombreAdultes = nombreAdultes; }

    public int getNombreEnfants() { return nombreEnfants; }
    public void setNombreEnfants(int nombreEnfants) { this.nombreEnfants = nombreEnfants; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(double prixTotal) { this.prixTotal = prixTotal; }

    public Integer getPromotionId() { return promotionId; }
    public void setPromotionId(Integer promotionId) { this.promotionId = promotionId; }

    public Date getDateReservation() { return dateReservation; }
    public void setDateReservation(Date dateReservation) { this.dateReservation = dateReservation; }
}