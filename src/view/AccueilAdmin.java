package view;

import javax.swing.*;
import java.awt.*;

public class AccueilAdmin extends JFrame {
    public AccueilAdmin(String identifiant) {
        setTitle("Espace Admin");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Admin : " + identifiant);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        add(label);

        setVisible(true);
    }
}
