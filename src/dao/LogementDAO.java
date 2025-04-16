package dao;

import model.Logement;
import java.util.List;

public interface LogementDAO {
    boolean modifierLogement(Logement logement);

    List<Logement> getAllLogementsAvecImages();
    boolean ajouterLogement(Logement logement);
}
