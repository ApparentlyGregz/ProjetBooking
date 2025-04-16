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
        setPreferredSize(new Dimension(600, 180));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setLayout(new BorderLayout());

        // ----- IMAGE À GAUCHE -----
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(250, 180));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(imageLabel, BorderLayout.WEST);

        // ----- TEXTE À DROITE -----
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titre = new JLabel(logement.getNom());
        titre.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel desc = new JLabel("<html><body style='width:300px'>" + logement.getDescription() + "</body></html>");
        desc.setFont(new Font("Arial", Font.PLAIN, 13));

        textPanel.add(titre);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(desc);

        add(textPanel, BorderLayout.CENTER);

        // ----- CARROUSEL D’IMAGES -----
        List<String> images = logement.getImages();
        if (!images.isEmpty()) {
            updateImage(images);
            timer = new Timer(2500, e -> {
                currentImageIndex = (currentImageIndex + 1) % images.size();
                updateImage(images);
            });
            timer.start();
        }
    }

    private void updateImage(List<String> images) {
        try {
            String path = "images/" + images.get(currentImageIndex);
            ImageIcon icon = new ImageIcon(path);
            Image scaled = icon.getImage().getScaledInstance(250, 180, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            imageLabel.setText("Image non trouvée");
        }
    }
}
