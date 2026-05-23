package com.example.catshat.ui;

import com.example.catshat.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Random;

public class GameController {

    @FXML private Canvas gameCanvas;
    @FXML private Label coordsLabel;

    // Track score points in runtime memory
    private int currentScore = 0;
    private String currentUsername = "g"; // Default fallback, or pass this from MenuController!

    private static final int MAP_SIZE = 12;
    private int[][] worldMap = new int[MAP_SIZE][MAP_SIZE];

    private static final int TILE_WIDTH = 64;
    private static final int TILE_HEIGHT = 32;

    private int catRow = 3;
    private int catCol = 3;

    public void initialize() {
        generateRandomWorld();

        javafx.application.Platform.runLater(() -> {
            drawGame();
            // This grabs focus on the canvas right away so arrow keys work immediately without clicking
            gameCanvas.requestFocus();
        });
    }

    private void generateRandomWorld() {
        Random rand = new Random();
        for (int r = 0; r < MAP_SIZE; r++) {
            for (int c = 0; c < MAP_SIZE; c++) {
                worldMap[r][c] = (rand.nextFloat() < 0.15f) ? 1 : 0;
            }
        }
        worldMap[catRow][catCol] = 0; // Clear spawn point
    }

    private void drawGame() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        // Centered for the new 750px canvas container width
        double offsetX = gameCanvas.getWidth() / 2;
        double offsetY = 120;

        for (int r = 0; r < MAP_SIZE; r++) {
            for (int c = 0; c < MAP_SIZE; c++) {
                double isoX = (c - r) * (TILE_WIDTH / 2.0) + offsetX;
                double isoY = (c + r) * (TILE_HEIGHT / 2.0) + offsetY;

                if (worldMap[r][c] == 1) {
                    drawIsoTile(gc, isoX, isoY, Color.web("#7f8c8d"), Color.web("#95a5a6"));
                } else {
                    drawIsoTile(gc, isoX, isoY, Color.web("#27ae60"), Color.web("#2ecc71"));
                }

                if (r == catRow && c == catCol) {
                    drawCat(gc, isoX, isoY);
                }
            }
        }
        coordsLabel.setText("Cat Grid Position:\nRow: " + catRow + ", Col: " + catCol);
    }

    private void drawIsoTile(GraphicsContext gc, double x, double y, Color border, Color fill) {
        double[] xPoints = {x, x + TILE_WIDTH/2.0, x, x - TILE_WIDTH/2.0};
        double[] yPoints = {y, y + TILE_HEIGHT/2.0, y + TILE_HEIGHT, y + TILE_HEIGHT/2.0};

        gc.setFill(fill);
        gc.fillPolygon(xPoints, yPoints, 4);
        gc.setStroke(border);
        gc.setLineWidth(1.0);
        gc.strokePolygon(xPoints, yPoints, 4);
    }

    private void drawCat(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.web("#e67e22")); // Orange Cat
        gc.fillOval(x - 12, y - 10, 24, 24);

        double[] leftEarX = {x - 12, x - 12, x - 4};
        double[] leftEarY = {y - 4, y - 18, y - 8};
        gc.fillPolygon(leftEarX, leftEarY, 3);

        double[] rightEarX = {x + 12, x + 12, x + 4};
        double[] rightEarY = {y - 4, y - 18, y - 8};
        gc.fillPolygon(rightEarX, rightEarY, 3);

        gc.setFill(Color.BLACK);
        gc.fillOval(x - 5, y - 4, 3, 3);
        gc.fillOval(x + 2, y - 4, 3, 3);
    }

    @FXML
    private void handleMovement(KeyEvent event) {
        int targetRow = catRow;
        int targetCol = catCol;

        switch (event.getCode()) {
            case W, UP:    targetRow--; break;
            case S, DOWN:  targetRow++; break;
            case A, LEFT:  targetCol--; break;
            case D, RIGHT: targetCol++; break;
            default: return;
        }

        if (targetRow >= 0 && targetRow < MAP_SIZE && targetCol >= 0 && targetCol < MAP_SIZE) {
            if (worldMap[targetRow][targetCol] == 0) {
                catRow = targetRow;
                catCol = targetCol;

                // Add 10 points for every successful isometric tile movement step!
                currentScore += 10;

                drawGame();
            }
        }
    }

    // Side panel interactive buttons!
    @FXML
    private void handleMeow() {
        System.out.println("MEOW! 🐱");
        // You could hook up an audio clip or open a dialog bubble here!
    }

    @FXML
    private void handleRegenWorld() {
        generateRandomWorld();
        drawGame();
        gameCanvas.requestFocus(); // Hand focus back to canvas so movement controls don't drop out
    }

    @FXML
    private void handleExitToMenu() {
        try {
            // LOCK IT IN: Save the run score directly to the leaderboard table on exit!
            if (currentScore > 0) {
                com.example.catshat.database.AuthService.saveScore(currentUsername, currentScore);
            }

            Stage stage = (Stage) gameCanvas.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menu.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setScene(scene);
            stage.setTitle("Cat-Shat RPG - Main Menu");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}