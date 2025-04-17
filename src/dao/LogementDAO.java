package dao;

import model.Logement;
import java.util.List;
public interface LogementDAO {
    List<Logement> getAllLogementsAvecImages();
    boolean ajouterLogement(Logement logement);
    boolean modifierLogement(Logement logement);
    List<Logement> getLogementsParVille(String ville);

    // Ajoute la méthode rechercherLogements
    List<Logement> rechercherLogements(String ville, int nbPersonnes, int nbChambres);
}

