package view;

import model.Logement;

import javax.swing.*;
import java.awt.*;
import java.util.List;
public class CarteLogementAdmin extends JPanel {
    private JLabel imageLabel;
    private int currentImageIndex = 0;
    private Timer timer;

    public CarteLogementAdmin(Logement logement) {
        setPreferredSize(new Dimension(600, 250));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setLayout(new BorderLayout());  // Utilisation du BorderLayout

        // Image à gauche
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(250, 250));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(imageLabel, BorderLayout.WEST);

        // Création du panneau à droite
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());  // BorderLayout pour la gestion du bouton
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Création du panneau pour le texte (nom, description, etc.)
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titre = new JLabel(logement.getNom());
        titre.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel desc = new JLabel("<html><body style='width:300px'>" + logement.getDescription() + "</body></html>");
        desc.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel infos = new JLabel("<html><body style='width:300px'>"
                + "<br><b>Superficie :</b> " + logement.getSuperficie() + " m²"
                + "<br><b>Nombre de personnes max :</b> " + logement.getNbPersonnesMax()
                + "<br><b>Nombre d'étoiles :</b> " + logement.getNombreEtoiles()
                + (logement.getDateCreation() != null ? ("<br><b>Date de création :</b> " + logement.getDateCreation()) : "")
                + "</body></html>");
        infos.setFont(new Font("Arial", Font.PLAIN, 12));

        // Position du bouton "Modifier"
        JButton modifierBtn = new JButton("Modifier");
        modifierBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);  // Aligne à droite

        modifierBtn.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new FenetreModifierLogement(logement, parent);
        });

        // Panneau d'options (WiFi, Clim, Parking)
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        optionsPanel.setOpaque(false);

        if (logement.hasWifi()) {
            JLabel wifiIcon = new JLabel(resizeIcon(new ImageIcon("images/wifi.jpeg"), 24, 24));
            optionsPanel.add(wifiIcon);
        }
        if (logement.hasClim()) {
            JLabel climIcon = new JLabel(resizeIcon(new ImageIcon("images/clim.jpeg"), 24, 24));
            optionsPanel.add(climIcon);
        }
        if (logement.hasParking()) {
            JLabel parkingIcon = new JLabel(resizeIcon(new ImageIcon("images/parking.jpeg"), 24, 24));
            optionsPanel.add(parkingIcon);
        }

        // Ajouter tout dans le panneau texte
        textPanel.add(titre);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(desc);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(infos);

        JLabel adresseLabel = new JLabel("<html><b>Adresse :</b> " + logement.getRue() + ", "
                + logement.getCodePostal() + " " + logement.getVille() + " (" + logement.getPays() + ")</html>");
        adresseLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        textPanel.add(adresseLabel);

        JLabel distanceLabel = new JLabel("À " + logement.getDistanceCentre() + " m du centre");
        distanceLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        textPanel.add(distanceLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(optionsPanel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Ajouter le bouton "Modifier" dans le coin supérieur droit
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));  // Panneau de flux à droite
        topRightPanel.setOpaque(false);
        topRightPanel.add(modifierBtn);
        rightPanel.add(topRightPanel, BorderLayout.NORTH);  // Ajouter le bouton au panneau droit en haut

        // Ajouter tout dans le panneau principal à droite
        rightPanel.add(textPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.CENTER);

        // Carrousel d’images
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
            Image scaled = icon.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            imageLabel.setText("Image non trouvée");
        }
    }

    private Icon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resized = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }
}
