package salon.beaute.demo.models;

import salon.beaute.demo.managers.ClientManager;
import salon.beaute.demo.managers.ServiceManager;
// AJOUT
import salon.beaute.demo.managers.PrestataireManager; // Pour la désérialisation du prestataire

import java.time.LocalDate;

public class RendezVous {
    private static int compteur = 1;
    private String id;
    private LocalDate date;
    private String statut;
    private Client client;
    private Service service;
    // AJOUT
    private Prestataire prestataire; // Ajout du prestataire

    // Constructeur pour nouvelle réservation
    // MODIF : ajout du paramètre prestataire
    public RendezVous(Client client, Service service, LocalDate date, String statut, Prestataire prestataire) {
        this.id = String.format("RDV%03d", compteur++); // ex: RDV001
        this.date = date;
        this.statut = statut;
        this.client = client;
        this.prestataire = prestataire; // AJOUT
        this.service = service;
    }

    // Constructeur utilisé lors du chargement depuis le CSV
    // MODIF : ajout du prestataire
    public RendezVous(String id, LocalDate date, String statut, Client client, Service service, Prestataire prestataire) {
        this.id = id;
        this.date = date;
        this.statut = statut;
        this.client = client;
        this.service = service;
        this.prestataire = prestataire; // AJOUT

        // Mettre à jour le compteur
        if (id.startsWith("RDV")) {
            try {
                int num = Integer.parseInt(id.replace("RDV", ""));
                if (num >= compteur) {
                    compteur = num + 1;
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }

    // MODIF : affichage du prestataire
    public void afficher() {
        String numeroLisible = id.replace("RDV", "#");
        System.out.println(" RDV " + numeroLisible + " | " + date + " | " + service.getNom() +
                " pour " + client.getNom() +
                (prestataire != null ? " avec " + prestataire.getNom() : "") + // AJOUT
                " - Statut: " + statut);
    }

    // MODIF : ajout du prestataire dans la sérialisation
    public String toCSV() {
        return id + ";" + date + ";" + statut + ";" + client.getNom() + ";" + service.getNom() +
                ";" + (prestataire != null ? prestataire.getEmail() : ""); // AJOUT
    }

    // MODIF : ajout du paramètre PrestataireManager
    public static RendezVous fromCSV(String line, ClientManager clientManager, ServiceManager serviceManager, PrestataireManager prestataireManager) {
        String[] parts = line.split(";");
        String id = parts[0];
        LocalDate date = LocalDate.parse(parts[1]);
        String statut = parts[2];
        Client client = clientManager.getClientParNom(parts[3]);
        Service service = serviceManager.chercherParNom(parts[4]);
        // AJOUT
        Prestataire prestataire = null;
        if (parts.length > 5 && prestataireManager != null) {
            prestataire = prestataireManager.getPrestataireParEmail(parts[5]);
        }
        return new RendezVous(id, date, statut, client, service, prestataire);
    }

    // --- Getters & Setters ---
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    // AJOUT
    public Prestataire getPrestataire() {
        return prestataire;
    }

    public void setPrestataire(Prestataire prestataire) {
        this.prestataire = prestataire;
    }
}
