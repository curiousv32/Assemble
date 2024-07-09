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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NoteManager implements INoteManager {

    private static NoteManager REFERENCE;
    private DatabaseManager dbManager;
    private HashMap<UUID, Note> notes;
    private boolean useSQLDatabase;

    public static final String STUB_NOTE_NAME = "stub";

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

    public HashMap<UUID, Note> init(String ownerUUID) {
        notes.clear();
        List<Note> notesList;
        if (!useSQLDatabase) {
            notesList = stubNote;
        } else {
            notesList = this.getUserNotesFromDB(ownerUUID);
        }
        for (Note note : notesList) {
            notes.put(note.getID(), note);
        }
        return notes;
    }

    public Note addNote(String name, String content) {
        if (!useSQLDatabase) {
            Note note = new Note(UUID.randomUUID(), name);
            note.setText(content);
            return note;
        } else {
            Note note = new Note(UUID.randomUUID(), name);
            try {
                add(note);
            } catch (InvalidNoteException e) {
                e.printStackTrace();
            }
            return note;
        }
    }

    @Override
    public void add(Note note) throws InvalidNoteException {
        if(!useSQLDatabase) {
            notes.put(note.getID(), note);
            return;
        }
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO notes (id, name, creation_date, last_updated_date, content) VALUES (?, ?, ?, ?, ?)")) {
            pstmt.setString(1, note.getID().toString());
            pstmt.setString(2, note.getName());
            pstmt.setTimestamp(3, new java.sql.Timestamp(note.getCreationDate().getTime()));
            pstmt.setTimestamp(4, new java.sql.Timestamp(note.getLastUpdatedDate().getTime()));
            pstmt.setString(5, note.getText());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    note.setCreationDate(rs.getTimestamp("creation_date"));
                    note.setLastUpdatedDate(rs.getTimestamp("last_updated_date"));
                    note.setText(rs.getString("content"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return note;
    }


    @Override
    public void update(UUID noteId, Note note) {
        if (!useSQLDatabase) {
            notes.put(noteId, note);
            return;
        }

        dbManager.runQuery(
                "UPDATE notes SET name = ?, last_updated_date =CURRENT_TIMESTAMP, content = ? WHERE id = ?",
                note.getName(),
                note.getLastUpdatedDate().getTime(),
                note.getText(),
                noteId.toString()
        );
        /*try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE notes SET name = ?, last_updated_date = ?, content = ? WHERE id = ?")) {
            pstmt.setString(1, note.getName());
            pstmt.setTimestamp(2, new java.sql.Timestamp(note.getLastUpdatedDate().getTime()));
            pstmt.setString(3, note.getText());
            pstmt.setString(4, noteId.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        notes.put(noteId, note);
    }



    @Override
    public void delete(UUID noteId) {
        if (!useSQLDatabase) {
            notes.remove(noteId);
            return;
        }
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM notes WHERE id = ?")) {
            pstmt.setString(1, noteId.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        notes.remove(noteId);
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
             PreparedStatement pstmt = conn.prepareStatement("SELECT id, name, creation_date, last_updated_date, content FROM notes")) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Note note = new Note(UUID.fromString(rs.getString("id")),
                            rs.getString("name"));
                    note.setCreationDate(rs.getTimestamp("creation_date"));
                    note.setLastUpdatedDate(rs.getTimestamp("last_updated_date"));
                    note.setText(rs.getString("content"));
                    notes.add(note);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }

    private List<Note> stubNote = new ArrayList<Note>() {{
        add(new Note(UUID.randomUUID(), STUB_NOTE_NAME));
    }};
}
