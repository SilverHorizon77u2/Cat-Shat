package com.example.catshat.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    public static boolean registerUser(String username, String password) {
        String insertSQL = "INSERT INTO users(username, password) VALUES(?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true; // Registration successful

        } catch (SQLException e) {
            // This will trigger if the username already exists (UNIQUE constraint violation)
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean loginUser(String username, String password) {
        String querySQL = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(querySQL)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            return rs.next(); // Returns true if a match was found

        } catch (SQLException e) {
            System.err.println("Login query failed: " + e.getMessage());
            return false;
        }
    }

    public static void saveScore(String username, int score) {
        String insertScoreSQL = "INSERT INTO leaderboard (user_id, score_value) " +
                "VALUES ((SELECT id FROM users WHERE username = ?), ?);";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertScoreSQL)) { // Fixed line here!

            pstmt.setString(1, username);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
            System.out.println("Score of " + score + " saved successfully for user: " + username);
        } catch (SQLException e) {
            System.err.println("Failed to save score transaction: " + e.getMessage());
        }
    }

    public static int getPersonalHighScore(String username) {
        String querySQL = "SELECT MAX(score_value) AS high_score FROM leaderboard " +
                "WHERE user_id = (SELECT id FROM users WHERE username = ?);";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(querySQL)) {

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("high_score"); // Returns 0 if no scores exist yet
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching high score: " + e.getMessage());
        }
        return 0;
    }

    public static int getMostRecentScore(String username) {
        String querySQL = "SELECT score_value FROM leaderboard " +
                "WHERE user_id = (SELECT id FROM users WHERE username = ?) " +
                "ORDER BY score_id DESC LIMIT 1;";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(querySQL)) {

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("score_value");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching recent score: " + e.getMessage());
        }
        return 0;
    }

}