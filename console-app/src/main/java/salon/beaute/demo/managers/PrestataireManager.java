package salon.beaute.demo.managers;

import salon.beaute.demo.models.Prestataire;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PrestataireManager {
    private static final String FILE_PATH = "src/data/prestataires.csv";
    private List<Prestataire> prestataires;

    public PrestataireManager() {
        this.prestataires = new ArrayList<>();
        chargerPrestataires();
    }

    private void chargerPrestataires() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                prestataires.add(Prestataire.fromCSV(ligne));
            }
        } catch (IOException e) {
            System.out.println("Impossible de charger les prestataires: " + e.getMessage());
        }
    }

    public void ajouterPrestataire(Prestataire p) {
        prestataires.add(p);
        sauvegarder();
    }

    private void sauvegarder() {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Prestataire p : prestataires) {
                writer.write(p.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    public List<Prestataire> getPrestataires() {
        return prestataires;
    }

    public Prestataire getPrestataireParEmail(String email) {
        for (Prestataire p : prestataires) {
            if (p.getEmail().equalsIgnoreCase(email)) return p;
        }
        return null;
    }
}
