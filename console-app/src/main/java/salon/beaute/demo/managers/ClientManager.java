package salon.beaute.demo.managers;

import salon.beaute.demo.models.Client;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientManager {
    private static final String FILE_PATH = "src/data/clients.csv";
    private List<Client> clients;

    public ClientManager() {
        this.clients = new ArrayList<>();
        chargerClients();
    }

    public Client getClientParNom(String nom) {
        for (Client c : clients) {
            if (c.getNom().equalsIgnoreCase(nom)) return c;
        }
        return null;
    }

    private void chargerClients() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                clients.add(Client.fromCSV(ligne));
            }
        } catch (IOException e) {
            System.out.println(" Impossible de charger les clients : " + e.getMessage());
        }
    }

    public void ajouterClient(Client client) {
        clients.add(client);
        sauvegarder();
    }

    private void sauvegarder() {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Client c : clients) {
                writer.write(c.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(" Erreur lors de la sauvegarde : " + e.getMessage());
        }
        System.out.println(" Clients enregistrés dans le fichier.");
    }

    public List<Client> getClients() {
        return clients;
    }

    public void afficherClients() {
        for (Client c : clients) {
            c.afficher();
        }
    }
}
