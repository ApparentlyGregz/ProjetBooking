package dao;

import model.Logement;
import outils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogementDAOImpl implements LogementDAO {

    @Override
    public List<Logement> getAllLogementsAvecImages() {
        List<Logement> logements = new ArrayList<>();
        String sql = "SELECT l.id, l.nom, l.description, l.superficie, l.nb_personnes_max, l.nombre_etoiles, " +
                "l.date_creation, l.wifi, l.clim, l.parking, l.type, i.url_image, " +
                "a.rue, a.ville, a.code_postal, a.pays, a.distance_centre, l.nb_chambres " +  // Assurez-vous que l.nb_chambres est bien sélectionné
                "FROM logement l " +
                "LEFT JOIN logement_image i ON l.id = i.logement_id " +
                "LEFT JOIN adresse a ON l.id = a.logement_id " +
                "ORDER BY l.id";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            int currentId = -1;
            Logement logement = null;

            while (rs.next()) {
                int id = rs.getInt("id");
                if (id != currentId) {
                    logement = new Logement();
                    logement.setRue(rs.getString("rue"));
                    logement.setVille(rs.getString("ville"));
                    logement.setCodePostal(rs.getString("code_postal"));
                    logement.setPays(rs.getString("pays"));
                    logement.setDistanceCentre(rs.getInt("distance_centre"));

                    logement.setId(id);
                    logement.setNom(rs.getString("nom"));
                    logement.setDescription(rs.getString("description"));
                    logement.setSuperficie(rs.getInt("superficie"));
                    logement.setNbPersonnesMax(rs.getInt("nb_personnes_max"));
                    logement.setNombreEtoiles(rs.getInt("nombre_etoiles"));
                    logement.setDateCreation(rs.getDate("date_creation"));
                    logement.setHasWifi(rs.getInt("wifi") == 1);
                    logement.setHasClim(rs.getInt("clim") == 1);
                    logement.setHasParking(rs.getInt("parking") == 1);
                    logement.setNbChambres(rs.getInt("nb_chambres"));  // Récupère le nombre de chambres
                    logements.add(logement);
                    currentId = id;
                }
                if (logement != null) {
                    String image = rs.getString("url_image");
                    if (image != null) {
                        logement.addImage(image);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logements;
    }


    @Override
    public int ajouterLogement(Logement logement) {
        String insertLogementSQL = "INSERT INTO logement (nom, description, superficie, nb_personnes_max, nombre_etoiles, date_creation, wifi, clim, parking, type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertImageSQL = "INSERT INTO logement_image (logement_id, url_image) VALUES (?, ?)";

        String insertAdresseSQL = "INSERT INTO adresse (logement_id, rue, ville, code_postal, pays, distance_centre) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmtLogement = conn.prepareStatement(insertLogementSQL, Statement.RETURN_GENERATED_KEYS)) {

            // Insertion du logement
            stmtLogement.setString(1, logement.getNom());
            stmtLogement.setString(2, logement.getDescription());
            stmtLogement.setInt(3, logement.getSuperficie());
            stmtLogement.setInt(4, logement.getNbPersonnesMax());
            stmtLogement.setInt(5, logement.getNombreEtoiles());
            stmtLogement.setDate(6, new java.sql.Date(logement.getDateCreation().getTime()));
            stmtLogement.setBoolean(7, logement.hasWifi());
            stmtLogement.setBoolean(8, logement.hasClim());
            stmtLogement.setBoolean(9, logement.hasParking());
            stmtLogement.setString(10, logement.getType());

            int rows = stmtLogement.executeUpdate();
            if (rows == 0) return -1;

            ResultSet generatedKeys = stmtLogement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int logementId = generatedKeys.getInt(1);

                // Insertion des images
                if (logement.getImages() != null && !logement.getImages().isEmpty()) {
                    try (PreparedStatement stmtImage = conn.prepareStatement(insertImageSQL)) {
                        for (String image : logement.getImages()) {
                            stmtImage.setInt(1, logementId);
                            stmtImage.setString(2, image);
                            stmtImage.addBatch();
                        }
                        stmtImage.executeBatch();
                    }
                }

                // Insertion de l'adresse
                try (PreparedStatement stmtAdresse = conn.prepareStatement(insertAdresseSQL)) {
                    stmtAdresse.setInt(1, logementId);
                    stmtAdresse.setString(2, logement.getRue());
                    stmtAdresse.setString(3, logement.getVille());
                    stmtAdresse.setString(4, logement.getCodePostal());
                    stmtAdresse.setString(5, logement.getPays());
                    stmtAdresse.setInt(6, logement.getDistanceCentre());
                    stmtAdresse.executeUpdate();
                }

                return logementId; // ✅ Retourner l'ID du logement inséré
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // ❌ Retourner -1 si échec
    }


    public boolean supprimerLogement(int idLogement) {
        String sql = "DELETE FROM logement WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLogement);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modifierLogement(Logement logement) {
        String sqlLogement = "UPDATE logement SET nom=?, description=?, superficie=?, nb_personnes_max=?, nombre_etoiles=?, wifi=?, clim=?, parking=?, type=?, nb_chambres=? WHERE id=?";
        String sqlAdresse = "UPDATE adresse SET rue=?, ville=?, code_postal=?, pays=?, distance_centre=? WHERE logement_id=?";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false); // pour garantir cohérence

            try (
                    PreparedStatement stmtLogement = conn.prepareStatement(sqlLogement);
                    PreparedStatement stmtAdresse = conn.prepareStatement(sqlAdresse)
            ) {
                // --- Mise à jour du logement ---
                stmtLogement.setString(1, logement.getNom());
                stmtLogement.setString(2, logement.getDescription());
                stmtLogement.setInt(3, logement.getSuperficie());
                stmtLogement.setInt(4, logement.getNbPersonnesMax());
                stmtLogement.setInt(5, logement.getNombreEtoiles());
                stmtLogement.setBoolean(6, logement.hasWifi());
                stmtLogement.setBoolean(7, logement.hasClim());
                stmtLogement.setBoolean(8, logement.hasParking());
                stmtLogement.setString(9, logement.getType());
                stmtLogement.setInt(10, logement.getNbChambres());  // Mise à jour du nombre de chambres
                stmtLogement.setInt(11, logement.getId());
                stmtLogement.executeUpdate();

                // --- Mise à jour de l'adresse associée ---
                stmtAdresse.setString(1, logement.getRue());
                stmtAdresse.setString(2, logement.getVille());
                stmtAdresse.setString(3, logement.getCodePostal());
                stmtAdresse.setString(4, logement.getPays());
                stmtAdresse.setInt(5, logement.getDistanceCentre());
                stmtAdresse.setInt(6, logement.getId());
                stmtAdresse.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback(); // rollback si erreur
                ex.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public List<Logement> getLogementsParVille(String ville) {
        List<Logement> logements = new ArrayList<>();
        String sql = "SELECT l.*, i.url_image, a.rue, a.ville, a.code_postal, a.pays, a.distance_centre " +
                "FROM logement l " +
                "LEFT JOIN logement_image i ON l.id = i.logement_id " +
                "LEFT JOIN adresse a ON l.id = a.logement_id " +
                "WHERE a.ville LIKE ? " +
                "ORDER BY l.id";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + ville + "%");

            ResultSet rs = stmt.executeQuery();
            int currentId = -1;
            Logement logement = null;

            while (rs.next()) {
                int id = rs.getInt("id");
                if (id != currentId) {
                    logement = new Logement();
                    logement.setId(id);
                    logement.setNom(rs.getString("nom"));
                    logement.setDescription(rs.getString("description"));
                    logement.setSuperficie(rs.getInt("superficie"));
                    logement.setNbPersonnesMax(rs.getInt("nb_personnes_max"));
                    logement.setNombreEtoiles(rs.getInt("nombre_etoiles"));
                    logement.setNbChambres(rs.getInt("nb_chambres")); // Ajoutez cette ligne pour récupérer le nombre de chambres

                    logement.setDateCreation(rs.getDate("date_creation"));
                    logement.setHasWifi(rs.getInt("wifi") == 1);
                    logement.setHasClim(rs.getInt("clim") == 1);
                    logement.setHasParking(rs.getInt("parking") == 1);
                    logement.setType(rs.getString("type"));
                    logement.setRue(rs.getString("rue"));
                    logement.setVille(rs.getString("ville"));
                    logement.setCodePostal(rs.getString("code_postal"));
                    logement.setPays(rs.getString("pays"));
                    logement.setDistanceCentre(rs.getInt("distance_centre"));

                    logements.add(logement);
                    currentId = id;
                }
                if (logement != null) {
                    String image = rs.getString("url_image");
                    if (image != null) {
                        logement.addImage(image);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return logements;
    }



    @Override
    public List<Logement> rechercherLogements(String ville, int nbPersonnes, int nbChambres) {
        List<Logement> logements = new ArrayList<>();

        // Construire la requête SQL
        String sql = "SELECT l.*, i.url_image, a.rue, a.ville, a.code_postal, a.pays, a.distance_centre " +
                "FROM logement l " +
                "LEFT JOIN logement_image i ON l.id = i.logement_id " +
                "LEFT JOIN adresse a ON l.id = a.logement_id " +
                "WHERE l.nb_personnes_max >= ? AND l.nb_chambres >= ?";  // Applique le filtre sur le nombre de personnes et de chambres

        // Ajouter la condition de la ville si elle est renseignée
        if (ville != null && !ville.isEmpty()) {
            sql += " AND a.ville LIKE ?";
        }

        sql += " ORDER BY l.id";  // Trier les résultats par ID (vous pouvez changer ce critère si nécessaire)

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Appliquer les filtres sur le nombre de personnes et le nombre de chambres
            stmt.setInt(1, nbPersonnes);
            stmt.setInt(2, nbChambres);

            // Ajouter la condition sur la ville si nécessaire
            if (ville != null && !ville.isEmpty()) {
                stmt.setString(3, "%" + ville + "%");
            }

            ResultSet rs = stmt.executeQuery();

            int currentId = -1;
            Logement logement = null;

            while (rs.next()) {
                int id = rs.getInt("id");
                if (id != currentId) {
                    logement = new Logement();
                    logement.setId(id);
                    logement.setNom(rs.getString("nom"));
                    logement.setDescription(rs.getString("description"));
                    logement.setSuperficie(rs.getInt("superficie"));
                    logement.setNbPersonnesMax(rs.getInt("nb_personnes_max"));
                    logement.setNombreEtoiles(rs.getInt("nombre_etoiles"));
                    logement.setHasWifi(rs.getBoolean("wifi"));
                    logement.setHasClim(rs.getBoolean("clim"));
                    logement.setHasParking(rs.getBoolean("parking"));
                    logement.setType(rs.getString("type"));
                    logement.setDateCreation(rs.getDate("date_creation"));
                    logement.setRue(rs.getString("rue"));
                    logement.setVille(rs.getString("ville"));
                    logement.setCodePostal(rs.getString("code_postal"));
                    logement.setPays(rs.getString("pays"));
                    logement.setDistanceCentre(rs.getInt("distance_centre"));

                    logements.add(logement);
                    currentId = id;
                }
                if (logement != null) {
                    String image = rs.getString("url_image");
                    if (image != null) {
                        logement.addImage(image);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logements;
    }

    // Méthode de recherche des logements avec les filtres
    private List<Logement> rechercherLogementsAvecFiltres(String ville, int distance, double prixMin, double prixMax, boolean wifi, boolean clim, boolean parking) {
        List<Logement> logements = new ArrayList<>();

        // Construire la requête SQL avec les filtres
        String sql = "SELECT l.*, i.url_image, a.rue, a.ville, a.code_postal, a.pays, a.distance_centre " +
                "FROM logement l " +
                "LEFT JOIN logement_image i ON l.id = i.logement_id " +
                "LEFT JOIN adresse a ON l.id = a.logement_id " +
                "WHERE l.nb_personnes_max > 0 ";

        // Filtrage par ville
        if (ville != null && !ville.isEmpty()) {
            sql += "AND a.ville LIKE ? ";
        }

        // Filtrage par distance du centre
        if (distance > 0) {
            sql += "AND a.distance_centre <= ? ";
        }

        // Filtrage par prix
        if (prixMin >= 0 && prixMax >= prixMin) {
            sql += "AND l.prix_nuit BETWEEN ? AND ? ";
        }

        // Filtrage par wifi, clim, parking
        if (wifi) {
            sql += "AND l.wifi = 1 ";
        }
        if (clim) {
            sql += "AND l.clim = 1 ";
        }
        if (parking) {
            sql += "AND l.parking = 1 ";
        }

        // Exécution de la requête
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int index = 1;
            // Ajouter les paramètres de la requête pour chaque filtre
            if (ville != null && !ville.isEmpty()) {
                stmt.setString(index++, "%" + ville + "%");
            }
            if (distance > 0) {
                stmt.setInt(index++, distance);
            }
            if (prixMin >= 0 && prixMax >= prixMin) {
                stmt.setDouble(index++, prixMin);
                stmt.setDouble(index++, prixMax);
            }
            if (wifi) {
                stmt.setBoolean(index++, true);
            }
            if (clim) {
                stmt.setBoolean(index++, true);
            }
            if (parking) {
                stmt.setBoolean(index++, true);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Logement logement = new Logement();
                logement.setId(rs.getInt("id"));
                logement.setNom(rs.getString("nom"));
                logement.setDescription(rs.getString("description"));
                logement.setSuperficie(rs.getInt("superficie"));
                logement.setNbPersonnesMax(rs.getInt("nb_personnes_max"));
                logement.setNombreEtoiles(rs.getInt("nombre_etoiles"));
                logement.setDateCreation(rs.getDate("date_creation"));
                logement.setHasWifi(rs.getInt("wifi") == 1);
                logement.setHasClim(rs.getInt("clim") == 1);
                logement.setHasParking(rs.getInt("parking") == 1);
                logement.setType(rs.getString("type"));
                logement.setRue(rs.getString("rue"));
                logement.setVille(rs.getString("ville"));
                logement.setCodePostal(rs.getString("code_postal"));
                logement.setPays(rs.getString("pays"));
                logement.setDistanceCentre(rs.getInt("distance_centre"));

                logements.add(logement);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logements;
    }



}