package salon.beaute.demo.managers;

import salon.beaute.demo.models.Client;
import salon.beaute.demo.models.RendezVous;
import salon.beaute.demo.models.Service;
// AJOUT
import salon.beaute.demo.models.Prestataire;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RendezVousManager {
    private static final String FILE_PATH = "src/data/rendezvous.csv";
    private List<RendezVous> rdvs = new ArrayList<>();
    private ClientManager clientManager;
    private ServiceManager serviceManager;
    // AJOUT
    private PrestataireManager prestataireManager;

    // MODIF : ajout du paramètre PrestataireManager
    public RendezVousManager(ClientManager clientManager, ServiceManager serviceManager, PrestataireManager prestataireManager) {
        this.clientManager = clientManager;
        this.serviceManager = serviceManager;
        this.prestataireManager = prestataireManager; // AJOUT
        charger();
    }

    public void ajouter(RendezVous rdv) {
        rdvs.add(rdv);
        sauvegarder();
    }

    public List<RendezVous> getAll() {
        return rdvs;
    }

    public void afficherTous() {
        for (RendezVous r : rdvs) r.afficher();
    }

    private void charger() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                // MODIF : passer prestataireManager
                rdvs.add(RendezVous.fromCSV(ligne, clientManager, serviceManager, prestataireManager));
            }
        } catch (IOException e) {
            System.out.println("Erreur chargement RDV : " + e.getMessage());
        }
    }

    private void sauvegarder() {
        File f = new File(FILE_PATH);
        f.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (RendezVous r : rdvs) {
                writer.write(r.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erreur sauvegarde RDV : " + e.getMessage());
        }
    }

    // MODIF : réservation avec choix du prestataire et vérification des conflits
    public void reserverRendezVous(Client client, Scanner scanner) {
        System.out.println("=== Réservation ===");
        serviceManager.afficherTous();
        System.out.print("Nom du service : ");
        String nomService = scanner.nextLine();
        Service service = serviceManager.chercherParNom(nomService);
        if (service == null) {
            System.out.println("Service introuvable.");
            return;
        }

        System.out.println("Liste des prestataires disponibles :");
        for (Prestataire p : prestataireManager.getPrestataires()) {
            p.afficher();
        }
        System.out.print("Email du prestataire : ");
        String emailPrestataire = scanner.nextLine();
        Prestataire prestataire = prestataireManager.getPrestataireParEmail(emailPrestataire);
        if (prestataire == null) {
            System.out.println("Prestataire introuvable.");
            return;
        }

        System.out.print("Date (YYYY-MM-DD) : ");
        String dateStr = scanner.nextLine();
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            System.out.println("Date invalide.");
            return;
        }

        // Vérification des conflits
        for (RendezVous rdv : rdvs) {
            if (rdv.getPrestataire() != null &&
                    rdv.getPrestataire().getEmail().equalsIgnoreCase(emailPrestataire) &&
                    rdv.getDate().equals(date)) {
                System.out.println("Erreur : Ce prestataire a déjà un rendez-vous à cette date.");
                System.out.println("Notification : La réservation a échoué car le créneau est déjà pris.");
                return;
            }
        }


        RendezVous rdv = new RendezVous(client, service, date, "Prévu", prestataire);
        rdvs.add(rdv);
        sauvegarder();

        // Notification de confirmation
        System.out.println("Notification : Rendez-vous confirmé pour le " + date + " avec " + prestataire.getNom() + ".");
    }


    public void afficherRendezVousPourClient(Client client) {
        System.out.println("=== Vos Rendez-vous ===");
        for (RendezVous rdv : rdvs) {
            if (rdv.getClient().getEmail().equals(client.getEmail())) {
                rdv.afficher();
            }
        }
    }

    // AJOUT : méthode pour afficher les RDV d’un prestataire
    public void afficherRendezVousPourPrestataire(Prestataire prestataire) {
        System.out.println("=== Rendez-vous pour " + prestataire.getNom() + " ===");
        for (RendezVous rdv : rdvs) {
            if (rdv.getPrestataire() != null &&
                    rdv.getPrestataire().getEmail().equals(prestataire.getEmail())) {
                rdv.afficher();
            }
        }
    }
    public boolean annulerRendezVous(String id, Client client) {
        for (RendezVous rdv : rdvs) {
            if (rdv.getId().equals(id) && rdv.getClient().getEmail().equals(client.getEmail())) {
                rdvs.remove(rdv);
                sauvegarder();
                System.out.println("Notification : Rendez-vous annulé pour le " + rdv.getDate() + " avec " + rdv.getPrestataire().getNom() + ".");
                return true;
            }
        }
        return false;
    }


}
