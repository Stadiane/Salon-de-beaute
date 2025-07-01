package salon.beaute.demo.models;

public class Client {
    private String nom;
    private String telephone;
    private String email;
    private String motDePasse;

    public Client(String nom, String telephone, String email, String motDePasse) {
        this.nom = nom;
        this.telephone = telephone;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    public void afficher() {
        System.out.println("Client: " + nom + ", Téléphone: " + telephone + ", Email: " + email);
    }

    // --- Getters et Setters ---
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    // --- Pour sauvegarder dans CSV ---
    public String toCSV() {
        return nom + ";" + telephone + ";" + email + ";" + motDePasse;
    }

    public static Client fromCSV(String line) {
        String[] parts = line.split(";");
        if (parts.length < 4) {
            // on met une valeur vide ou par défaut
            return new Client(parts[0], parts[1], parts[2], "");
        } else {
            return new Client(parts[0], parts[1], parts[2], parts[3]);
        }
    }
}
