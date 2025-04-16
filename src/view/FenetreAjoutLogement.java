package view;

import dao.LogementDAO;
import dao.LogementDAOImpl;
import model.Logement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;

public class FenetreAjoutLogement extends JFrame {

    public FenetreAjoutLogement(JFrame parent) {
        setTitle("Ajouter un logement");
        setSize(400, 550);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nomField = new JTextField();
        JTextArea descField = new JTextArea(3, 20);
        JTextField superficieField = new JTextField();
        JTextField nbPersField = new JTextField();
        JTextField etoilesField = new JTextField();

        String[] types = { "Villa", "Appartement", "Chalet", "Hôtel", "Studio" };
        JComboBox<String> typeBox = new JComboBox<>(types);

        JCheckBox wifiBox = new JCheckBox("WiFi");
        JCheckBox climBox = new JCheckBox("Climatisation");
        JCheckBox parkingBox = new JCheckBox("Parking");

        JButton validerBtn = new JButton("Valider");

        panel.add(new JLabel("Nom :"));
        panel.add(nomField);
        panel.add(new JLabel("Description :"));
        panel.add(new JScrollPane(descField));
        panel.add(new JLabel("Superficie :"));
        panel.add(superficieField);
        panel.add(new JLabel("Nb personnes max :"));
        panel.add(nbPersField);
        panel.add(new JLabel("Nombre d'étoiles :"));
        panel.add(etoilesField);
        panel.add(new JLabel("Type de logement :"));
        panel.add(typeBox);

        panel.add(wifiBox);
        panel.add(climBox);
        panel.add(parkingBox);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(validerBtn);

        add(panel);

        validerBtn.addActionListener((ActionEvent e) -> {
            try {
                if (superficieField.getText().isEmpty() || nbPersField.getText().isEmpty() || etoilesField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs numériques.", "Champs manquants", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Logement logement = new Logement();
                logement.setNom(nomField.getText());
                logement.setDescription(descField.getText());
                logement.setSuperficie(Integer.parseInt(superficieField.getText()));
                logement.setNbPersonnesMax(Integer.parseInt(nbPersField.getText()));
                logement.setNombreEtoiles(Integer.parseInt(etoilesField.getText()));
                logement.setDateCreation(new java.sql.Date(new Date().getTime()));
                logement.setHasWifi(wifiBox.isSelected());
                logement.setHasClim(climBox.isSelected());
                logement.setHasParking(parkingBox.isSelected());
                logement.setType((String) typeBox.getSelectedItem());

                LogementDAO dao = new LogementDAOImpl();
                boolean success = dao.ajouterLogement(logement);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Logement ajouté avec succès !");
                    dispose();
                    parent.dispose();
                    new AccueilAdmin(logement.getNom());
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valeurs numériques invalides.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}