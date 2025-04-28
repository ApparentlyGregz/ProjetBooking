package controller;

import dao.UtilisateurDAO;
import dao.UtilisateurDAOImpl;
import model.Utilisateur;

// controleur desopérations liées aux utilisateurs
public class UtilisateurController {
    // DAO pour accéder aux données des utilisateurs
    private UtilisateurDAO dao = new UtilisateurDAOImpl();

    // vérification identifiants de connexion
    public boolean connexion(String identifiant, String mdp) {
        return dao.checkLogin(identifiant, mdp);
    }

    // inscription nouvel utilisateur dans la base
    public boolean inscription(Utilisateur utilisateur) {
        return dao.insert(utilisateur);
    }

    // recuperation de l'utilisateur à partir de son identifiant
    public Utilisateur getUtilisateurParIdentifiant(String id) {
        return dao.getByIdentifiant(id);
    }
}
