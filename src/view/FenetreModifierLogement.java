package view;

import dao.LogementDAO;
import dao.LogementDAOImpl;
import model.Logement;

import javax.swing.*;
import java.awt.*;

public class FenetreModifierLogement extends JFrame {

    public FenetreModifierLogement(Logement logement, JFrame parent) {
        setTitle("Modifier le logement");
        setSize(400, 600);
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
        JTextField typeField = new JTextField(logement.getType());

        JCheckBox wifiBox = new JCheckBox("WiFi", logement.hasWifi());
        JCheckBox climBox = new JCheckBox("Climatisation", logement.hasClim());
        JCheckBox parkingBox = new JCheckBox("Parking", logement.hasParking());

        JButton validerBtn = new JButton("Valider les modifications");

        panel.add(new JLabel("Nom :")); panel.add(nomField);
        panel.add(new JLabel("Description :")); panel.add(new JScrollPane(descField));
        panel.add(new JLabel("Superficie :")); panel.add(superficieField);
        panel.add(new JLabel("Nb personnes max :")); panel.add(nbPersField);
        panel.add(new JLabel("Nombre d'étoiles :")); panel.add(etoilesField);
        panel.add(new JLabel("Type :")); panel.add(typeField);
        panel.add(wifiBox); panel.add(climBox); panel.add(parkingBox);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(validerBtn);

        add(panel);

        validerBtn.addActionListener(e -> {
            try {
                logement.setNom(nomField.getText());
                logement.setDescription(descField.getText());
                logement.setSuperficie(Integer.parseInt(superficieField.getText()));
                logement.setNbPersonnesMax(Integer.parseInt(nbPersField.getText()));
                logement.setNombreEtoiles(Integer.parseInt(etoilesField.getText()));
                logement.setType(typeField.getText());
                logement.setHasWifi(wifiBox.isSelected());
                logement.setHasClim(climBox.isSelected());
                logement.setHasParking(parkingBox.isSelected());

                LogementDAO dao = new LogementDAOImpl();
                boolean success = dao.modifierLogement(logement);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Logement modifié avec succès !");
                    dispose();
                    parent.dispose();
                    new AccueilAdmin("admin");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la modification.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Champs numériques invalides.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
