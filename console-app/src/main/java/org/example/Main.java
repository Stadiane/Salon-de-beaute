package org.example;

import salon.beaute.demo.managers.ClientManager;
import salon.beaute.demo.managers.RendezVousManager;
import salon.beaute.demo.managers.ServiceManager;
import salon.beaute.demo.models.Client;
import salon.beaute.demo.models.Avis;
import salon.beaute.demo.models.RendezVous;
import salon.beaute.demo.managers.AvisManager;
import salon.beaute.demo.models.Service;
import salon.beaute.demo.managers.PrestataireManager;

import java.util.Scanner;

public class Main {
    public static void menuClient(ClientManager clientManager, Scanner scanner, RendezVousManager rdvManager, ServiceManager serviceManager, AvisManager avisManager) {
        System.out.println("=== ESPACE CLIENT ===");
        System.out.println("1. S'inscrire");
        System.out.println("2. Se connecter");
        System.out.print("Votre choix : ");
        int choix = Integer.parseInt(scanner.nextLine());
        switch (choix) {
            case 1 -> {
                System.out.print("Nom : ");
                String nom = scanner.nextLine();
                System.out.print("Téléphone : ");
                String tel = scanner.nextLine();
                System.out.print("Email : ");
                String email = scanner.nextLine();
                System.out.print("Mot de passe : ");
                String mdp1 = scanner.nextLine();
                System.out.print("Confirmer le mot de passe : ");
                String mdp2 = scanner.nextLine();
                if (!mdp1.equals(mdp2)) {
                    System.out.println("Les mots de passe ne correspondent pas. Inscription annulée.");
                    return;
                }
                Client nouveau = new Client(nom, tel, email, mdp1);
                clientManager.ajouterClient(nouveau);
                System.out.println("Inscription réussie !");
            }
            case 2 -> {
                System.out.print("Entrez votre email : ");
                String email = scanner.nextLine();
                System.out.print("Mot de passe : ");
                String motDePasse = scanner.nextLine();
                boolean trouve = false;
                for (Client c : clientManager.getClients()) {
                    if (c.getEmail().equalsIgnoreCase(email) && c.getMotDePasse().equals(motDePasse)) {
                        System.out.println("Bienvenue " + c.getNom() + " !");
                        trouve = true;
                        int choixClient;
                        do {
                            System.out.println("\n--- MENU CLIENT CONNECTÉ ---");
                            System.out.println("1. Réserver un rendez-vous");
                            System.out.println("2. Voir mes rendez-vous");
                            System.out.println("0. Retour");
                            System.out.print("Votre choix : ");
                            choixClient = Integer.parseInt(scanner.nextLine());
                            switch (choixClient) {
                                case 1 -> rdvManager.reserverRendezVous(c, scanner);
                                case 2 -> {
                                    rdvManager.afficherRendezVousPourClient(c);
                                    for (RendezVous rdv : rdvManager.getAll()) {
                                        if (rdv.getClient().getEmail().equals(c.getEmail())) {
                                            if (rdv.getStatut().equalsIgnoreCase("Prévu") || rdv.getStatut().equalsIgnoreCase("Terminé")) {
                                                if (rdv.getPrestataire() != null) {
                                                    System.out.print("Voulez-vous laisser un avis sur le rendez-vous du " + rdv.getDate() + " avec " + rdv.getPrestataire().getNom() + " ? (oui/non) : ");
                                                    String reponse = scanner.nextLine();
                                                    if (reponse.equalsIgnoreCase("oui")) {
                                                        System.out.print("Note (1-5) : ");
                                                        int note = Integer.parseInt(scanner.nextLine());
                                                        System.out.print("Commentaire : ");
                                                        String commentaire = scanner.nextLine();
                                                        avisManager.ajouterAvis(new Avis(c.getEmail(), rdv.getPrestataire().getEmail(), note, commentaire));
                                                        System.out.println("Merci pour votre avis !");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                case 0 -> System.out.println("Retour au menu principal.");
                                default -> System.out.println("Choix invalide.");
                            }
                        } while (choixClient != 0);
                        break;
                    }
                }
                if (!trouve) {
                    System.out.println("Email ou mot de passe incorrect.");
                }
            }
            default -> System.out.println("Choix invalide.");
        }
    }

    public static void main(String[] args) {
        ServiceManager manager = new ServiceManager();
        ClientManager clientManager = new ClientManager();
        PrestataireManager prestataireManager = new PrestataireManager();
        RendezVousManager rdvManager = new RendezVousManager(clientManager, manager, prestataireManager);
        AvisManager avisManager = new AvisManager();
        Scanner scanner = new Scanner(System.in);
        int choix = -1;
        do {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Lister les services");
            System.out.println("2. Ajouter un service");
            System.out.println("3. Supprimer un service");
            System.out.println("4. Rechercher un service");
            System.out.println("5. Espace client");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");
            try {
                choix = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez saisir un nombre.");
                continue;
            }
            switch (choix) {
                case 1 -> manager.afficherTous();
                case 2 -> {
                    System.out.print("Nom : ");
                    String nom = scanner.nextLine();
                    System.out.print("Description : ");
                    String desc = scanner.nextLine();
                    System.out.print("Prix (€) : ");
                    double prix = scanner.nextDouble();
                    System.out.print("Durée (min) : ");
                    int duree = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("URL de l'image : ");
                    String img = scanner.nextLine();
                    Service service = new Service(nom, desc, prix, duree, img);
                    manager.ajouter(service);
                    System.out.println("Service ajouté !");
                }
                case 3 -> {
                    System.out.print("Nom du service à supprimer : ");
                    String nomSuppr = scanner.nextLine();
                    boolean supprime = manager.supprimerParNom(nomSuppr);
                    if (supprime) {
                        System.out.println("Supprimé !");
                    } else {
                        System.out.println("Introuvable.");
                    }
                }
                case 4 -> {
                    System.out.print("Nom du service à rechercher : ");
                    String recherche = scanner.nextLine();
                    Service trouve = manager.chercherParNom(recherche);
                    if (trouve != null) {
                        trouve.afficher();
                    } else {
                        System.out.println("Aucun service trouvé.");
                    }
                }
                case 5 -> menuClient(clientManager, scanner, rdvManager, manager, avisManager);
                case 0 -> System.out.println("Au revoir !");
                default -> System.out.println("Choix invalide.");
            }
        } while (choix != 0);
        scanner.close();
    }
}
