package com.example.myapplication.database;

import com.example.myapplication.model.Note;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {

    private List<Note> stubNote = new ArrayList<Note>() {{
        add(new Note(UUID.randomUUID(), "stub"));
    }};

    private static DatabaseManager reference = new DatabaseManager();

    public static DatabaseManager getInstance() {
        return reference;
    }

    public List<Note> getUserNotes(String ownerUUID) {
        return stubNote;
    }

    public void updateUserNote(Note note, String newText) {
        stubNote.get(0).setText(newText);
    }
}
