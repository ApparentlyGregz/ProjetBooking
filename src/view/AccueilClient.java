package view;

import dao.LogementDAO;
import dao.LogementDAOImpl;
import dao.TarifDAOImpl;
import dao.UtilisateurDAOImpl;
import model.Logement;
import model.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AccueilClient extends JFrame {
    private JPanel logementsPanel;
    private Utilisateur utilisateur;

    // Composants filtres
    private JSlider prixSlider;
    private JSlider distanceSlider;
    private JCheckBox wifiBox, climBox, parkingBox;

    // Composants recherche
    private JTextField destinationField;
    private JSpinner dateArrivee, dateDepart, nbPersonnes, nbChambres;

    public AccueilClient(String identifiant) {
        setTitle("Espace Client - Booking");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UtilisateurDAOImpl utilisateurDAO = new UtilisateurDAOImpl();
        this.utilisateur = utilisateurDAO.getByIdentifiant(identifiant);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // ------ PANEL GLOBAL HAUT ------
        JPanel topGlobalPanel = new JPanel();
        topGlobalPanel.setLayout(new BorderLayout());

        // ------ BARRE DU HAUT (Déconnexion + Logo + Paiement) ------
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(90, 130, 160));
        topBar.setPreferredSize(new Dimension(0, 60));

        JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.addActionListener(e -> {
            dispose();
            new FenetreConnexion();
        });

        JLabel logo = new JLabel("Booking", JLabel.CENTER);
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);

        JButton btnPaiement = new JButton("Paiement");
        btnPaiement.addActionListener(e -> new PagePaiement(utilisateur.getIdentifiant()));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(btnDeconnexion);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(btnPaiement);

        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(logo, BorderLayout.CENTER);
        topBar.add(rightPanel, BorderLayout.EAST);

        topGlobalPanel.add(topBar, BorderLayout.NORTH);

        // ------ BARRE DE RECHERCHE ------
        JPanel recherchePanel = new JPanel();
        recherchePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        recherchePanel.setBackground(new Color(230, 240, 250));
        recherchePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        destinationField = new JTextField("Où allez-vous ?", 12);
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

        dateArrivee = new JSpinner(new SpinnerDateModel());
        dateArrivee.setEditor(new JSpinner.DateEditor(dateArrivee, "dd/MM/yyyy"));
        dateDepart = new JSpinner(new SpinnerDateModel());
        dateDepart.setEditor(new JSpinner.DateEditor(dateDepart, "dd/MM/yyyy"));
        nbPersonnes = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        nbChambres = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        JButton btnRechercher = new JButton("🔍 Rechercher");
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

        topGlobalPanel.add(recherchePanel, BorderLayout.SOUTH);

        mainPanel.add(topGlobalPanel, BorderLayout.NORTH);

        // ------ PANNEAU CENTRAL contenant filtres + logements ------
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Filtres à gauche
        JPanel filtresPanel = new JPanel();
        filtresPanel.setLayout(new BoxLayout(filtresPanel, BoxLayout.Y_AXIS));
        filtresPanel.setBackground(new Color(230, 240, 250));
        filtresPanel.setBorder(BorderFactory.createTitledBorder("Filtres"));

        filtresPanel.add(new JLabel("Prix maximum (€)"));
        prixSlider = new JSlider(0, 500, 500);
        prixSlider.setMajorTickSpacing(100);
        prixSlider.setPaintTicks(true);
        prixSlider.setPaintLabels(true);
        filtresPanel.add(prixSlider);

        filtresPanel.add(new JLabel("Distance maximale (m)"));
        distanceSlider = new JSlider(0, 3000, 3000);
        distanceSlider.setMajorTickSpacing(500);
        distanceSlider.setPaintTicks(true);
        distanceSlider.setPaintLabels(true);
        filtresPanel.add(distanceSlider);

        wifiBox = new JCheckBox("WiFi");
        climBox = new JCheckBox("Climatisation");
        parkingBox = new JCheckBox("Parking");

        filtresPanel.add(wifiBox);
        filtresPanel.add(climBox);
        filtresPanel.add(parkingBox);

        JButton btnAppliquerFiltres = new JButton("Appliquer filtres");
        btnAppliquerFiltres.setBackground(new Color(0, 102, 204));
        btnAppliquerFiltres.setForeground(Color.WHITE);
        filtresPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        filtresPanel.add(btnAppliquerFiltres);

        centerPanel.add(filtresPanel, BorderLayout.WEST);

        // Logements à droite
        logementsPanel = new JPanel();
        logementsPanel.setLayout(new BoxLayout(logementsPanel, BoxLayout.Y_AXIS));
        logementsPanel.setBackground(new Color(245, 245, 245));
        logementsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(logementsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Récupération initiale
        afficherLogements(new LogementDAOImpl().getAllLogementsAvecImages());

        // Actions
        btnRechercher.addActionListener(e -> appliquerRecherche());
        btnAppliquerFiltres.addActionListener(e -> appliquerRecherche());

        setContentPane(mainPanel);
        setVisible(true);
    }
    private void appliquerRecherche() {
        String ville = destinationField.getText().trim();
        int nbPersonnesVal = (int) nbPersonnes.getValue();
        int nbChambresVal = (int) nbChambres.getValue();

        List<Logement> tousLogements = new LogementDAOImpl().getAllLogementsAvecImages();
        List<Logement> resultats = new ArrayList<>();

        for (Logement l : tousLogements) {
            boolean correspond = true;

            if (!(ville.isEmpty() || ville.equalsIgnoreCase("Où allez-vous ?"))) {
                if (!l.getVille().equalsIgnoreCase(ville)) {
                    correspond = false;
                }
            }

            if (l.getNbPersonnesMax() < nbPersonnesVal) {
                correspond = false;
            }

            if (l.getNbChambres() < nbChambresVal) {
                correspond = false;
            }

            if (correspond) {
                resultats.add(l);
            }
        }

        // Ensuite on applique aussi les filtres de gauche (prix, distance, wifi, clim, parking)
        double prixMax = prixSlider.getValue();
        int distanceMax = distanceSlider.getValue();
        boolean wifi = wifiBox.isSelected();
        boolean clim = climBox.isSelected();
        boolean parking = parkingBox.isSelected();

        TarifDAOImpl tarifDAO = new TarifDAOImpl();

        resultats.removeIf(l -> {
            double prix = tarifDAO.getPrixParNuit(l.getId());
            return (prix > prixMax)
                    || (l.getDistanceCentre() > distanceMax)
                    || (wifi && !l.hasWifi())
                    || (clim && !l.hasClim())
                    || (parking && !l.hasParking());
        });

        afficherLogements(resultats);
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
