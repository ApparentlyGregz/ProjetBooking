package view;

import dao.LogementDAO;
import dao.LogementDAOImpl;
import model.Logement;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class FenetreModifierLogement extends JFrame {

    public FenetreModifierLogement(Logement logement, JFrame parent) {
        setTitle("Modifier le logement");
        setSize(400, 700);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nomField = new JTextField(logement.getNom());
        JTextArea descField = new JTextArea(logement.getDescription());
        JTextField superficieField = new JTextField(String.valueOf(logement.getSuperficie()));
        JTextField nbPersField = new JTextField(String.valueOf(logement.getNbPersonnesMax()));
        JTextField etoilesField = new JTextField(String.valueOf(logement.getNombreEtoiles()));

        String[] types = { "Villa", "Appartement", "Chalet", "Hôtel", "Studio" };
        JComboBox<String> typeBox = new JComboBox<>(types);
        if (logement.getType() != null) {
            typeBox.setSelectedItem(logement.getType());
        } else {
            typeBox.setSelectedIndex(0);
        }

        JCheckBox wifiBox = new JCheckBox("WiFi", logement.hasWifi());
        JCheckBox climBox = new JCheckBox("Climatisation", logement.hasClim());
        JCheckBox parkingBox = new JCheckBox("Parking", logement.hasParking());

        // Champs adresse
        JTextField rueField = new JTextField(logement.getRue());
        JTextField villeField = new JTextField(logement.getVille());
        JTextField codePostalField = new JTextField(logement.getCodePostal());
        JTextField paysField = new JTextField(logement.getPays());
        JTextField distanceField = new JTextField(String.valueOf(logement.getDistanceCentre()));

        JButton validerBtn = new JButton("Enregistrer les modifications");

        panel.add(new JLabel("Nom :")); panel.add(nomField);
        panel.add(new JLabel("Description :")); panel.add(new JScrollPane(descField));
        panel.add(new JLabel("Superficie :")); panel.add(superficieField);
        panel.add(new JLabel("Nb personnes max :")); panel.add(nbPersField);
        panel.add(new JLabel("Nombre d'étoiles :")); panel.add(etoilesField);
        panel.add(new JLabel("Type :")); panel.add(typeBox);

        panel.add(wifiBox);
        panel.add(climBox);
        panel.add(parkingBox);

        // Ajout des champs d’adresse
        panel.add(new JLabel("Rue :")); panel.add(rueField);
        panel.add(new JLabel("Ville :")); panel.add(villeField);
        panel.add(new JLabel("Code postal :")); panel.add(codePostalField);
        panel.add(new JLabel("Pays :")); panel.add(paysField);
        panel.add(new JLabel("Distance du centre (km) :")); panel.add(distanceField);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(validerBtn);

        add(panel);

        validerBtn.addActionListener(e -> {
            try {
                // Validation du type
                String selectedType = (String) typeBox.getSelectedItem();
                if (selectedType == null || selectedType.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez sélectionner un type de logement.");
                    return;
                }

                logement.setNom(nomField.getText());
                logement.setDescription(descField.getText());
                logement.setSuperficie(Integer.parseInt(superficieField.getText()));
                logement.setNbPersonnesMax(Integer.parseInt(nbPersField.getText()));
                logement.setNombreEtoiles(Integer.parseInt(etoilesField.getText()));
                logement.setHasWifi(wifiBox.isSelected());
                logement.setHasClim(climBox.isSelected());
                logement.setHasParking(parkingBox.isSelected());
                logement.setType(selectedType);
                logement.setDateCreation(new java.sql.Date(new Date().getTime()));

                // Adresse
                logement.setRue(rueField.getText());
                logement.setVille(villeField.getText());
                logement.setCodePostal(codePostalField.getText());
                logement.setPays(paysField.getText());
                logement.setDistanceCentre(Integer.parseInt(distanceField.getText()));

                LogementDAO dao = new LogementDAOImpl();
                boolean success = dao.modifierLogement(logement);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Logement modifié avec succès !");
                    dispose();
                    parent.dispose();
                    new AccueilAdmin(logement.getNom());
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la modification.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valeurs numériques invalides.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
