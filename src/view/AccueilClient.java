package view;

import javax.swing.*;
import java.awt.*;

public class AccueilClient extends JFrame {
    public AccueilClient(String identifiant) {
        setTitle("Espace Client");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Bienvenue, " + identifiant + " !");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        add(label);

        setVisible(true);
    }
}
