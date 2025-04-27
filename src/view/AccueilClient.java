package view;

import dao.LogementDAO;
import dao.LogementDAOImpl;
import dao.TarifDAOImpl;
import model.Logement;
import model.Utilisateur;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;

public class AccueilClient extends JFrame {

    private JPanel logementsPanel;
    private Utilisateur utilisateur;
    private JTextField destinationField;
    private JSpinner nbPersonnes;
    private JSpinner nbChambres;
    private JSlider prixMaxSlider;
    private JSlider distanceMaxSlider;
    private JSlider superficieMinSlider;
    private JComboBox<String> typeCombo;
    private JSpinner nbEtoilesSpinner;

    public AccueilClient(String identifiant) {
        setTitle("Espace Client - Booking");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Utilisateur utilisateur = new dao.UtilisateurDAOImpl().getByIdentifiant(identifiant);
        this.utilisateur = utilisateur;

        JPanel mainPanel = new JPanel(new BorderLayout());

        // ======== Barre du haut ========
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(90, 130, 160));
        topPanel.setPreferredSize(new Dimension(0, 60));

        JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.addActionListener(e -> {
            dispose();
            new FenetreConnexion();
        });

        JLabel logo = new JLabel("Booking", JLabel.LEFT);
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);
        logo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));

        JButton btnPaiement = new JButton("Paiement");
        btnPaiement.addActionListener(e -> new PagePaiement(identifiant));

        JLabel userBox = new JLabel("Connecté : " + identifiant);
        userBox.setForeground(Color.WHITE);
        userBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(btnDeconnexion);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        centerPanel.add(logo);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(btnPaiement);
        rightPanel.add(userBox);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(centerPanel, BorderLayout.CENTER);
        topPanel.add(rightPanel, BorderLayout.EAST);

        // ======== Barre de recherche ========
        JPanel recherchePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        recherchePanel.setBackground(new Color(230, 240, 250));
        recherchePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        destinationField = new JTextField("Où allez-vous ?", 15);
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

        nbPersonnes = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        nbChambres = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        JButton btnRechercher = new JButton("Rechercher");

        recherchePanel.add(destinationField);
        recherchePanel.add(new JLabel("Personnes:"));
        recherchePanel.add(nbPersonnes);
        recherchePanel.add(new JLabel("Chambres:"));
        recherchePanel.add(nbChambres);
        recherchePanel.add(btnRechercher);

        // Panel contenant topPanel + recherchePanel
        JPanel topWrapper = new JPanel();
        topWrapper.setLayout(new BoxLayout(topWrapper, BoxLayout.Y_AXIS));
        topWrapper.add(topPanel);
        topWrapper.add(recherchePanel);

        mainPanel.add(topWrapper, BorderLayout.NORTH);

        // ======== Panneau de filtres ========
        JPanel filtresPanel = new JPanel();
        filtresPanel.setLayout(new BoxLayout(filtresPanel, BoxLayout.Y_AXIS));
        filtresPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        filtresPanel.setBackground(new Color(245, 245, 245));

        JLabel filtreTitre = new JLabel("Filtres avancés");
        filtreTitre.setFont(new Font("Arial", Font.BOLD, 16));

        filtresPanel.add(filtreTitre);
        filtresPanel.add(new JLabel("Prix maximum (€):"));
        prixMaxSlider = new JSlider(50, 500, 500);
        prixMaxSlider.setMajorTickSpacing(50);
        prixMaxSlider.setPaintTicks(true);
        prixMaxSlider.setPaintLabels(true);
        filtresPanel.add(prixMaxSlider);

        filtresPanel.add(new JLabel("Distance maximum (m):"));
        distanceMaxSlider = new JSlider(100, 5000, 5000);
        distanceMaxSlider.setMajorTickSpacing(1000);
        distanceMaxSlider.setPaintTicks(true);
        distanceMaxSlider.setPaintLabels(true);
        filtresPanel.add(distanceMaxSlider);

        filtresPanel.add(new JLabel("Superficie minimale (m²):"));
        superficieMinSlider = new JSlider(10, 300, 10);
        superficieMinSlider.setMajorTickSpacing(50);
        superficieMinSlider.setPaintTicks(true);
        superficieMinSlider.setPaintLabels(true);
        filtresPanel.add(superficieMinSlider);

        filtresPanel.add(new JLabel("Type de logement:"));
        typeCombo = new JComboBox<>(new String[] {"", "Villa", "Appartement", "Chalet", "Hôtel", "Studio"});;
        typeCombo.setMaximumSize(new Dimension(150, 25));
        filtresPanel.add(typeCombo);

        filtresPanel.add(new JLabel("Nombre d'étoiles minimum:"));
        nbEtoilesSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
        nbEtoilesSpinner.setMaximumSize(new Dimension(50, 25));
        filtresPanel.add(nbEtoilesSpinner);

        mainPanel.add(filtresPanel, BorderLayout.WEST);

        // ======== Panneau des logements ========
        logementsPanel = new JPanel();
        logementsPanel.setLayout(new BoxLayout(logementsPanel, BoxLayout.Y_AXIS));
        logementsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(logementsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        afficherLogements(new LogementDAOImpl().getAllLogementsAvecImages());

        btnRechercher.addActionListener(e -> filtrerLogements());

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

    private void filtrerLogements() {
        String ville = destinationField.getText().trim();
        int nbPers = (int) nbPersonnes.getValue();
        int nbCh = (int) nbChambres.getValue();

        if (nbPers < 1) nbPers = 1;
        if (nbCh < 1) nbCh = 1;

        List<Logement> logements = new LogementDAOImpl().rechercherLogements(
                ville.equals("Où allez-vous ?") ? "" : ville, nbPers, nbCh
        );

        double prixMax = prixMaxSlider.getValue();
        int distanceMax = distanceMaxSlider.getValue();
        int superficieMin = superficieMinSlider.getValue();
        String typeLogement = (String) typeCombo.getSelectedItem();
        int nbEtoilesMin = (int) nbEtoilesSpinner.getValue();

        logements.removeIf(l -> {
            TarifDAOImpl tarifDAO = new TarifDAOImpl();
            double prix = tarifDAO.getPrixParNuit(l.getId());
            boolean filtrePrix = prix > prixMax;
            boolean filtreDistance = l.getDistanceCentre() > distanceMax;
            boolean filtreSuperficie = l.getSuperficie() < superficieMin;
            boolean filtreType = (typeLogement != null && !typeLogement.isEmpty() && !l.getType().equalsIgnoreCase(typeLogement));
            boolean filtreEtoiles = l.getNombreEtoiles() < nbEtoilesMin;
            return filtrePrix || filtreDistance || filtreSuperficie || filtreType || filtreEtoiles;
        });

        afficherLogements(logements);
    }

}