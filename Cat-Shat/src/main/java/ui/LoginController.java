package com.example.catshat.ui;

import com.example.catshat.HelloApplication;
import com.example.catshat.database.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Please enter both username and password.");
            return;
        }

        boolean success = AuthService.loginUser(username, password);

        if (success) {
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Login successful! Loading Main Menu...");

            try {
                // Get the current window stage
                Stage stage = (Stage) statusLabel.getScene().getWindow();

                // Load the new main menu FXML layout
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menu.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);

                // Access the menu controller to display the user's name customly
                MenuController menuController = fxmlLoader.getController();
                menuController.setUsername(username);

                // Change the scene on the stage and re-center the window
                stage.setScene(scene);
                stage.setTitle("Cat-Shat RPG - Main Menu");
                stage.centerOnScreen();

            } catch (Exception e) {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("Error loading main menu layout.");
                e.printStackTrace();
            }
        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    private void handleSignup() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Please enter both username and password.");
            return;
        }

        boolean success = AuthService.registerUser(username, password);
        if (success) {
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Account created successfully! You can now log in.");
        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Username already taken. Try another one.");
        }
    }
}