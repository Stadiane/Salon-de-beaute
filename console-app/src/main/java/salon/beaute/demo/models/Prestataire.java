package salon.beaute.demo.models;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class Prestataire {
    private String nom;
    private String email;
    private String motDePasse;

    public Prestataire(String nom, String email, String motDePasse) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }

    public String toCSV() {
        return nom + ";" + email + ";" + motDePasse;
    }

    public static Prestataire fromCSV(String line) {
        String[] parts = line.split(";");
        return new Prestataire(parts[0], parts[1], parts[2]);
    }

    public void afficher() {
        System.out.println("Prestataire: " + nom + ", Email: " + email);
    }
    private List<LocalDate> indisponibilites = new ArrayList<>();
    public List<LocalDate> getIndisponibilites() { return indisponibilites; }
    public void ajouterIndisponibilite(LocalDate date) { indisponibilites.add(date); }

}
