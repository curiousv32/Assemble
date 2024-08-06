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
import java.util.List;
import java.util.Optional;
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
        noteManager.init(UUID.randomUUID().toString());
    }

    @Test
    public void create_succeeded_withStubDB() {
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
    public void getNote_invalid_withStubDB() {
        UUID randomUUID = UUID.randomUUID();
        Note note = noteManager.get(randomUUID, Note.class);
        assertNull("Should not find a non-existent note", note);
    }

    @Test
    public void searchNotes_filter_withStubDB() {
        try {
            Note createdNote1 = new Note(UUID.randomUUID(), "test1");
            Note createdNote2 = new Note(UUID.randomUUID(), "test2");

            createdNote1.setText("this is correct");
            createdNote2.setText("this is not correct");

            noteManager.add(createdNote1);
            noteManager.add(createdNote2);

            List<Note> searchResults = noteManager.searchNotes("this is correct");

            assertEquals(searchResults.size(), 1);
            assertEquals(searchResults.get(0).getName(), "test1");
            assertEquals(searchResults.get(0).getText(), "this is correct");

        } catch (InvalidNoteException e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void searchNotes_no_results_withStubDB() {
        try {
            Note createdNote1 = new Note(UUID.randomUUID(), "test1");
            Note createdNote2 = new Note(UUID.randomUUID(), "test2");

            createdNote1.setText("this is correct");
            createdNote2.setText("this is not correct");

            noteManager.add(createdNote1);
            noteManager.add(createdNote2);

            List<Note> searchResults = noteManager.searchNotes("this is correct not");

            assertEquals(searchResults.size(), 0);

        } catch (InvalidNoteException e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void delete_withStubDB() {
        noteManager.clearNotes();
        try {
            Note createdNote1 = new Note(UUID.randomUUID(), "test1");
            Note createdNote2 = new Note(UUID.randomUUID(), "test2");

            createdNote1.setText("this is correct");
            createdNote2.setText("this is not correct");

            noteManager.add(createdNote1);
            noteManager.add(createdNote2);

            noteManager.delete(createdNote1.getID());

            assertEquals(noteManager.getNotes().size(), 1);
            Optional<Note> optionalNote = noteManager.getNotes().stream().findFirst();
            assertTrue(optionalNote.isPresent());
            assertEquals("test2", optionalNote.get().getName());
        } catch (InvalidNoteException e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void rename_withStubDB() {
        try {
            Note createdNote1 = new Note(UUID.randomUUID(), "test1");
            Note createdNote2 = new Note(UUID.randomUUID(), "test2");

            createdNote1.setText("this is correct");
            createdNote2.setText("this is not correct");

            noteManager.add(createdNote1);
            noteManager.add(createdNote2);

            noteManager.rename(createdNote1, "test3");

            assertEquals(createdNote1.getName(), "test3");
            assertEquals(createdNote2.getName(), "test2");

        } catch (InvalidNoteException e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }


}
