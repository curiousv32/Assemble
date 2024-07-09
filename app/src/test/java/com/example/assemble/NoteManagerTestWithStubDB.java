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
import java.util.UUID;

public class NoteManagerTestWithStubDB {

    private NoteManager noteManager;
    private Context mockContext;

    @Before
    public void setUp() {
        mockContext = Mockito.mock(Context.class);
        File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.getPath()).thenReturn("mock/path");
        Mockito.when(mockContext.getFilesDir()).thenReturn(mockFile);  // Ensure getFilesDir() does not return null
        DatabaseManager.setUseSQLDatabase(false);
        noteManager = NoteManager.getInstance(mockContext);
        noteManager.clearNotes(); // Clean notes after each test
    }

    @Test
    public void create_succeeded_withStubDB() {
        noteManager.init("");
        int size = noteManager.getNotesSize();
        try {
            Note newNote = new Note(UUID.randomUUID(), "test");
            noteManager.add(newNote);
            assertNotNull("Note should be created", newNote);
            assertEquals("Size should increase by 1", size + 1, noteManager.getNotesSize());
        } catch (InvalidNoteException e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void saveNote_withStubDB() {
        noteManager.init("");
        try {
            Note newNote = new Note(UUID.randomUUID(), "test");
            noteManager.add(newNote);
            newNote.setText("New text");
            noteManager.update(newNote.getID(), newNote);
            assertEquals("Content should be updated", "New text", newNote.getText());
        } catch (InvalidNoteException e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void getNote_succeed_withStubDB() {
        noteManager.init("");
        try {
            Note createdNote = new Note(UUID.randomUUID(), "test");
            noteManager.add(createdNote);
            Note retrievedNote = noteManager.get(createdNote.getID(), Note.class);
            assertNotNull("Note should be retrievable", retrievedNote);
        } catch (Exception e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void getNote_invalid_withStubDB() {
        noteManager.init("");
        UUID randomUUID = UUID.randomUUID();
        Note note = noteManager.get(randomUUID, Note.class);
        assertNull("Should not find a non-existent note", note);
    }

    @Test
    public void getNoteByName_succeed_withStubDB() {
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


}
