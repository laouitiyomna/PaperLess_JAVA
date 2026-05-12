package com.java.paperless_miniprojet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/paperless?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection = null;

    // Bloc static pour charger le driver une seule fois
    static {
        try {
            // Charger le driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL chargé avec succès !");
        } catch (ClassNotFoundException e) {
            System.err.println("ERREUR: Driver MySQL non trouvé !");
            System.err.println("Ajoutez la dépendance dans pom.xml");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion à la base de données réussie !");
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("ERREUR de connexion à la base : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion fermée.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture : " + e.getMessage());
        }
    }
}