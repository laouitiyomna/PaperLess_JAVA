package com.java.paperless_miniprojet.controller;

import com.java.paperless_miniprojet.util.ConnexionDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label kpiTotal;
    @FXML private Label kpiObjectif;
    @FXML private Label kpiAlertes;
    @FXML private BarChart<String, Number> barChart;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chargerKPI();
        chargerGraphique();
    }

    private void chargerKPI() {
        try {
            Connection conn = ConnexionDB.getConnection();
            Statement st = conn.createStatement();

            ResultSet rs1 = st.executeQuery(
                    "SELECT SUM(quantite) AS total FROM consommations " +
                            "WHERE MONTH(date) = MONTH(CURDATE()) AND YEAR(date) = YEAR(CURDATE())"
            );
            if (rs1.next()) kpiTotal.setText(String.valueOf(rs1.getInt("total")));

            ResultSet rs2 = st.executeQuery(
                    "SELECT SUM(objectif_mensuel) AS total FROM departements"
            );
            if (rs2.next()) kpiObjectif.setText(String.valueOf(rs2.getInt("total")));

            ResultSet rs3 = st.executeQuery(
                    "SELECT COUNT(*) AS nb FROM " +
                            "(SELECT c.departement, SUM(c.quantite) AS total, d.objectif_mensuel " +
                            " FROM consommations c " +
                            " JOIN departements d ON c.departement = d.nom " +
                            " WHERE MONTH(c.date) = MONTH(CURDATE()) AND YEAR(c.date) = YEAR(CURDATE()) " +
                            " GROUP BY c.departement, d.objectif_mensuel " +
                            " HAVING total > d.objectif_mensuel) AS depassements"
            );
            if (rs3.next()) kpiAlertes.setText(String.valueOf(rs3.getInt("nb")));

        } catch (Exception e) {
            System.out.println("Erreur chargerKPI : " + e.getMessage());
        }
    }

    private void chargerGraphique() {
        try {
            Connection conn = ConnexionDB.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT departement, SUM(quantite) AS total FROM consommations " +
                            "WHERE MONTH(date) = MONTH(CURDATE()) AND YEAR(date) = YEAR(CURDATE()) " +
                            "GROUP BY departement"
            );
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName("Rames consommées");
            while (rs.next()) {
                serie.getData().add(new XYChart.Data<>(
                        rs.getString("departement"),
                        rs.getInt("total")
                ));
            }
            barChart.getData().add(serie);

        } catch (Exception e) {
            System.out.println("Erreur chargerGraphique : " + e.getMessage());
        }
    }

    @FXML
    private void goToConsommation() {
        changerScene("/com/java/paperless_miniprojet/Consommation.fxml", "PaperLess - Consommation");
    }

    @FXML
    private void goToAlertes() {
        changerScene("/com/java/paperless_miniprojet/Alertes.fxml", "PaperLess - Alertes");
    }

    @FXML
    private void handleLogout() {
        changerScene("/com/java/paperless_miniprojet/Login.fxml", "PaperLess - Connexion");
    }

    private void changerScene(String fxml, String titre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Stage stage = (Stage) kpiTotal.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 800, 600));
            stage.setTitle(titre);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}