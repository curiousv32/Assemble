package com.example.assemble.database;

import android.util.Log;

import com.example.assemble.model.Note;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {

    private static String DB_NAME = "assemble";
    private static final String JDBC_URL = "jdbc:hsqldb:file:";
    private static final String JDBC_USER = "SA";
    private final String JDBC_PASSWORD = "";

    public static final String STUB_NOTE_NAME = "stub";

    private List<Note> stubNote = new ArrayList<Note>() {{
        add(new Note(UUID.randomUUID(), STUB_NOTE_NAME));
    }};

    public static final String STUB_USER = "admin";
    public static final String STUB_PASSWORD = "admin";

    private static final DatabaseManager REFERENCE = new DatabaseManager();

    public DatabaseManager() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            Log.e("database error", e.getException().getMessage() +  " " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL + DB_NAME, JDBC_USER, JDBC_PASSWORD);
    }

    public static DatabaseManager getInstance() {
        return REFERENCE;
    }

    public List<Note> getUserNotes(String ownerUUID) {
        return stubNote;
    }
}
