package salon.beaute.demo.utils;

import salon.beaute.demo.models.Client;
import salon.beaute.demo.models.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvUtils {
    private static final String SERVICE_FILE_PATH = "./data/services.csv";
    private static final String CLIENT_FILE_PATH = "./data/clients.csv";

    // ===== SERVICES =====
    public static List<Service> lireServices() {
        List<Service> services = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SERVICE_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                services.add(Service.fromCSV(line));
            }
        } catch (IOException e) {
            System.out.println("Erreur lecture services : " + e.getMessage());
        }
        return services;
    }

    public static void ecrireServices(List<Service> services) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SERVICE_FILE_PATH))) {
            for (Service s : services) {
                writer.write(s.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erreur écriture services : " + e.getMessage());
        }
    }

    // ===== CLIENTS =====
    public static List<Client> lireClients() {
        List<Client> clients = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CLIENT_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                clients.add(Client.fromCSV(line));
            }
        } catch (IOException e) {
            System.out.println("Erreur lecture clients : " + e.getMessage());
        }
        return clients;
    }

    public static void ecrireClients(List<Client> clients) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CLIENT_FILE_PATH))) {
            for (Client c : clients) {
                writer.write(c.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erreur écriture clients : " + e.getMessage());
        }
    }
}
