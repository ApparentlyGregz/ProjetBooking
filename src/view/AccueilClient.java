package view;

import javax.swing.*;
import java.awt.*;

public class AccueilClient extends JFrame {
    public AccueilClient(String identifiant) {
        setTitle("Espace Client - Booking");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Plein écran
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel principal en BorderLayout
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

        // ------ Zone centrale (contenu) ------
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel message = new JLabel("Trouvez votre logement idéal ");
        message.setFont(new Font("Arial", Font.BOLD, 28));
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField searchBar = new JTextField();
        searchBar.setMaximumSize(new Dimension(600, 30));
        searchBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton searchButton = new JButton("Rechercher");
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(message);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(searchBar);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(searchButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Ajout du panel principal à la fenêtre
        setContentPane(mainPanel);
        setVisible(true);
    }
}