package com.example.assemble;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import android.content.Context;

import com.example.assemble.database.DatabaseManager;
import com.example.assemble.exceptions.InvalidNoteException;
import com.example.assemble.model.Note;
import com.example.assemble.service.NoteManager;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
public class NoteManagerTestWithSQLDB {

    private NoteManager noteManager;
    private Context mockContext;

    @Before
    public void setUp() {
        mockContext = Mockito.mock(Context.class);
        File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.getPath()).thenReturn("mock/path");
        Mockito.when(mockContext.getFilesDir()).thenReturn(mockFile);  // Ensure getFilesDir() does not return null
        noteManager = NoteManager.getInstance(mockContext);
        initializeDatabase();
        noteManager.clearNotes(); // Clean notes after each test
    }

    private void initializeDatabase() {
        DatabaseManager dbManager = DatabaseManager.getInstance(this.mockContext);
        try (Connection conn = dbManager.getConnection()) {
            String createUserTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id CHAR(36) PRIMARY KEY, " +
                    "username VARCHAR(255) NOT NULL, " +
                    "password VARCHAR(255) NOT NULL)";
            try (PreparedStatement stmt = conn.prepareStatement(createUserTable)) {
                stmt.executeUpdate();
            }

            String createNoteTable = "CREATE TABLE IF NOT EXISTS notes (" +
                    "id CHAR(36) PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "content VARCHAR(65535))";
            try (PreparedStatement stmt = conn.prepareStatement(createNoteTable)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void init_returnsNonNull() {
        noteManager.init("");
        if (noteManager.getNotes() == null) {
            fail("Notes should not be null");
        }
    }

    @Test
    public void create_fails_withSQLDB() {
        noteManager.init("");
        try {
            noteManager.add(new Note(UUID.randomUUID(), "test"));
        } catch (InvalidNoteException e) {
            fail("First creation should not fail");
        }
    }


    @Test
    public void create_succeeded_withSQLDB() {
        noteManager.init("");
        int size = noteManager.getNotesSize();
        try {
            Note note = new Note(UUID.randomUUID(), "test");
            noteManager.add(note);
            assertNotNull("Note should be created", note);
            assertEquals("Size should increase by 1", size + 1, noteManager.getNotesSize());
        } catch (InvalidNoteException e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void saveNote_withSQLDB() {
        noteManager.init("");
        try {
            Note note = new Note(UUID.randomUUID(), "test");
            noteManager.add(note);
            note.setText("New text");
            noteManager.update(note.getID(), note);
            assertEquals("Content should be updated", "New text", note.getText());
        } catch (InvalidNoteException e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void getNote_succeed_withSQLDB() {
        noteManager.init("");
        try {
            Note createdNote = new Note(UUID.randomUUID(), "test");
            createdNote.setText("test");
            noteManager.add(createdNote);
            Note retrievedNote = noteManager.get(createdNote.getID(), Note.class);
            assertNotNull("Note should be retrievable", retrievedNote);
        } catch (Exception e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void getNote_invalid_withSQLDB() {
        noteManager.init("");
        UUID randomUUID = UUID.randomUUID();
        Note note = noteManager.get(randomUUID, Note.class);
        assertNull("Should not find a non-existent note", note);
    }

    @Test
    public void getNoteByName_succeed_withSQLDB() {
        noteManager.init("");
        try {
            UUID id = UUID.randomUUID();
            Note createdNote = new Note(id, "test");
            noteManager.add(createdNote);
            Note foundNote = noteManager.get(id, Note.class);
            assertTrue("Retrieved note should match created", createdNote.equals(foundNote));
        } catch (InvalidNoteException e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void delete_succeed_withSQLDB() {
        noteManager.init("");

        try {
            UUID id = UUID.randomUUID();
            Note createdNote = new Note(id, "test");
            noteManager.add(createdNote);

            noteManager.delete(id);

            Note foundNote = noteManager.get(id, Note.class);
            assertNull("Note should be null", foundNote);
        } catch (InvalidNoteException e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void rename_succeed_withSQLDB() {
        noteManager.init("");

        try {
            UUID id = UUID.randomUUID();
            Note createdNote = new Note(id, "test");
            noteManager.add(createdNote);

            noteManager.rename(createdNote, "test2");

            assertEquals("Note name should be changed", "test2", createdNote.getName());
        } catch (InvalidNoteException e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }
}