package salon.beaute.demo.models;

public class Avis {
    private String clientEmail;
    private String prestataireEmail;
    private int note; // 1 à 5
    private String commentaire;

    public Avis(String clientEmail, String prestataireEmail, int note, String commentaire) {
        this.clientEmail = clientEmail;
        this.prestataireEmail = prestataireEmail;
        this.note = note;
        this.commentaire = commentaire;
    }
    public String getPrestataireEmail() {
        return prestataireEmail;
    }

    public String toCSV() {
        return clientEmail + ";" + prestataireEmail + ";" + note + ";" + commentaire;
    }

    public static Avis fromCSV(String line) {
        String[] parts = line.split(";");
        return new Avis(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3]);
    }

    public void afficher() {
        System.out.println("Note : " + note + "/5 | " + commentaire + " (par " + clientEmail + ")");
    }
}
