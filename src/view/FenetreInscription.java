package view;

import controller.UtilisateurController;
import model.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class FenetreInscription extends JFrame {
    private JTextField identifiantField, emailField, nomField, prenomField, telField;
    private JPasswordField mdpField;
    private JButton inscrireButton;
    private UtilisateurController controller;

    public FenetreInscription() {
        controller = new UtilisateurController();

        setTitle("Créer un compte - Booking");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Identifiant :"));
        identifiantField = new JTextField();
        panel.add(identifiantField);

        panel.add(new JLabel("Mot de passe :"));
        mdpField = new JPasswordField();
        panel.add(mdpField);

        panel.add(new JLabel("Email :"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Nom :"));
        nomField = new JTextField();
        panel.add(nomField);

        panel.add(new JLabel("Prénom :"));
        prenomField = new JTextField();
        panel.add(prenomField);

        panel.add(new JLabel("Téléphone :"));
        telField = new JTextField();
        panel.add(telField);

        inscrireButton = new JButton("Créer le compte");
        panel.add(new JLabel()); // pour l'espacement
        panel.add(inscrireButton);

        add(panel);

        inscrireButton.addActionListener(e -> {
            String id = identifiantField.getText();
            String mdp = new String(mdpField.getPassword());
            String email = emailField.getText();
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String tel = telField.getText();

            if (id.isEmpty() || mdp.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Identifiant, mot de passe et email sont obligatoires.");
                return;
            }

            Utilisateur u = new Utilisateur(id, mdp, email, "nouveau_client");
            u.setNom(nom);
            u.setPrenom(prenom);
            u.setTelephone(tel);

            boolean success = controller.inscription(u);
            if (success) {
                JOptionPane.showMessageDialog(this, "Compte créé avec succès ! Vous pouvez vous connecter.");
                dispose(); // Fermer la fenêtre d'inscription
                new FenetreConnexion(); // <-- On ouvre la fenêtre de connexion
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la création du compte. Vérifiez les infos.");
            }
        });

        setVisible(true);
    }
}
