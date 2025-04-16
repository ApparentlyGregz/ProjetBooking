package model;

import java.util.ArrayList;
import java.util.List;

public class Logement {
    private int id;
    private String nom;
    private String description;
    private List<String> images = new ArrayList<>();

    // Constructeur
    public Logement(int id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getImages() {
        return images;
    }

    // Ajouter une image
    public void addImage(String imagePath) {
        images.add(imagePath);
    }
}

