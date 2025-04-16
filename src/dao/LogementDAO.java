package dao;

import model.Logement;
import java.util.List;

public interface LogementDAO {
    List<Logement> getAllLogementsAvecImages();
    boolean ajouterLogement(Logement logement);
}
