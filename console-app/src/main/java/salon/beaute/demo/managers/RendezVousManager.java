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
        String nom = scanner.nextLine();
        Service service = serviceManager.chercherParNom(nom);

        if (service == null) {
            System.out.println(" Service introuvable.");
            return;
        }

        // AJOUT : choix du prestataire
        System.out.println("Liste des prestataires disponibles :");
        for (Prestataire p : prestataireManager.getPrestataires()) {
            System.out.println("- " + p.getNom() + " (" + p.getEmail() + ")");
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

        try {
            LocalDate date = LocalDate.parse(dateStr);

            // AJOUT : vérification des conflits pour ce prestataire
            for (RendezVous rdv : rdvs) {
                if (rdv.getPrestataire() != null &&
                        rdv.getPrestataire().getEmail().equals(prestataire.getEmail()) &&
                        rdv.getDate().equals(date)) {
                    System.out.println("Ce prestataire a déjà un rendez-vous à cette date !");
                    return;
                }
            }

            RendezVous rdv = new RendezVous(client, service, date, "Prévu", prestataire); // MODIF : ajout prestataire
            rdvs.add(rdv);
            sauvegarder();
            System.out.println(" Rendez-vous enregistré !");
        } catch (DateTimeParseException e) {
            System.out.println(" Date invalide.");
        }
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
}
