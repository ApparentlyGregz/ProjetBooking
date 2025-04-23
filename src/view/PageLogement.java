package view;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.IDateEvaluator;
import dao.ReservationDAO;
import dao.ReservationDAOImpl;
import dao.TarifDAOImpl;
import model.Logement;
import model.Reservation;
import model.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PageLogement extends JFrame {
    private int currentImageIndex = 0;
    private Timer timer;
    private JLabel imageLabel;
    private Date dateDebutSelectionnee = null;
    private Date dateFinSelectionnee = null;
    private Utilisateur utilisateur;

    public PageLogement(Logement logement, Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        setTitle("Détails du logement - " + logement.getNom());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JLabel titre = new JLabel(logement.getNom(), SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 32));
        mainPanel.add(titre, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

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

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);

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

        if (logement.hasWifi())
            iconsPanel.add(new JLabel(resizeIcon(new ImageIcon("images/wifi.jpeg"), 24, 24)));
        if (logement.hasClim())
            iconsPanel.add(new JLabel(resizeIcon(new ImageIcon("images/clim.jpeg"), 24, 24)));
        if (logement.hasParking())
            iconsPanel.add(new JLabel(resizeIcon(new ImageIcon("images/parking.jpeg"), 24, 24)));

        topInfosPanel.add(description);
        topInfosPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topInfosPanel.add(infos);
        topInfosPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topInfosPanel.add(iconsPanel);

        rightPanel.add(topInfosPanel, BorderLayout.NORTH);

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
            public boolean isSpecial(Date date) { return false; }
            public Color getSpecialForegroundColor() { return null; }
            public Color getSpecialBackroundColor() { return null; }
            public String getSpecialTooltip() { return null; }

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

            public Color getInvalidForegroundColor() { return Color.RED; }
            public Color getInvalidBackroundColor() { return Color.LIGHT_GRAY; }
            public String getInvalidTooltip() { return "Déjà réservée"; }
        });

        calendar.getDayChooser().addPropertyChangeListener("day", evt -> {
            Date selectedDate = calendar.getDate();

            if (dateDebutSelectionnee == null) {
                dateDebutSelectionnee = selectedDate;
                JOptionPane.showMessageDialog(this, "Date d'arrivée sélectionnée : " + selectedDate);
            } else if (dateFinSelectionnee == null) {
                if (selectedDate.before(dateDebutSelectionnee)) {
                    JOptionPane.showMessageDialog(this, "La date de départ ne peut pas être avant la date d'arrivée !");
                } else {
                    dateFinSelectionnee = selectedDate;
                    JOptionPane.showMessageDialog(this, "Date de départ sélectionnée : " + selectedDate);
                }
            } else {
                dateDebutSelectionnee = selectedDate;
                dateFinSelectionnee = null;
                JOptionPane.showMessageDialog(this, "Nouvelle date d'arrivée sélectionnée : " + selectedDate);
            }
        });

        calendarPanel.add(calendarTitle);
        calendarPanel.add(calendar);
        rightPanel.add(calendarPanel, BorderLayout.SOUTH);

        centerPanel.add(rightPanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JButton btnReserver = new JButton("Réserver ce logement");
        btnReserver.setFont(new Font("Arial", Font.BOLD, 18));
        btnReserver.setPreferredSize(new Dimension(250, 50));
        btnReserver.addActionListener(e -> {
            if (dateDebutSelectionnee != null && dateFinSelectionnee != null) {
                JSpinner spinnerAdultes = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
                JSpinner spinnerEnfants = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

                JPanel panel = new JPanel(new GridLayout(2, 2));
                panel.add(new JLabel("Nombre d'adultes :"));
                panel.add(spinnerAdultes);
                panel.add(new JLabel("Nombre d'enfants :"));
                panel.add(spinnerEnfants);

                int result = JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Informations de réservation",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION) {
                    int nbAdultes = (int) spinnerAdultes.getValue();
                    int nbEnfants = (int) spinnerEnfants.getValue();
                    int totalPersonnes = nbAdultes + nbEnfants;

                    if (totalPersonnes > logement.getNbPersonnesMax()) {
                        JOptionPane.showMessageDialog(this,
                                "Ce logement peut accueillir au maximum " + logement.getNbPersonnesMax() + " personnes.\nVous avez saisi " + totalPersonnes + ".",
                                "Capacité dépassée",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    long diffMillis = dateFinSelectionnee.getTime() - dateDebutSelectionnee.getTime();
                    long nbNuits = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);
                    TarifDAOImpl tarifDAO = new TarifDAOImpl();
                    double prixParNuit = tarifDAO.getPrixParNuitPourPeriode(logement.getId(), dateDebutSelectionnee);
                    double total = prixParNuit * nbNuits;

                    Reservation reservation = new Reservation();
                    reservation.setLogementId(logement.getId());
                    reservation.setUtilisateurId(utilisateur.getId());
                    reservation.setDateDebut(dateDebutSelectionnee);
                    reservation.setDateFin(dateFinSelectionnee);
                    reservation.setNombreAdultes(nbAdultes);
                    reservation.setNombreEnfants(nbEnfants);
                    reservation.setStatut("en_attente");
                    reservation.setPrixTotal(total);
                    reservation.setPromotionId(null);

                    boolean inserted = new ReservationDAOImpl().insertReservation(reservation);

                    if (inserted) {
                        JOptionPane.showMessageDialog(this,
                                "Réservation enregistrée avec succès !\n" +
                                        "Total à payer : " + total + " €");
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Erreur lors de l'enregistrement de la réservation.",
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une date d'arrivée et de départ.");
            }
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