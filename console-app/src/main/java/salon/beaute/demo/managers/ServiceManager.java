package salon.beaute.demo.managers;

import salon.beaute.demo.models.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceManager {

    private static final String FILE_PATH = "src/data/services.csv";
    private List<Service> services;

    public ServiceManager() {
        this.services = chargerDepuisCSV();
    }

    private List<Service> chargerDepuisCSV() {
        List<Service> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                list.add(Service.fromCSV(ligne));
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
        return list;
    }

    private void sauvegarderVersCSV() {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Service s : services) {
                writer.write(s.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier : " + e.getMessage());
        }
        System.out.println(" Enregistrement des services dans le fichier CSV...");
    }


    public List<Service> getAll() {
        return services;
    }

    public void ajouter(Service service) {
        services.add(service);
        sauvegarderVersCSV();
    }

    public boolean supprimerParNom(String nom) {
        boolean removed = services.removeIf(s -> s.getNom().equalsIgnoreCase(nom));
        if (removed) sauvegarderVersCSV();
        return removed;
    }

    public Service chercherParNom(String nom) {
        for (Service s : services) {
            if (s.getNom().equalsIgnoreCase(nom)) {
                return s;
            }
        }
        return null;
    }

    public void afficherTous() {
        for (Service s : services) {
            s.afficher();
        }
    }
}
