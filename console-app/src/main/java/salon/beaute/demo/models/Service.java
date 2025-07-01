package salon.beaute.demo.models;

public class Service extends Prestation {
    private String imageUrl;

    public Service(String nom, String description, double prix, int duree, String imageUrl) {
        super(nom, description, prix, duree);
        this.imageUrl = imageUrl;
    }

    public String toCSV() {
        return nom + ";" + description + ";" + prix + ";" + duree + ";" + imageUrl;
    }

    public static Service fromCSV(String line) {
        String[] parts = line.split(";");
        return new Service(parts[0], parts[1], Double.parseDouble(parts[2]),
                Integer.parseInt(parts[3]), parts[4]);
    }

    @Override
    public void afficher() {
        System.out.println(nom + " - " + description + " - " + prix + "€ - " + duree + "min");
    }

    // --- Getters et Setters ---

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
