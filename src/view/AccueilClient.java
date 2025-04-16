package view;

import dao.LogementDAO;
import dao.LogementDAOImpl;
import model.Logement;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AccueilClient extends JFrame {

    public AccueilClient(String identifiant) {
        setTitle("Espace Client - Booking");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // ------ Barre du haut ------
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(100, 140, 180));
        topBar.setPreferredSize(new Dimension(0, 60));

        JLabel logo = new JLabel("Booking", JLabel.LEFT);
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);
        logo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));

        JLabel userBox = new JLabel("Connecté : " + identifiant);
        userBox.setFont(new Font("Arial", Font.PLAIN, 16));
        userBox.setForeground(Color.WHITE);
        userBox.setHorizontalAlignment(SwingConstants.RIGHT);
        userBox.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20));

        topBar.add(logo, BorderLayout.WEST);
        topBar.add(userBox, BorderLayout.EAST);
        mainPanel.add(topBar, BorderLayout.NORTH);

        // ------ Zone des logements ------
        JPanel logementsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        logementsPanel.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(logementsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ------ Récupérer les logements ------
        LogementDAO logementDAO = new LogementDAOImpl();
        List<Logement> logements = logementDAO.getAllLogementsAvecImages();

        for (Logement logement : logements) {
            logementsPanel.add(new CarteLogement(logement));
        }

        setContentPane(mainPanel);
        setVisible(true);
    }
}
