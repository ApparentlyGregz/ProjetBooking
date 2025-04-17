package view;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.IDateEvaluator;
import dao.ReservationDAO;
import dao.ReservationDAOImpl;
import model.Logement;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PageLogement extends JFrame {
    private int currentImageIndex = 0;
    private Timer timer;
    private JLabel imageLabel;

    public PageLogement(Logement logement) {
        setTitle("Détails du logement - " + logement.getNom());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Titre
        JLabel titre = new JLabel(logement.getNom(), SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 32));
        mainPanel.add(titre, BorderLayout.NORTH);

        // Centre
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        // === PARTIE GAUCHE : CARROUSEL IMAGE ===
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.TOP);
        imageLabel.setPreferredSize(new Dimension(500, 400));

        List<String> images = logement.getImages();
        if (!images.isEmpty()) {
            updateImage(images);
            timer = new Timer(2500, e -> {
                currentImageIndex = (currentImageIndex + 1) % images.size();
                updateImage(images);
            });
            timer.start();
        } else {
            imageLabel.setText("Aucune image disponible");
        }

        centerPanel.add(imageLabel, BorderLayout.WEST);

        // === PARTIE DROITE : INFOS + CALENDRIER ===
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setOpaque(false);

        // --- INFOS EN HAUT ---
        JPanel topInfosPanel = new JPanel();
        topInfosPanel.setLayout(new BoxLayout(topInfosPanel, BoxLayout.Y_AXIS));
        topInfosPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topInfosPanel.setOpaque(false);

        JLabel description = new JLabel("<html><body style='width:400px'>" + logement.getDescription() + "</body></html>");
        description.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel infos = new JLabel("<html><body style='width:400px'>"
                + "<b>Superficie :</b> " + logement.getSuperficie() + " m²<br>"
                + "<b>Personnes max :</b> " + logement.getNbPersonnesMax() + "<br>"
                + "<b>Étoiles :</b> " + logement.getNombreEtoiles() + "<br>"
                + "<b>Date de création :</b> " + logement.getDateCreation()
                + "</body></html>");
        infos.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel iconsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iconsPanel.setOpaque(false);

        if (logement.hasWifi()) {
            iconsPanel.add(new JLabel(resizeIcon(new ImageIcon("images/wifi.jpeg"), 24, 24)));
        }
        if (logement.hasClim()) {
            iconsPanel.add(new JLabel(resizeIcon(new ImageIcon("images/clim.jpeg"), 24, 24)));
        }
        if (logement.hasParking()) {
            iconsPanel.add(new JLabel(resizeIcon(new ImageIcon("images/parking.jpeg"), 24, 24)));
        }

        topInfosPanel.add(description);
        topInfosPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topInfosPanel.add(infos);
        topInfosPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topInfosPanel.add(iconsPanel);

        rightPanel.add(topInfosPanel, BorderLayout.NORTH);

        // --- CALENDRIER EN BAS ---
        JPanel calendarPanel = new JPanel();
        calendarPanel.setLayout(new BoxLayout(calendarPanel, BoxLayout.Y_AXIS));
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        calendarPanel.setOpaque(false);

        JLabel calendarTitle = new JLabel("Disponibilités");
        calendarTitle.setFont(new Font("Arial", Font.BOLD, 16));
        calendarTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCalendar calendar = new JCalendar();
        calendar.setPreferredSize(new Dimension(350, 250));

        ReservationDAO reservationDAO = new ReservationDAOImpl();
        List<Date> datesReservees = reservationDAO.getDatesReserveesPourLogement(logement.getId());

        calendar.getDayChooser().addDateEvaluator(new IDateEvaluator() {
            @Override
            public boolean isSpecial(Date date) { return false; }

            @Override
            public Color getSpecialForegroundColor() { return null; }

            @Override
            public Color getSpecialBackroundColor() { return null; }

            @Override
            public String getSpecialTooltip() { return null; }

            @Override
            public boolean isInvalid(Date date) {
                Calendar c1 = Calendar.getInstance();
                c1.setTime(date);
                c1.set(Calendar.HOUR_OF_DAY, 0);
                c1.set(Calendar.MINUTE, 0);
                c1.set(Calendar.SECOND, 0);
                for (Date reserved : datesReservees) {
                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(reserved);
                    c2.set(Calendar.HOUR_OF_DAY, 0);
                    c2.set(Calendar.MINUTE, 0);
                    c2.set(Calendar.SECOND, 0);
                    if (c1.equals(c2)) return true;
                }
                return false;
            }

            @Override
            public Color getInvalidForegroundColor() { return Color.RED; }

            @Override
            public Color getInvalidBackroundColor() { return Color.LIGHT_GRAY; }

            @Override
            public String getInvalidTooltip() { return "Déjà réservée"; }
        });

        calendarPanel.add(calendarTitle);
        calendarPanel.add(calendar);
        rightPanel.add(calendarPanel, BorderLayout.SOUTH);

        centerPanel.add(rightPanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bas : bouton de réservation (placeholder)
        JButton btnReserver = new JButton("Réserver ce logement");
        btnReserver.setFont(new Font("Arial", Font.BOLD, 18));
        btnReserver.setPreferredSize(new Dimension(250, 50));
        btnReserver.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Formulaire de réservation à venir...");
        });

        JPanel southPanel = new JPanel();
        southPanel.add(btnReserver);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private Icon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resized = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }

    private void updateImage(List<String> images) {
        try {
            String path = "images/" + images.get(currentImageIndex);
            ImageIcon icon = new ImageIcon(path);
            Image scaled = icon.getImage().getScaledInstance(450, 300, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            imageLabel.setText("Image non trouvée");
        }
    }
}