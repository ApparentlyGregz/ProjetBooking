package dao;

import model.Utilisateur;

public interface UtilisateurDAO {
    boolean checkLogin(String identifiant, String mdp);
    boolean insert(Utilisateur utilisateur);
    Utilisateur getByIdentifiant(String identifiant);
}
