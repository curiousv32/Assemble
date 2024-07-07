package com.example.assemble.database;

import android.content.Context;
import android.util.Log;

import com.example.assemble.model.Note;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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


    private DatabaseManager(Context context) {
        this.dbPath = "jdbc:hsqldb:file:" + context.getFilesDir().getPath() + "/" + DB_NAME;
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException e) {
            Log.e("database error", e.getMessage());
        }
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
}
