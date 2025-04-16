package view;

import model.Logement;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CarteLogement extends JPanel {
    private JLabel imageLabel;
    private int currentImageIndex = 0;
    private Timer timer;

    public CarteLogement(Logement logement) {
        setPreferredSize(new Dimension(300, 250));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setLayout(new BorderLayout());

        // Image
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(300, 150));
        add(imageLabel, BorderLayout.NORTH);

        // Texte descriptif
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        textPanel.add(new JLabel(logement.getNom()));
        textPanel.add(new JLabel("<html><body style='width: 280px'>" + logement.getDescription() + "</body></html>"));
        add(textPanel, BorderLayout.CENTER);

        // Lancer le carrousel si au moins une image
        List<String> images = logement.getImages();
        if (!images.isEmpty()) {
            updateImage(images);
            timer = new Timer(2000, e -> {
                currentImageIndex = (currentImageIndex + 1) % images.size();
                updateImage(images);
            });
            timer.start();
        }
    }

    private void updateImage(List<String> images) {
        String path = \"images/\" + images.get(currentImageIndex); // Relatif à src
        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaled));
    }
}
