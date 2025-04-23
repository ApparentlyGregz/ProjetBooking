package model;

public class Utilisateur {
    private int id;
    private String identifiant;
    private String mdp;
    private String email;
    private String typeCompte;
    private String nom;
    private String prenom;
    private String telephone;
    private int ancienClient;

    public Utilisateur() {}

    public Utilisateur(String identifiant, String mdp, String email, String typeCompte) {
        this.identifiant = identifiant;
        this.mdp = mdp;
        this.email = email;
        this.typeCompte = typeCompte;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIdentifiant() { return identifiant; }
    public void setIdentifiant(String identifiant) { this.identifiant = identifiant; }

    public String getMdp() { return mdp; }
    public void setMdp(String mdp) { this.mdp = mdp; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTypeCompte() { return typeCompte; }
    public void setTypeCompte(String typeCompte) { this.typeCompte = typeCompte; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public int getAncienClient() { return ancienClient; }
    public void setAncienClient(int ancienClient) { this.ancienClient = ancienClient; }
}
