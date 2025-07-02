package salon.beaute.demo.managers;
import java.time.LocalDate;

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

    public void sauvegarder() {
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
        System.out.println("Liste des prestataires disponibles :");
        for (Prestataire p : prestataireManager.getPrestataires()) {
            if (!p.getIndisponibilites().contains(date)) {
                p.afficher();
            }
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
        System.out.println("Email envoyé à " + client.getEmail() + " : confirmation de votre rendez-vous.");
        System.out.println("SMS envoyé au " + client.getTel() + " : confirmation de votre rendez-vous.");

    }
    public void modifierRendezVous(String id, Client client, Scanner scanner) {
        for (RendezVous rdv : rdvs) {
            if (rdv.getId().equals(id) && rdv.getClient().getEmail().equals(client.getEmail())) {
                System.out.println("Que souhaitez-vous modifier ?");
                System.out.println("1. Date");
                System.out.println("2. Service");
                System.out.println("3. Prestataire");
                System.out.print("Votre choix : ");
                int choix = Integer.parseInt(scanner.nextLine());
                switch (choix) {
                    case 1 -> {
                        System.out.print("Nouvelle date (YYYY-MM-DD) : ");
                        String dateStr = scanner.nextLine();
                        LocalDate newDate;
                        try {
                            newDate = LocalDate.parse(dateStr);
                        } catch (DateTimeParseException e) {
                            System.out.println("Date invalide.");
                            return;
                        }
                        // Conflit ?
                        for (RendezVous r : rdvs) {
                            if (r != rdv && r.getPrestataire().getEmail().equals(rdv.getPrestataire().getEmail()) && r.getDate().equals(newDate)) {
                                System.out.println("Conflit : ce prestataire est déjà réservé à cette date.");
                                return;
                            }
                        }
                        rdv.setDate(newDate);
                    }
                    case 2 -> {
                        serviceManager.afficherTous();
                        System.out.print("Nouveau service : ");
                        String nomService = scanner.nextLine();
                        Service newService = serviceManager.chercherParNom(nomService);
                        if (newService == null) {
                            System.out.println("Service introuvable.");
                            return;
                        }
                        rdv.setService(newService);
                    }
                    case 3 -> {
                        for (Prestataire p : prestataireManager.getPrestataires()) p.afficher();
                        System.out.print("Email du nouveau prestataire : ");
                        String emailPrestataire = scanner.nextLine();
                        Prestataire newPrestataire = prestataireManager.getPrestataireParEmail(emailPrestataire);
                        if (newPrestataire == null) {
                            System.out.println("Prestataire introuvable.");
                            return;
                        }
                        // Conflit ?
                        for (RendezVous r : rdvs) {
                            if (r != rdv && r.getPrestataire().getEmail().equals(newPrestataire.getEmail()) && r.getDate().equals(rdv.getDate())) {
                                System.out.println("Conflit : ce prestataire est déjà réservé à cette date.");
                                return;
                            }
                        }
                        rdv.setPrestataire(newPrestataire);
                    }
                    default -> System.out.println("Choix invalide.");
                }
                sauvegarder();
                System.out.println("Notification : Rendez-vous modifié avec succès.");
                // Notification simulée mail
                System.out.println("Notification : Rendez-vous modifié avec succès.");
                System.out.println("Email envoyé à " + client.getEmail() + " : modification de votre rendez-vous.");
                System.out.println("SMS envoyé au " + client.getTel() + " : modification de votre rendez-vous.");

                return;
            }
        }
        System.out.println("Aucun rendez-vous trouvé avec cet ID.");
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
                // Notification de confirmation
                System.out.println("Notification : Rendez-vous annulé.");
                System.out.println("Email envoyé à " + client.getEmail() + " : annulation de votre rendez-vous.");
                System.out.println("SMS envoyé au " + client.getTel() + " : annulation de votre rendez-vous.");

                return true;
            }
        }
        return false;
    }


}
