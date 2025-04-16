package view;

import dao.LogementDAO;
import dao.LogementDAOImpl;
import model.Logement;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AccueilAdmin extends JFrame {

    public AccueilAdmin(String identifiant) {
        setTitle("Espace Administrateur - Booking");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ------ Panel principal ------
        JPanel mainPanel = new JPanel(new BorderLayout());

        // ------ Barre du haut ------
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(180, 200, 230));  // plus clair
        topBar.setPreferredSize(new Dimension(0, 60));

        JLabel logo = new JLabel("Booking Admin", JLabel.LEFT);
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setForeground(Color.DARK_GRAY);
        logo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));

        JPanel adminActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        adminActions.setOpaque(false);

        JButton btnAdd = new JButton("\u2795 Ajouter");
        btnAdd.addActionListener(e -> new FenetreAjoutLogement(this));
        JButton btnUsers = new JButton("\uD83D\uDC64 Utilisateurs");

        adminActions.add(btnAdd);
        adminActions.add(btnUsers);

        topBar.add(logo, BorderLayout.WEST);
        topBar.add(adminActions, BorderLayout.EAST);

        mainPanel.add(topBar, BorderLayout.NORTH);

        // ------ Zone des logements ------
        JPanel logementsPanel = new JPanel();
        logementsPanel.setLayout(new BoxLayout(logementsPanel, BoxLayout.Y_AXIS));
        logementsPanel.setBackground(new Color(245, 245, 250));
        logementsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JScrollPane scrollPane = new JScrollPane(logementsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ------ Récupérer les logements ------
        LogementDAO logementDAO = new LogementDAOImpl();
        List<Logement> logements = logementDAO.getAllLogementsAvecImages();

        for (Logement logement : logements) {
            logementsPanel.add(new CarteLogementAdmin(logement));
            logementsPanel.add(Box.createRigidArea(new Dimension(0, 20))); // espacement
        }

        setContentPane(mainPanel);
        setVisible(true);
    }
}