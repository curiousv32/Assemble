package com.example.assemble.service;
import static com.example.assemble.database.DatabaseManager.usingSQLDatabase;
import static java.sql.DriverManager.getConnection;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.assemble.database.DatabaseManager;
import com.example.assemble.model.Flashcard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlashcardManager {
    private static final String SHARED_PREF_NAME = "AssemblePrefs";
    private DatabaseManager dbManager;
    private boolean useSQLDatabase;
    public FlashcardManager(Context context) {
        this.dbManager = DatabaseManager.getInstance(context);
        this.useSQLDatabase = usingSQLDatabase();
    }

    public void addFlashcard(String username, String question, String answer) {
        if (useSQLDatabase) {
            String sql = "INSERT INTO flashcards (username, question, answer) VALUES (?, ?, ?)";
            try (Connection connection = dbManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, question);
                preparedStatement.setString(3, answer);
                preparedStatement.executeUpdate();
                // Integrity violation constraint here because the user is not found in the users table
            } catch (SQLException e) {
                Log.e("database error", "Error adding flashcard: " + e.getMessage());
            }
        } else {
            // stub implementation
        }
    }

    public void deleteFlashcard(String username, String question) {
        if (useSQLDatabase) {
            String sql = "DELETE FROM flashcards WHERE username = ? AND question = ?";
            try (Connection connection = dbManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, question);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                Log.e("database error", "Error deleting flashcard: " + e.getMessage());
            }
        } else {
            // stub implementation
        }
    }

    public List<Flashcard> getFlashcards(String username) {
        if (useSQLDatabase) {
            List<Flashcard> flashcards = new ArrayList<>();
            String sql = "SELECT * FROM flashcards WHERE username = ?";
            try (Connection connection = dbManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String userName = resultSet.getString("username");
                        String question = resultSet.getString("question");
                        String answer = resultSet.getString("answer");
                        flashcards.add(new Flashcard(userName, question, answer));
                    }
                }
            } catch (SQLException e) {
                Log.e("database error", "Error retrieving flashcards: " + e.getMessage());
            }
            return flashcards;
        } else {
            // stub implementation
            List<Flashcard> flashcards = new ArrayList<>();
            return flashcards;
        }
    }
}
