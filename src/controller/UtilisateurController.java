package controller;

import dao.UtilisateurDAO;
import dao.UtilisateurDAOImpl;
import model.Utilisateur;

public class UtilisateurController {
    private UtilisateurDAO dao = new UtilisateurDAOImpl();

    public boolean connexion(String identifiant, String mdp) {
        return dao.checkLogin(identifiant, mdp);
    }

    public boolean inscription(Utilisateur utilisateur) {
        return dao.insert(utilisateur);
    }
}
