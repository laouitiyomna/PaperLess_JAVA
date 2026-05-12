package com.java.paperless_miniprojet.controller;



import com.java.paperless_miniprojet.model.Consommation;
import com.java.paperless_miniprojet.util.ConnexionDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ConsommationController implements Initializable {

    @FXML private ComboBox<String>                   deptCombo;
    @FXML private TextField                          quantiteField;
    @FXML private DatePicker                         datePicker;
    @FXML private Label                              messageLabel;
    @FXML private TableView<Consommation>            tableConsommation;
    @FXML private TableColumn<Consommation, Integer> colId;
    @FXML private TableColumn<Consommation, String>  colDept;
    @FXML private TableColumn<Consommation, Integer> colQuantite;
    @FXML private TableColumn<Consommation, String>  colDate;

    private ObservableList<Consommation> liste = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chargerDepartements();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDept.setCellValueFactory(new PropertyValueFactory<>("departement"));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        chargerTableau();
    }

    private void chargerDepartements() {
        try {
            Connection conn = ConnexionDB.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT nom FROM departements");
            ObservableList<String> depts = FXCollections.observableArrayList();
            while (rs.next()) {
                depts.add(rs.getString("nom"));
            }
            deptCombo.setItems(depts);
        } catch (Exception e) {
            System.out.println("Erreur chargement départements : " + e.getMessage());
        }
    }

    private void chargerTableau() {
        liste.clear();
        try {
            Connection conn = ConnexionDB.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM consommations ORDER BY date DESC");
            while (rs.next()) {
                liste.add(new Consommation(
                        rs.getInt("id"),
                        rs.getString("departement"),
                        rs.getInt("quantite"),
                        rs.getString("date")
                ));
            }
            tableConsommation.setItems(liste);
        } catch (Exception e) {
            System.out.println("Erreur chargement tableau : " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouter() {
        String dept    = deptCombo.getValue();
        String qteStr  = quantiteField.getText().trim();
        LocalDate date = datePicker.getValue();

        if (dept == null || qteStr.isEmpty() || date == null) {
            afficherMessage("Veuillez remplir tous les champs.", "red");
            return;
        }

        try {
            int qte = Integer.parseInt(qteStr);
            Connection conn = ConnexionDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO consommations (departement, quantite, date) VALUES (?, ?, ?)"
            );
            ps.setString(1, dept);
            ps.setInt(2, qte);
            ps.setString(3, date.toString());
            ps.executeUpdate();

            afficherMessage("Consommation ajoutée avec succès.", "green");
            viderFormulaire();
            chargerTableau();

        } catch (NumberFormatException e) {
            afficherMessage("La quantité doit être un nombre entier.", "red");
        } catch (Exception e) {
            afficherMessage("Erreur lors de l'ajout.", "red");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleModifier() {
        Consommation selected = tableConsommation.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherMessage("Sélectionnez une ligne à modifier.", "red");
            return;
        }

        String dept    = deptCombo.getValue();
        String qteStr  = quantiteField.getText().trim();
        LocalDate date = datePicker.getValue();

        // Si formulaire vide : remplir avec la ligne sélectionnée
        if (dept == null || qteStr.isEmpty() || date == null) {
            deptCombo.setValue(selected.getDepartement());
            quantiteField.setText(String.valueOf(selected.getQuantite()));
            datePicker.setValue(LocalDate.parse(selected.getDate()));
            afficherMessage("Modifiez les champs puis cliquez sur Modifier.", "blue");
            return;
        }

        try {
            int qte = Integer.parseInt(qteStr);
            Connection conn = ConnexionDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE consommations SET departement=?, quantite=?, date=? WHERE id=?"
            );
            ps.setString(1, dept);
            ps.setInt(2, qte);
            ps.setString(3, date.toString());
            ps.setInt(4, selected.getId());
            ps.executeUpdate();

            afficherMessage("Consommation modifiée avec succès.", "green");
            viderFormulaire();
            chargerTableau();

        } catch (Exception e) {
            afficherMessage("Erreur lors de la modification.", "red");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSupprimer() {
        Consommation selected = tableConsommation.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherMessage("Sélectionnez une ligne à supprimer.", "red");
            return;
        }

        try {
            Connection conn = ConnexionDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM consommations WHERE id = ?"
            );
            ps.setInt(1, selected.getId());
            ps.executeUpdate();

            afficherMessage("Consommation supprimée.", "green");
            chargerTableau();

        } catch (Exception e) {
            afficherMessage("Erreur lors de la suppression.", "red");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleActualiser() {
        chargerTableau();
        messageLabel.setText("");
    }

    private void afficherMessage(String msg, String couleur) {
        messageLabel.setStyle("-fx-text-fill: " + couleur + "; -fx-font-size: 12px;");
        messageLabel.setText(msg);
    }

    private void viderFormulaire() {
        deptCombo.setValue(null);
        quantiteField.clear();
        datePicker.setValue(null);
    }

    @FXML
    private void goToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/java/paperless_miniprojet/Dashboard.fxml"));
            Stage stage = (Stage) tableConsommation.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 800, 600));
            stage.setTitle("PaperLess - Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}