package com.example.assemble.service;

import static com.example.assemble.database.DatabaseManager.usingSQLDatabase;

import android.content.Context;

import com.example.assemble.database.DatabaseManager;
import com.example.assemble.exceptions.InvalidNoteException;
import com.example.assemble.interfaces.INoteManager;
import com.example.assemble.model.Note;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class NoteManager implements INoteManager {

    public static final int MAX_NOTE_NAME_SIZE = 10;
    public static final int MIN_NOTE_SEARCH_SIZE = 3;

    private static NoteManager REFERENCE;
    private DatabaseManager dbManager;
    private HashMap<UUID, Note> notes;
    private Note openedNote;
    private boolean useSQLDatabase;
    private String ownerId;
    private final UUID defaultNoteId = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public static final String DEFAULT_NOTE_NAME = "DEFAULT NOTE";

    private NoteManager(Context context) {
        this.notes = new HashMap<>();
        this.dbManager = DatabaseManager.getInstance(context);
        this.useSQLDatabase = usingSQLDatabase();
    }

    public static synchronized NoteManager getInstance(Context context) {
        if (REFERENCE == null) {
            REFERENCE = new NoteManager(context);
        }
        return REFERENCE;
    }

    public void init(String ownerUUID) {
        notes.clear();
        List<Note> notesList;
        this.ownerId = ownerUUID;
        addDefaultItem();

        if (useSQLDatabase) {
            notesList = this.getUserNotesFromDB(ownerUUID);
            for (Note note : notesList) {
                notes.put(note.getID(), note);
            }
        }
    }

    public Note createNote(String name) throws InvalidNoteException {
        Note note = new Note(UUID.randomUUID(), name);
        notes.put(note.getID(), note);

        if (useSQLDatabase) {
            add(note);
        }
        return note;
    }

    @Override
    public void add(Note note) throws InvalidNoteException {
        if (!useSQLDatabase) {
            notes.put(note.getID(), note);
            return;
        }
        dbManager.runUpdateQuery(
                "INSERT INTO notes (id, name, creation_date, last_updated_date, content, owner_id) VALUES (?, ?, ?, ?, ?, ?)",
                note.getID().toString(),
                note.getName(),
                new java.sql.Timestamp(note.getCreationDate().getTime()),
                new java.sql.Timestamp(note.getLastUpdatedDate().getTime()),
                note.getText(),
                ownerId
        );
        notes.put(note.getID(), note);
    }

    @Override
    public Note get(UUID noteId, Class<Note> type) {
        if (!useSQLDatabase) {
            return notes.get(noteId);
        }
        Note note = null;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT id, name, creation_date, last_updated_date, content FROM notes WHERE id = ?")) {
            pstmt.setString(1, noteId.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    note = new Note(UUID.fromString(rs.getString("id")),
                            rs.getString("name"));
                    note.setCreationDate(new Date(rs.getTimestamp("creation_date").getTime()));
                    note.setLastUpdatedDate(new Date(rs.getTimestamp("last_updated_date").getTime()));
                    note.setText(rs.getString("content"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return note;
    }

    public void setText(Note note, String content) {
        note.setText(content);
        note.setLastUpdatedDate();
    }

    @Override
    public void update(UUID noteId, Note note) {

        if (useSQLDatabase) {
            dbManager.runUpdateQuery(
                    "UPDATE notes SET name=?, last_updated_date=CURRENT_TIMESTAMP, content=? WHERE id = ?",
                    note.getName(),
                    note.getText(),
                    noteId.toString()
            );
        }
    }



    @Override
    public void delete(UUID noteId) {
        if (!useSQLDatabase) {
            notes.remove(noteId);
            return;
        }

        dbManager.runUpdateQuery("DELETE FROM notes WHERE id=?", noteId.toString());
        notes.remove(noteId);
    }

    public void rename(Note note, String newName) {
        note.setName(newName);

        if (useSQLDatabase) {
            dbManager.runUpdateQuery("UPDATE notes SET name=? WHERE id=?", newName, note.getID());
        }
    }
    @Override
    public void addDefaultItem() {
        try {
            Note note = new Note(defaultNoteId, DEFAULT_NOTE_NAME);
            add(note);
        } catch (InvalidNoteException e) {
            e.printStackTrace();
        }
    }

    public Note getOpenNote() {
        return openedNote;
    }

    public void setOpenedNote(Note openedNote) {
        this.openedNote = openedNote;
    }

    public boolean contains(String noteName) {
        return notes.values().stream().anyMatch(note -> note.getName().equals(noteName));
    }

    public int getNotesSize() {
        return notes.size();
    }

    public void clearNotes() {
        notes.clear();
    }

    public Collection<Note> getNotes() { return notes.values(); }

    public List<Note> getUserNotesFromDB(String ownerUUID) {
        List<Note> notes = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT id, name, creation_date, last_updated_date, content FROM notes WHERE owner_id = ?")) {
            pstmt.setString(1, ownerUUID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Note note = new Note(UUID.fromString(rs.getString("id")), rs.getString("name"));

                    note.setCreationDate(new Date(rs.getTimestamp("creation_date").getTime()));
                    note.setLastUpdatedDate(new Date(rs.getTimestamp("last_updated_date").getTime()));
                    note.setText(rs.getString("content"));
                    notes.add(note);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }

    public List<Note> searchNotes(String searchText) {
        return notes.values().stream()
                .filter(note -> note.getText().contains(searchText))
                .collect(Collectors.toList());
    }
}
