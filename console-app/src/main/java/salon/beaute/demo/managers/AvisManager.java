package salon.beaute.demo.managers;

import salon.beaute.demo.models.Avis;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AvisManager {
    private static final String FILE_PATH = "src/data/avis.csv";
    private List<Avis> avisList;

    public AvisManager() {
        this.avisList = new ArrayList<>();
        chargerAvis();
    }

    private void chargerAvis() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                avisList.add(Avis.fromCSV(ligne));
            }
        } catch (IOException e) {
            System.out.println("Impossible de charger les avis: " + e.getMessage());
        }
    }

    public void ajouterAvis(Avis avis) {
        avisList.add(avis);
        sauvegarder();
    }

    private void sauvegarder() {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Avis a : avisList) {
                writer.write(a.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    public List<Avis> getAvisPourPrestataire(String emailPrestataire) {
        List<Avis> result = new ArrayList<>();
        for (Avis a : avisList) {
            if (a.getPrestataireEmail().equals(emailPrestataire)) {
                result.add(a);
            }
        }
        return result;
    }
}
