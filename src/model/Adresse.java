package model;

public class Adresse {
    private String rue;
    private String ville;
    private String codePostal;
    private String pays;
    private int distanceCentre;

    // Getters et setters
    public String getRue() { return rue; }
    public void setRue(String rue) { this.rue = rue; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }

    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }

    public int getDistanceCentre() { return distanceCentre; }
    public void setDistanceCentre(int distanceCentre) { this.distanceCentre = distanceCentre; }
}
