package view;

import dao.LogementDAO;
import dao.LogementDAOImpl;
import model.Logement;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class AccueilAdmin extends JFrame {

    private JPanel logementsPanel;

    public AccueilAdmin(String identifiant) {
        setTitle("Espace Administrateur - Booking");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // --- TopBar avec déconnexion + titre + actions ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(180, 200, 230));
        topBar.setPreferredSize(new Dimension(0, 60));

        JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.addActionListener(e -> {
            dispose();
            new FenetreConnexion();
        });

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(btnDeconnexion);

        JLabel logo = new JLabel("Booking Admin");
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setForeground(Color.DARK_GRAY);
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.add(logo);

        JButton btnAdd = new JButton("➕ Ajouter");
        btnAdd.addActionListener(e -> new FenetreAjoutLogement(this));
        JButton btnUsers = new JButton("👤 Utilisateurs");

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(btnAdd);
        rightPanel.add(btnUsers);

        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(centerPanel, BorderLayout.CENTER);
        topBar.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(topBar, BorderLayout.NORTH);

        // --- Barre de recherche ---
        JPanel searchBar = new JPanel();
        searchBar.setLayout(new FlowLayout(FlowLayout.CENTER));
        searchBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        searchBar.setBackground(new Color(230, 235, 245));

        JTextField villeField = new JTextField("Où allez-vous ?", 20);
        villeField.setForeground(Color.GRAY);
        villeField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (villeField.getText().equals("Où allez-vous ?")) {
                    villeField.setText("");
                    villeField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (villeField.getText().isEmpty()) {
                    villeField.setText("Où allez-vous ?");
                    villeField.setForeground(Color.GRAY);
                }
            }
        });

        JButton searchBtn = new JButton("Rechercher");
        searchBar.add(villeField);
        searchBar.add(searchBtn);

        mainPanel.add(searchBar, BorderLayout.BEFORE_FIRST_LINE);

        // --- Zone des logements ---
        logementsPanel = new JPanel();
        logementsPanel.setLayout(new BoxLayout(logementsPanel, BoxLayout.Y_AXIS));
        logementsPanel.setBackground(new Color(245, 245, 250));
        logementsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JScrollPane scrollPane = new JScrollPane(logementsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Chargement initial des logements ---
        LogementDAO logementDAO = new LogementDAOImpl();
        List<Logement> tousLesLogements = logementDAO.getAllLogementsAvecImages();
        afficherLogements(tousLesLogements);

        // --- Action de recherche ---
        searchBtn.addActionListener(e -> {
            String ville = villeField.getText().trim();
            if (!ville.isEmpty() && !ville.equals("Où allez-vous ?")) {
                List<Logement> filtres = tousLesLogements.stream()
                        .filter(l -> l.getVille() != null && l.getVille().equalsIgnoreCase(ville))
                        .collect(Collectors.toList());
                afficherLogements(filtres);
            } else {
                afficherLogements(tousLesLogements); // reset
            }
        });

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void afficherLogements(List<Logement> logements) {
        logementsPanel.removeAll();
        for (Logement logement : logements) {
            logementsPanel.add(new CarteLogementAdmin(logement));
            logementsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        logementsPanel.revalidate();
        logementsPanel.repaint();
    }
}
