package view;

import dao.ReservationDAOImpl;
import dao.UtilisateurDAOImpl;
import model.Reservation;
import model.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PagePaiement extends JFrame {
    public PagePaiement(String identifiant) {
        setTitle("Paiement de vos réservations");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        UtilisateurDAOImpl utilisateurDAO = new UtilisateurDAOImpl();
        Utilisateur utilisateur = utilisateurDAO.getByIdentifiant(identifiant);
        if (utilisateur == null) {
            JOptionPane.showMessageDialog(this, "Utilisateur introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        boolean isAncienClient = utilisateur.getAncienClient() == 1;
        double reduction = isAncienClient ? 0.30 : 0.0;

        ReservationDAOImpl reservationDAO = new ReservationDAOImpl();
        List<Reservation> reservations = reservationDAO.getReservationsEnAttente(utilisateur.getId());

        String[] columnNames = {"ID", "Logement", "Date début", "Date fin", "Adultes", "Enfants", "Prix", "Statut"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Reservation r : reservations) {
            double prixFinal = r.getPrixTotal();
            if (isAncienClient) {
                prixFinal *= (1 - reduction);
            }
            model.addRow(new Object[]{
                    r.getId(),
                    r.getLogementId(),
                    r.getDateDebut(),
                    r.getDateFin(),
                    r.getNombreAdultes(),
                    r.getNombreEnfants(),
                    prixFinal,
                    r.getStatut()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnPayer = new JButton("Payer les réservations sélectionnées");
        btnPayer.setBackground(new Color(0, 102, 204));
        btnPayer.setForeground(Color.WHITE);

        btnPayer.addActionListener(e -> {
            int[] selectedRows = table.getSelectedRows();
            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner au moins une réservation.");
                return;
            }

            double totalAvant = 0;
            for (int row : selectedRows) {
                double prix = (double) table.getValueAt(row, 6);
                if (isAncienClient) {
                    prix /= (1 - reduction); // pour retrouver le prix original
                }
                totalAvant += prix;
            }
            double montantReduction = isAncienClient ? totalAvant * reduction : 0;
            double totalApres = totalAvant - montantReduction;

            String message = String.format(
                    "Montant total avant réduction : %.2f €\nRéduction appliquée : %.2f €\nMontant final à payer : %.2f €",
                    totalAvant, montantReduction, totalApres
            );

            int infoConfirm = JOptionPane.showConfirmDialog(this, message + "\n\nContinuer vers le paiement ?", "Détail du paiement", JOptionPane.OK_CANCEL_OPTION);
            if (infoConfirm != JOptionPane.OK_OPTION) return;

            JPanel paiementPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            JTextField numeroCarte = new JTextField();
            JTextField nomCarte = new JTextField();
            JTextField expiration = new JTextField();
            JTextField cvv = new JTextField();

            paiementPanel.add(new JLabel("Numéro de carte (16 chiffres) :"));
            paiementPanel.add(numeroCarte);
            paiementPanel.add(new JLabel("Nom sur la carte :"));
            paiementPanel.add(nomCarte);
            paiementPanel.add(new JLabel("Date d'expiration (MM/AA) :"));
            paiementPanel.add(expiration);
            paiementPanel.add(new JLabel("CVV (3 chiffres) :"));
            paiementPanel.add(cvv);

            int result = JOptionPane.showConfirmDialog(this, paiementPanel, "Informations de paiement", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String num = numeroCarte.getText().trim();
                String nom = nomCarte.getText().trim();
                String exp = expiration.getText().trim();
                String c = cvv.getText().trim();

                if (!num.matches("\\d{16}") || !exp.matches("\\d{2}/\\d{2}") || !c.matches("\\d{3}") || nom.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez remplir les champs correctement.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for (int row : selectedRows) {
                    int reservationId = (int) table.getValueAt(row, 0);
                    reservationDAO.confirmerReservation(reservationId);
                }

                JOptionPane.showMessageDialog(this, "Paiement effectué avec succès ! " +
                        (isAncienClient ? "(Réduction de 30% appliquée)" : ""));
                dispose();
            }
        });

        JPanel panelBas = new JPanel();
        panelBas.add(btnPayer);

        add(scrollPane, BorderLayout.CENTER);
        add(panelBas, BorderLayout.SOUTH);
        setVisible(true);
    }
}
