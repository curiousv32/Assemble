package com.example.assemble.database;

import android.content.Context;
import android.util.Log;

import com.example.assemble.model.Note;
import com.example.assemble.model.UserProfile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {

    private static boolean useSQLDatabase = true;
    private static String DB_NAME = "assemble";
    private static final String JDBC_USER = "SA";
    private static final String JDBC_PASSWORD = "";
    private String dbPath;
    private static DatabaseManager REFERENCE;

    public static final String STUB_NOTE_NAME = "stub";
    private List<Note> stubNote = new ArrayList<Note>() {{
        add(new Note(UUID.randomUUID(), STUB_NOTE_NAME));
    }};

    public static final String STUB_USER = "admin";
    public static final String STUB_PASSWORD = "admin";


    public DatabaseManager(Context context) {
        this.dbPath = "jdbc:hsqldb:file:" + context.getFilesDir().getPath() + "/" + DB_NAME;
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            //clearDatabase();
        } catch (ClassNotFoundException e) {
            Log.e("database error", e.getMessage());
        }
        //initDB();
    }

    public static synchronized DatabaseManager getInstance(Context context) {
        if (REFERENCE == null) {
            REFERENCE = new DatabaseManager(context);
        }
        return REFERENCE;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbPath, JDBC_USER, JDBC_PASSWORD);
    }

    public static boolean usingSQLDatabase() {
        return useSQLDatabase;
    }

    public static void setUseSQLDatabase(boolean useSQLDatabase) {
        DatabaseManager.useSQLDatabase = useSQLDatabase;
    }

    public void clearDatabase(){
        try (Connection connection = DriverManager.getConnection(this.dbPath, JDBC_USER, JDBC_PASSWORD);
             Statement statement = connection.createStatement()) {
            String dropFlashcardsTableSQL = "DROP TABLE IF EXISTS flashcards";
            String dropUsersTableSQL = "DROP TABLE IF EXISTS users";
            String notesTableSQL = "DROP TABLE IF EXISTS notes";
            statement.executeUpdate(dropFlashcardsTableSQL);
            statement.executeUpdate(dropUsersTableSQL);
            statement.executeUpdate(notesTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void runUpdateQuery(String query, Object... parameters) {
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(query);

            for (int i = 1; i <= parameters.length; i++) {
                statement.setObject(i, parameters[i - 1]);
            }

            statement.executeUpdate();
        } catch (SQLException exception) {
            //Log.e("DBError", "Something went wrong on runQuery: " + query);
            exception.printStackTrace();
        }
    }

    // UserProfile methods
    public void createUserProfileTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS userprofile " +
                "(id IDENTITY PRIMARY KEY, " +
                "username VARCHAR(50), " +
                "password VARCHAR(50))";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(createTableSQL)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            Log.e("database error", "Error creating user profile table", e);
        }
    }

    public void addUserProfile(String username, String password) {
        String insertSQL = "INSERT INTO userprofile (username, password) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Log.e("database error", "Error inserting user profile", e);
        }
    }

    public UserProfile getUserProfile(String username) {
        String querySQL = "SELECT id, username, password FROM userprofile WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(querySQL)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UserProfile userProfile = new UserProfile();
                userProfile.setId(rs.getLong("id"));
                userProfile.setUsername(rs.getString("username"));
                userProfile.setPassword(rs.getString("password"));
                return userProfile;
            }
        } catch (SQLException e) {
            Log.e("database error", "Error fetching user profile", e);
        }
        return null;
    }

    // Update user profile password by username
    public void updateUserProfile(String currentUsername, String newUsername, String newPassword) {
        String updateSQL = "UPDATE userprofile SET username = ?, password = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
            stmt.setString(1, newUsername);
            stmt.setString(2, newPassword);
            stmt.setString(3, currentUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Log.e("database error", "Error updating user profile", e);
        }
    }

    // Delete user profile by username
    public void deleteUserProfile(String username) {
        String deleteSQL = "DELETE FROM userprofile WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Log.e("database error", "Error deleting user profile", e);
        }
    }
}
