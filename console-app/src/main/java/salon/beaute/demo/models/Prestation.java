package salon.beaute.demo.models;

public class Prestation {
    protected String nom;
    protected String description;
    protected double prix;
    protected int duree;

    public Prestation(String nom, String description, double prix, int duree) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.duree = duree;
    }

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
}
