#  Salon de Beauté - Application Java Console

Bienvenue dans notre projet de gestion d’un **salon de beauté** réalisé en Java, exécuté via une interface **console (CLI)**.

Ce programme permet de :
- Gérer des services de beauté (ajout, suppression, recherche…)
- Gérer un espace client (inscription, connexion)
- Réserver des rendez-vous
- Voir ses rendez-vous
- Et bien plus encore…


## Structure du projet

console-app/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ ├── org/example/Main.java ← Point d'entrée
│ │ │ ├── salon/beaute/demo/models/ ← Modèles : Client, Service, RendezVous
│ │ │ ├── salon/beaute/demo/managers/ ← Managers : gestion de fichiers & logique (ClientManager, ServiceManager ...)
│ ├── data/
│ │ ├── clients.csv
│ │ ├── services.csv
│ │ └── rendezvous.csv



##  Fonctionnalités développées

###  1. Gestion des services
- Lister tous les services
- Ajouter un service (nom, description, prix, durée, image)
- Supprimer un service
- Rechercher un service par nom

 Stockage : `services.csv`  
 Classe : `Service.java`  
 Manager : `ServiceManager.java`


### 2. Espace client
- Inscription (nom, email, téléphone, mot de passe)
- Connexion
- Affichage d’un message de bienvenue après authentification

 Stockage : `clients.csv`  
 Classe : `Client.java`  
 Manager : `ClientManager.java`


### 3. Réservations (Rendez-vous)
- Réserver un service à une date
- Afficher les rendez-vous d’un client connecté

Les ID de RDV sont générés automatiquement au format **RDV001, RDV002…**  
Les données sont persistées dans un fichier `.csv`

 Stockage : `rendezvous.csv`  
 Classe : `RendezVous.java`  
 Manager : `RendezVousManager.java`


### 4. Validation et gestion des erreurs
- Gestion des erreurs de saisie (`try/catch`)
- Contrôle du mot de passe (confirmation)
- Vérification de format de date (`LocalDate.parse`)
- Messages d'erreurs clairs pour les utilisateurs


##  Exemple d’exécution

=== MENU PRINCIPAL ===
1. Lister les services
2. Ajouter un service
3. Supprimer un service
4. Rechercher un service
5. Espace client
0. Quitter

=== ESPACE CLIENT ===
1. S'inscrire
2. Se connecter

=== MENU CLIENT CONNECTÉ ===
1. Réserver un rendez-vous
2. Voir mes rendez-vous
0. Retour

## Technologies utilisées

1. Java 21
2. Fichiers .csv pour la persistance des données
3. Scanner pour l’interaction console
4. LocalDate pour la gestion des dates

## Lancer le projet

Cloner le dépôt :
git clone https://github.com/<votre-repo>/Salon-de-beaute.git
Ouvrir dans IntelliJ (ou autre IDE Java)
Lancer Main.java

## Améliorations possibles

Ajout d’une interface graphique 
Enregistrement en base de données (MySQL, PostgreSQL…)
Gestion des rôles (Admin, Employé…)
Gestion de disponibilité du personnel
Système d’avis sur les services




