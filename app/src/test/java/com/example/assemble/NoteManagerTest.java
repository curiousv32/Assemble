package com.example.assemble;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import android.content.Context;

import com.example.assemble.exceptions.InvalidNoteException;
import com.example.assemble.model.Note;
import com.example.assemble.notes.NoteManager;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
public class NoteManagerTest {

    private NoteManager noteManager;
    private Context mockContext;

    @Before
    public void setUp() {
        mockContext = Mockito.mock(Context.class);
        File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.getPath()).thenReturn("mock/path");
        Mockito.when(mockContext.getFilesDir()).thenReturn(mockFile);  // Ensure getFilesDir() does not return null
        noteManager = NoteManager.getInstance(mockContext);
        noteManager.clearNotes(); // Clean notes after each test
    }

    @Test
    public void init_returnsNonNull() {
        HashMap<UUID, Note> notes = noteManager.init("");
        assertNotNull(notes);
    }

    @Test
    public void create_failsAndThrows() {
        noteManager.init("");
        try {
            noteManager.create("test");
        } catch (InvalidNoteException e) {
            fail("First creation should not fail");
        }

        try {
            noteManager.create("test");
            fail("Expected an InvalidNoteException to be thrown");
        } catch (InvalidNoteException e) {
            assertEquals("Note name \"test\" already exists", e.getMessage());
        }
    }


    @Test
    public void create_succeeded() {
        noteManager.init("");
        int size = noteManager.getNotesSize();
        try {
            Note note = noteManager.create("test");
            assertNotNull("Note should be created", note);
            assertEquals("Size should increase by 1", size + 1, noteManager.getNotesSize());
        } catch (InvalidNoteException e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void saveNote() {
        noteManager.init("");
        try {
            Note note = noteManager.create("test");
            noteManager.save(note, "New text");
            assertEquals("Content should be updated", "New text", note.getText());
        } catch (InvalidNoteException e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void getNote_succeed() {
        noteManager.init("");
        try {
            Note createdNote = noteManager.create("test");
            Note retrievedNote = noteManager.getNote(createdNote.getID());
            assertNotNull("Note should be retrievable", retrievedNote);
        } catch (Exception e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void getNote_invalid() {
        noteManager.init("");
        UUID randomUUID = UUID.randomUUID();
        Note note = noteManager.getNote(randomUUID);
        assertNull("Should not find a non-existent note", note);
    }

    @Test
    public void getNoteByName_succeed() {
        noteManager.init("");
        try {
            Note createdNote = noteManager.create("test");
            Note foundNote = noteManager.getNoteByName("test");
            assertEquals("Retrieved note should match created", createdNote, foundNote);
        } catch (InvalidNoteException e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void getNoteByName_invalid() {
        noteManager.init("");
        Note note = noteManager.getNoteByName("notexisted");
        assertNull("Should not find a non-existent note", note);
    }

}
