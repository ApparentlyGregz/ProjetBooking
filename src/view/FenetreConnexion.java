package view;

import controller.UtilisateurController;
import model.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FenetreConnexion extends JFrame {
    private JTextField identifiantField;
    private JPasswordField motDePasseField;
    private JButton connexionButton, inscriptionButton;
    private UtilisateurController controller;

    public FenetreConnexion() {
        controller = new UtilisateurController();

        setTitle("Booking - Connexion / Inscription");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Thème gris bleu
        Color fond = new Color(220, 100, 240);
        Color bouton = new Color(100, 140, 180);
        Color texte = Color.WHITE;

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(fond);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titre = new JLabel("Bienvenue sur Booking", JLabel.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(titre, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Identifiant :"), gbc);
        gbc.gridx = 1;
        identifiantField = new JTextField();
        panel.add(identifiantField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Mot de passe :"), gbc);
        gbc.gridx = 1;
        motDePasseField = new JPasswordField();
        panel.add(motDePasseField, gbc);

        connexionButton = new JButton("Se connecter");
        connexionButton.setBackground(bouton);
        connexionButton.setForeground(texte);
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(connexionButton, gbc);

        inscriptionButton = new JButton("S'inscrire");
        inscriptionButton.setBackground(bouton);
        inscriptionButton.setForeground(texte);
        gbc.gridx = 1;
        panel.add(inscriptionButton, gbc);

        add(panel);

        connexionButton.addActionListener(e -> {
            String id = identifiantField.getText();
            String mdp = new String(motDePasseField.getPassword());
            if (controller.connexion(id, mdp)) {
                JOptionPane.showMessageDialog(this, "Connexion réussie !");
                // TODO : ouvrir l'espace client/admin selon le type
            } else {
                JOptionPane.showMessageDialog(this, "Identifiants invalides.");
            }
        });

        inscriptionButton.addActionListener(e -> {
            new FenetreInscription();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FenetreConnexion::new);
    }
}
