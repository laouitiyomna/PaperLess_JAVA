package com.java.paperless_miniprojet.controller;

import com.java.paperless_miniprojet.util.ConnexionDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML private TextField     emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label         errorLabel;

    @FXML
    private void handleLogin() {
        String email    = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        try {
            Connection conn = ConnexionDB.getConnection();
            String sql = "SELECT * FROM utilisateurs WHERE email = ? AND mot_de_passe = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/com/java/paperless_miniprojet/Dashboard.fxml"));
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(loader.load(), 800, 600));
                stage.setTitle("PaperLess - Dashboard");
            } else {
                errorLabel.setText("Email ou mot de passe incorrect.");
            }

        } catch (Exception e) {
            errorLabel.setText("Erreur de connexion à la base de données.");
            e.printStackTrace();
        }
    }
}