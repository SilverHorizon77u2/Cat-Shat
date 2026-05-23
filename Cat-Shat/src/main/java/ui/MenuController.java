package com.example.catshat.ui;

import com.example.catshat.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuController {

    @FXML private Label welcomeLabel;

    public void setUsername(String username) {
        welcomeLabel.setText("Welcome back, " + username + "!");
    }

    @FXML
    private void handleStartGame() {
        try {
            System.out.println("Generating auto-random isometric world...");

            // Get the current window stage
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();

            // Load your new split layout game view
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game.fxml"));

            // Open it nice and wide (950x650) to beautifully fit the side panel + game canvas
            Scene scene = new Scene(fxmlLoader.load(), 950, 650);

            stage.setScene(scene);
            stage.setTitle("Cat-Shat RPG - Game World");
            stage.centerOnScreen();

        } catch (IOException e) {
            System.err.println("Failed to launch game scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 500); // Reset size back to login dimensions
            stage.setScene(scene);
            stage.setTitle("Cat-Shat RPG - Login");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}