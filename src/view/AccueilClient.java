package view;

import dao.LogementDAOImpl;
import dao.UtilisateurDAOImpl;
import model.Logement;
import model.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AccueilClient extends JFrame {

    private JPanel logementsPanel;
    private Utilisateur utilisateur;

    public AccueilClient(String identifiant) {
        setTitle("Espace Client - Booking");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Récupération de l'utilisateur connecté
        this.utilisateur = new UtilisateurDAOImpl().getByIdentifiant(identifiant);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // ------ BARRE DU HAUT ------
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(90, 130, 160));
        topBar.setPreferredSize(new Dimension(0, 60));

        JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.addActionListener(e -> {
            dispose();
            new FenetreConnexion();
        });

        JLabel logo = new JLabel("Booking", JLabel.LEFT);
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);
        logo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));

        JLabel userBox = new JLabel("Connecté : " + identifiant);
        userBox.setFont(new Font("Arial", Font.PLAIN, 16));
        userBox.setForeground(Color.WHITE);
        userBox.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(btnDeconnexion);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        centerPanel.add(logo);

        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(centerPanel, BorderLayout.CENTER);
        topBar.add(userBox, BorderLayout.EAST);
        mainPanel.add(topBar, BorderLayout.NORTH);

        // ------ ZONE RECHERCHE ------
        JPanel recherchePanel = new JPanel();
        recherchePanel.setBackground(new Color(230, 240, 250));
        recherchePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20),
                BorderFactory.createLineBorder(new Color(180, 200, 230))
        ));
        recherchePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        JTextField destinationField = new JTextField("Où allez-vous ?", 15);
        destinationField.setForeground(Color.GRAY);
        destinationField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (destinationField.getText().equals("Où allez-vous ?")) {
                    destinationField.setText("");
                    destinationField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (destinationField.getText().isEmpty()) {
                    destinationField.setText("Où allez-vous ?");
                    destinationField.setForeground(Color.GRAY);
                }
            }
        });

        JSpinner dateArrivee = new JSpinner(new SpinnerDateModel());
        dateArrivee.setEditor(new JSpinner.DateEditor(dateArrivee, "dd/MM/yyyy"));
        JSpinner dateDepart = new JSpinner(new SpinnerDateModel());
        dateDepart.setEditor(new JSpinner.DateEditor(dateDepart, "dd/MM/yyyy"));
        JSpinner nbPersonnes = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        JSpinner nbChambres = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        JButton btnRechercher = new JButton("Rechercher");
        btnRechercher.setBackground(new Color(0, 102, 204));
        btnRechercher.setForeground(Color.WHITE);

        recherchePanel.add(destinationField);
        recherchePanel.add(new JLabel("Arrivée:"));
        recherchePanel.add(dateArrivee);
        recherchePanel.add(new JLabel("Départ:"));
        recherchePanel.add(dateDepart);
        recherchePanel.add(new JLabel("Personnes:"));
        recherchePanel.add(nbPersonnes);
        recherchePanel.add(new JLabel("Chambres:"));
        recherchePanel.add(nbChambres);
        recherchePanel.add(btnRechercher);

        mainPanel.add(recherchePanel, BorderLayout.BEFORE_FIRST_LINE);

        // ------ ZONE LOGEMENTS ------
        logementsPanel = new JPanel();
        logementsPanel.setLayout(new BoxLayout(logementsPanel, BoxLayout.Y_AXIS));
        logementsPanel.setBackground(new Color(245, 245, 245));
        logementsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JScrollPane scrollPane = new JScrollPane(logementsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Récupération initiale
        afficherLogements(new LogementDAOImpl().getAllLogementsAvecImages());

        // ------ Action de recherche ------
        btnRechercher.addActionListener(e -> {
            String ville = destinationField.getText().trim();
            int nbPersonnesValue = (int) nbPersonnes.getValue();
            int nbChambresValue = (int) nbChambres.getValue();

            if (!ville.isEmpty() && !ville.equals("Où allez-vous ?")) {
                List<Logement> resultats = new LogementDAOImpl().rechercherLogements(ville, nbPersonnesValue, nbChambresValue);
                afficherLogements(resultats);
            } else {
                afficherLogements(new LogementDAOImpl().getAllLogementsAvecImages());
            }
        });

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void afficherLogements(List<Logement> logements) {
        logementsPanel.removeAll();
        for (Logement logement : logements) {
            logementsPanel.add(new CarteLogement(logement, utilisateur));
            logementsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        logementsPanel.revalidate();
        logementsPanel.repaint();
    }
}