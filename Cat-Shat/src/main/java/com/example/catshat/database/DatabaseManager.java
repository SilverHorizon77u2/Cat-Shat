package com.example.catshat.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:game.db";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite Driver not found! Make sure it's in pom.xml: " + e.getMessage());
        }

        Connection conn = DriverManager.getConnection(URL);
        // Explicitly enable Foreign Key support in SQLite for this connection session
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    public static void initializeDatabase() {
        // 1. Existing Users Table
        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL" +
                ");";

        // 2. New Historical Leaderboard Table
        String createLeaderboardTableSQL = "CREATE TABLE IF NOT EXISTS leaderboard (" +
                "score_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "score_value INTEGER NOT NULL, " +
                "date_achieved TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createUsersTableSQL);
            stmt.execute(createLeaderboardTableSQL);

            System.out.println("Database initialized: users and leaderboard tables are ready.");
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }
}