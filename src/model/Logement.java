package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Logement {

    private boolean hasWifi;
    private String type;
    private boolean hasClim;
    private boolean hasParking;
    private Date dateCreation;
    private int superficie;
    private int nbPersonnesMax;
    private int id;
    private String nom;
    private String description;
    private List<String> images = new ArrayList<>();
    private int nombreEtoiles;
    // Constructeur
    public Logement(int id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }
    public Logement() {
        // Constructeur par défaut
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }


    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }


    public void setImages(List<String> images) {
        this.images = images;
    }

    // Ajouter une image
    public void addImage(String imagePath) {
        images.add(imagePath);
    }

    public int getSuperficie() {
        return superficie;
    }

    public void setSuperficie(int superficie) {
        this.superficie = superficie;
    }

    public int getNbPersonnesMax() {
        return nbPersonnesMax;
    }

    public void setNbPersonnesMax(int nbPersonnesMax) {
        this.nbPersonnesMax = nbPersonnesMax;
    }

    public int getNombreEtoiles() {
        return nombreEtoiles;
    }

    public void setNombreEtoiles(int nombreEtoiles) {
        this.nombreEtoiles = nombreEtoiles;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }



    public void setId(int id) {
        this.id = id;
    }

    public boolean hasWifi() {
        return hasWifi;
    }

    public void setHasWifi(boolean hasWifi) {
        this.hasWifi = hasWifi;
    }

    public boolean hasClim() {
        return hasClim;
    }

    public void setHasClim(boolean hasClim) {
        this.hasClim = hasClim;
    }

    public boolean hasParking() {
        return hasParking;
    }

    public void setHasParking(boolean hasParking) {
        this.hasParking = hasParking;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}

