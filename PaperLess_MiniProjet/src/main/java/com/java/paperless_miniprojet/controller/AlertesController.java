package com.java.paperless_miniprojet.controller;



import com.java.paperless_miniprojet.model.Alerte;
import com.java.paperless_miniprojet.util.ConnexionDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AlertesController implements Initializable {

    @FXML private TableView<Alerte>            tableAlertes;
    @FXML private TableColumn<Alerte, String>  colDept;
    @FXML private TableColumn<Alerte, Integer> colConsomme;
    @FXML private TableColumn<Alerte, Integer> colObjectif;
    @FXML private TableColumn<Alerte, Integer> colEcart;
    @FXML private TableColumn<Alerte, String>  colStatut;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colDept.setCellValueFactory(new PropertyValueFactory<>("departement"));
        colConsomme.setCellValueFactory(new PropertyValueFactory<>("consomme"));
        colObjectif.setCellValueFactory(new PropertyValueFactory<>("objectif"));
        colEcart.setCellValueFactory(new PropertyValueFactory<>("ecart"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        chargerAlertes();
    }

    private void chargerAlertes() {
        ObservableList<Alerte> liste = FXCollections.observableArrayList();
        try {
            Connection conn = ConnexionDB.getConnection();
            if (conn == null) {
                System.out.println("Connexion null !");
                return;
            }

            Statement st = conn.createStatement();

            // Requête simplifiée sans MONTH/YEAR
            ResultSet rs = st.executeQuery(
                    "SELECT d.nom AS departement, " +
                            "COALESCE(SUM(c.quantite), 0) AS consomme, " +
                            "d.objectif_mensuel AS objectif, " +
                            "(COALESCE(SUM(c.quantite), 0) - d.objectif_mensuel) AS ecart " +
                            "FROM departements d " +
                            "LEFT JOIN consommations c ON c.departement = d.nom " +
                            "GROUP BY d.nom, d.objectif_mensuel " +
                            "ORDER BY ecart DESC"
            );

            int i = 1;
            while (rs.next()) {
                int consomme = rs.getInt("consomme");
                int objectif = rs.getInt("objectif");
                int ecart    = rs.getInt("ecart");

                String statut;
                if (ecart > 0)        statut = "DEPASSE";
                else if (ecart >= -100) statut = "PROCHE";
                else                   statut = "OK";

                System.out.println("Alerte : " + rs.getString("departement")
                        + " consomme=" + consomme
                        + " objectif=" + objectif
                        + " statut=" + statut);

                liste.add(new Alerte(i++,
                        rs.getString("departement"),
                        consomme, objectif, ecart, statut
                ));
            }

            System.out.println("Total alertes chargées : " + liste.size());
            tableAlertes.setItems(liste);

        } catch (Exception e) {
            System.out.println("Erreur AlertesController : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleActualiser() {
        chargerAlertes();
    }

    @FXML
    private void goToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/java/paperless_miniprojet/Dashboard.fxml"));
            Stage stage = (Stage) tableAlertes.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 800, 600));
            stage.setTitle("PaperLess - Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}