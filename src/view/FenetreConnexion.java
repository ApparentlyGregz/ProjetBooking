package view;

import controller.UtilisateurController;
import model.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class FenetreConnexion extends JFrame {

    private JTextField identifiantField;
    private JPasswordField motDePasseField;
    private UtilisateurController controller;

    public FenetreConnexion() {
        controller = new UtilisateurController();

        setTitle("Connexion - Booking");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Couleurs
        Color fond = new Color(220, 230, 240);
        Color bloc = new Color(255, 255, 255);
        Color bouton = new Color(100, 140, 180);
        Color texte = Color.WHITE;

        // Partie centre (formulaire)
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(fond);
        centerPanel.setLayout(new GridBagLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(bloc);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int y = 0;

        JLabel titre = new JLabel("Connexion à Booking");
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        formPanel.add(titre, gbc);
        y++;

        identifiantField = new JTextField(20);
        motDePasseField = new JPasswordField(20);
        JButton connexionBtn = new JButton("Se connecter");
        JButton inscriptionBtn = new JButton("Créer un compte");
        JLabel messageErreur = new JLabel("");
        messageErreur.setForeground(Color.RED);

        // Labels alignés à gauche des champs
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Identifiant :"), gbc);
        gbc.gridx = 1;
        formPanel.add(identifiantField, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Mot de passe :"), gbc);
        gbc.gridx = 1;
        formPanel.add(motDePasseField, gbc);
        y++;

        // Boutons
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        connexionBtn.setBackground(bouton);
        connexionBtn.setForeground(texte);
        getRootPane().setDefaultButton(connexionBtn);

        formPanel.add(connexionBtn, gbc);
        y++;

        gbc.gridy = y;
        inscriptionBtn.setBackground(bouton);
        inscriptionBtn.setForeground(texte);
        formPanel.add(inscriptionBtn, gbc);
        y++;

        gbc.gridy = y;
        formPanel.add(messageErreur, gbc);

        centerPanel.add(formPanel);
        add(centerPanel, BorderLayout.CENTER);

        // Footer
        JLabel signature = new JLabel("by Marino et Gregz");
        signature.setFont(new Font("Arial", Font.PLAIN, 12));
        signature.setForeground(Color.DARK_GRAY);
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(fond);
        footer.add(signature);
        add(footer, BorderLayout.SOUTH);

        // Actions
        connexionBtn.addActionListener(e -> {
            String id = identifiantField.getText();
            String mdp = new String(motDePasseField.getPassword());

            if (controller.connexion(id, mdp)) {
                Utilisateur u = controller.getUtilisateurParIdentifiant(id);
                dispose();
                if (u.getTypeCompte().equals("client")) {
                    new AccueilClient(u.getIdentifiant());
                } else if (u.getTypeCompte().equals("admin")) {
                    new AccueilAdmin(u.getIdentifiant());
                }
            } else {
                messageErreur.setText("❌ Identifiant ou mot de passe incorrect.");
            }
        });

        inscriptionBtn.addActionListener(e -> {
            dispose();
            new FenetreInscription();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FenetreConnexion::new);
    }
}
