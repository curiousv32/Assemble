package com.example.assemble.database;

import com.example.assemble.model.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {

    public static final String STUB_NOTE_NAME = "stub";

    private List<Note> stubNote = new ArrayList<Note>() {{
        add(new Note(UUID.randomUUID(), STUB_NOTE_NAME));
    }};

    public static final String STUB_USER = "admin";
    public static final String STUB_PASSWORD = "admin";

    private static DatabaseManager reference = new DatabaseManager();

    public static DatabaseManager getInstance() {
        return reference;
    }

    public List<Note> getUserNotes(String ownerUUID) {
        return stubNote;
    }
}
