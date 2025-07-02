package org.example;
import java.time.LocalDate;

import salon.beaute.demo.managers.ClientManager;
import salon.beaute.demo.managers.RendezVousManager;
import salon.beaute.demo.managers.ServiceManager;
import salon.beaute.demo.models.Client;
import salon.beaute.demo.models.Avis;
import salon.beaute.demo.models.RendezVous;
import salon.beaute.demo.managers.AvisManager;
import salon.beaute.demo.models.Service;
import salon.beaute.demo.managers.PrestataireManager;
import salon.beaute.demo.models.Prestataire;
// import salon.beaute.demo.models.Prestation;

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
                            System.out.println("3. Annuler un rendez-vous");
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
                                case 3 -> {
                                    rdvManager.afficherRendezVousPourClient(c);
                                    System.out.println("4. Modifier un rendez-vous");
                                    System.out.print("ID du rendez-vous à annuler : ");
                                    String id = scanner.nextLine();
                                    boolean ok = rdvManager.annulerRendezVous(id, c);
                                    if (ok) System.out.println("Rendez-vous annulé !");
                                    else System.out.println("Erreur : rendez-vous introuvable ou non annulable.");
                                }
                                case 4 -> {
                                    rdvManager.afficherRendezVousPourClient(c);
                                    System.out.print("ID du rendez-vous à modifier : ");
                                    String id = scanner.nextLine();
                                    rdvManager.modifierRendezVous(id, c, scanner);
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

    public static void menuPrestataire(PrestataireManager prestataireManager, RendezVousManager rdvManager, Scanner scanner) {
        System.out.println("=== ESPACE PRESTATAIRE ===");
        System.out.println("1. S'inscrire");
        System.out.println("2. Se connecter");
        System.out.print("Votre choix : ");
        int choix = Integer.parseInt(scanner.nextLine());

        switch (choix) {
            case 1 -> {
                System.out.print("Nom : ");
                String nom = scanner.nextLine();
                System.out.print("Email : ");
                String email = scanner.nextLine();
                System.out.print("Mot de passe : ");
                String mdp = scanner.nextLine();
                Prestataire nouveau = new Prestataire(nom, email, mdp);
                prestataireManager.ajouterPrestataire(nouveau);
                System.out.println("Inscription prestataire réussie !");
            }
            case 2 -> {
                System.out.print("Email : ");
                String email = scanner.nextLine();
                System.out.print("Mot de passe : ");
                String mdp = scanner.nextLine();
                Prestataire p = prestataireManager.getPrestataireParEmail(email);
                if (p != null && p.getMotDePasse().equals(mdp)) {
                    System.out.println("Bienvenue " + p.getNom() + " !");
                    boolean continuer = true;
                    while (continuer) {
                        System.out.println("1. Voir mes rendez-vous");
                        System.out.println("2. Ajouter une indisponibilité");
                        System.out.println("0. Déconnexion");
                        System.out.print("Votre choix : ");
                        int choix2 = Integer.parseInt(scanner.nextLine());
                        switch (choix2) {
                            case 1 -> rdvManager.afficherRendezVousPourPrestataire(p);
                            case 2 -> {
                                System.out.print("Date d'indisponibilité (YYYY-MM-DD) : ");
                                String dateStr = scanner.nextLine();
                                LocalDate date = LocalDate.parse(dateStr);
                                p.ajouterIndisponibilite(date);
                                prestataireManager.sauvegarder();
                                System.out.println("Indisponibilité ajoutée !");
                            }
                            case 0 -> {
                                System.out.println("Déconnexion prestataire.");
                                continuer = false;
                            }
                            default -> System.out.println("Choix invalide.");
                        }
                    }
                } else {
                    System.out.println("Identifiants incorrects.");
                }
            }
            default -> System.out.println("Choix invalide.");
        }
    }


    public static void menuAdmin(PrestataireManager prestataireManager, RendezVousManager rdvManager, ServiceManager serviceManager, Scanner scanner) {
        System.out.println("=== ESPACE ADMINISTRATEUR ===");
        int choix;
        do {
            System.out.println("1. Lister les prestataires");
            System.out.println("2. Ajouter un prestataire");
            System.out.println("3. Supprimer un prestataire");
            System.out.println("4. Voir tous les rendez-vous");
            System.out.println("5. Voir les statistiques");
            System.out.println("0. Retour");
            System.out.print("Votre choix : ");
            choix = Integer.parseInt(scanner.nextLine());
            switch (choix) {
                case 1 -> {
                    for (Prestataire p : prestataireManager.getPrestataires()) p.afficher();
                }
                case 2 -> {
                    System.out.print("Nom : ");
                    String nom = scanner.nextLine();
                    System.out.print("Email : ");
                    String email = scanner.nextLine();
                    System.out.print("Mot de passe : ");
                    String mdp = scanner.nextLine();
                    prestataireManager.ajouterPrestataire(new Prestataire(nom, email, mdp));
                    System.out.println(" Prestataire ajouté !");
                }
                case 3 -> {
                    System.out.print("Email du prestataire à supprimer : ");
                    String email = scanner.nextLine();
                    Prestataire aSupprimer = prestataireManager.getPrestataireParEmail(email);
                    if (aSupprimer != null) {
                        prestataireManager.getPrestataires().remove(aSupprimer);
                        System.out.println(" Prestataire supprimé !");
                    } else {
                        System.out.println(" Introuvable.");
                    }
                }
                case 4 -> rdvManager.afficherTous();
                case 5 -> {
                    System.out.println("Statistiques :");
                    System.out.println("Nombre de services : " + serviceManager.getAll().size());
                    System.out.println("Nombre de prestataires : " + prestataireManager.getPrestataires().size());
                    System.out.println("Nombre de rendez-vous : " + rdvManager.getAll().size());
                }
                case 0 -> System.out.println(" Retour au menu principal.");
                default -> System.out.println(" Choix invalide.");
            }
        } while (choix != 0);
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
            System.out.println("6. Espace prestataire");
            System.out.println("7. Espace administrateur");
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
                case 6 -> menuPrestataire(prestataireManager, rdvManager, scanner);
                case 7 -> menuAdmin(prestataireManager, rdvManager, manager, scanner);
                case 0 -> System.out.println("Au revoir !");
                default -> System.out.println("Choix invalide.");
            }
        } while (choix != 0);
        scanner.close();
    }
}
