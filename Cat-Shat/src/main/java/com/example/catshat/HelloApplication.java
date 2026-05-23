package com.example.catshat;

import com.example.catshat.database.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // 1. Initialize the SQLite database right when the app starts
        DatabaseManager.initializeDatabase();

        // 2. Load your newly renamed login screen
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));

        // 400x500 matches the dimensions we put in the FXML layout
        Scene scene = new Scene(fxmlLoader.load(), 400, 500);

        stage.setTitle("Cat-Shat RPG - Login");
        stage.setScene(scene);
        stage.setResizable(false); // Keeps the login screen nice and tidy
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}