package view;

import dao.LogementDAO;
import dao.LogementDAOImpl;
import dao.TarifDAO;
import dao.TarifDAOImpl;
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
        setLayout(new BorderLayout());

        // Image à gauche
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(250, 250));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(imageLabel, BorderLayout.WEST);

        // Panneau principal à droite
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Panneau pour les textes
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titre = new JLabel(logement.getNom());
        titre.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel desc = new JLabel("<html><body style='width:300px'>" + logement.getDescription() + "</body></html>");
        desc.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel nbChambresLabel = new JLabel("<html><body style='width:300px'><br><b>Nombre de chambres :</b> " + logement.getNbChambres() + "</body></html>");
        nbChambresLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel infos = new JLabel("<html><body style='width:300px'>"
                + "<br><b>Superficie :</b> " + logement.getSuperficie() + " m²"
                + "<br><b>Nombre de personnes max :</b> " + logement.getNbPersonnesMax()
                + "<br><b>Nombre d'étoiles :</b> " + logement.getNombreEtoiles()
                + (logement.getDateCreation() != null ? ("<br><b>Date de création :</b> " + logement.getDateCreation()) : "")
                + "</body></html>");
        infos.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel adresseLabel = new JLabel("<html><b>Adresse :</b> " + logement.getRue() + ", "
                + logement.getCodePostal() + " " + logement.getVille() + " (" + logement.getPays() + ")</html>");
        adresseLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel distanceLabel = new JLabel("À " + logement.getDistanceCentre() + " m du centre");
        distanceLabel.setFont(new Font("Arial", Font.ITALIC, 11));

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

        textPanel.add(titre);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(desc);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(infos);
        textPanel.add(nbChambresLabel);
        textPanel.add(adresseLabel);
        textPanel.add(distanceLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(optionsPanel);

        TarifDAO tarifDAO = new TarifDAOImpl();
        double prix = tarifDAO.getPrixParNuit(logement.getId());

        JPanel prixPanel = new JPanel();
        prixPanel.setLayout(new BoxLayout(prixPanel, BoxLayout.Y_AXIS));
        prixPanel.setPreferredSize(new Dimension(120, 250));
        prixPanel.setBackground(Color.WHITE);

        prixPanel.add(Box.createVerticalGlue());
        JLabel prixLabel = new JLabel(prix + " € / nuit");
        prixLabel.setFont(new Font("Arial", Font.BOLD, 16));
        prixLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        prixPanel.add(prixLabel);
        prixPanel.add(Box.createVerticalGlue());

        add(prixPanel, BorderLayout.EAST);

        // === Ajout des boutons "Modifier" et "Supprimer"
        JButton modifierBtn = new JButton("Modifier");
        JButton supprimerBtn = new JButton("Supprimer");

        modifierBtn.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new FenetreModifierLogement(logement, parent);
        });

        supprimerBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Êtes-vous sûr de vouloir supprimer ce logement ?",
                    "Confirmation de suppression",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                LogementDAO logementDAO = new LogementDAOImpl();
                boolean deleted = logementDAO.supprimerLogement(logement.getId());

                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Logement supprimé avec succès !");
                    Container parent = this.getParent();
                    parent.remove(this); // enlever la carte du panel
                    parent.revalidate();
                    parent.repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setOpaque(false);
        topRightPanel.add(modifierBtn);
        topRightPanel.add(supprimerBtn);

        rightPanel.add(topRightPanel, BorderLayout.NORTH);
        rightPanel.add(textPanel, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.CENTER);

        // === Carrousel d’images
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
