package com.example.assemble.database;

import android.content.Context;
import android.util.Log;

import com.example.assemble.model.Flashcard;
import com.example.assemble.model.Note;

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

    public void initDB(){
        try (Connection connection = DriverManager.getConnection(this.dbPath, JDBC_USER, JDBC_PASSWORD);
             Statement statement = connection.createStatement()) {
            // Create Users table
            String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER IDENTITY PRIMARY KEY,"
                    + "username VARCHAR(255) NOT NULL UNIQUE,"
                    + "password VARCHAR(255) NOT NULL"
                    + ")";
            statement.executeUpdate(createUsersTableSQL);
            // Create flashcards table
            String createFlashcardsTableSQL = "CREATE TABLE IF NOT EXISTS flashcards ("
                    + "id INTEGER IDENTITY PRIMARY KEY,"
                    + "username VARCHAR(255) NOT NULL,"
                    + "question VARCHAR(255) NOT NULL,"
                    + "answer VARCHAR(255) NOT NULL,"
                    + "FOREIGN KEY (username) REFERENCES users(username)"
                    + ")";
            statement.executeUpdate(createFlashcardsTableSQL);
            System.out.println("Database setup completed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void addUser(String userName, String password) {
//        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
//        try (Connection connection = getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setString(1, userName);
//            preparedStatement.setString(2, password);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            Log.e("database error", "Error adding user: " + e.getMessage());
//        }
//    }
//
//    public User getUser(String userName) {
//        String sql = "SELECT * FROM users WHERE username = ?";
//        try (Connection connection = getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setString(1, userName);
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                if (resultSet.next()) {
//                    String username = resultSet.getString("username");
//                    String password = resultSet.getString("password");
//                    return new User(username, password);
//                }
//            }
//        } catch (SQLException e) {
//            Log.e("database error", "Error retrieving user: " + e.getMessage());
//        }
//        return null;
//    }
//
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbPath, JDBC_USER, JDBC_PASSWORD);
    }
//
    public static boolean usingSQLDatabase() {
        return useSQLDatabase;
    }

    public static void setUseSQLDatabase(boolean useSQLDatabase) {
        DatabaseManager.useSQLDatabase = useSQLDatabase;
    }
//
    public void clearDatabase(){
        try (Connection connection = DriverManager.getConnection(this.dbPath, JDBC_USER, JDBC_PASSWORD);
             Statement statement = connection.createStatement()) {
            String dropFlashcardsTableSQL = "DROP TABLE IF EXISTS flashcards;";
            String dropUsersTableSQL = "DROP TABLE IF EXISTS users";
            statement.executeUpdate(dropFlashcardsTableSQL);
            statement.executeUpdate(dropUsersTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
