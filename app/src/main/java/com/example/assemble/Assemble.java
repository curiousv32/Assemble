package com.example.assemble;

import android.app.Application;
import android.content.Context;
import com.example.assemble.database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Assemble extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initializeDatabase();
    }

    public static Context getAppContext() {
        return context;
    }

    private void initializeDatabase() {
        DatabaseManager dbManager = DatabaseManager.getInstance(this);

        try (Connection conn = dbManager.getConnection()) {

            String createUserTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id CHAR(36) PRIMARY KEY, " +
                    "username VARCHAR(255) NOT NULL UNIQUE, " +
                    "password VARCHAR(255) NOT NULL)";
            try (PreparedStatement stmt = conn.prepareStatement(createUserTable)) {
                stmt.executeUpdate();
            }

            String createNoteTable = "CREATE TABLE IF NOT EXISTS notes (" +
                    "id CHAR(36) PRIMARY KEY, " +
                    "owner_id CHAR(36), " +
                    "name VARCHAR(255) NOT NULL, " +
                    "creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "content LONGVARCHAR)";
            try (PreparedStatement stmt = conn.prepareStatement(createNoteTable)) {
                stmt.executeUpdate();
            }
            String createFlashcardsTable = "CREATE TABLE IF NOT EXISTS flashcards ("
                    + "id INTEGER IDENTITY PRIMARY KEY,"
                    + "username VARCHAR(255) NOT NULL,"
                    + "question VARCHAR(255) NOT NULL,"
                    + "answer VARCHAR(255) NOT NULL,"
                    + "FOREIGN KEY (username) REFERENCES users(username)"
                    + ")";
            try (PreparedStatement stmt = conn.prepareStatement(createFlashcardsTable)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
