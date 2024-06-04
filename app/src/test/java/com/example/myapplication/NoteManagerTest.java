package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.example.myapplication.exceptions.InvalidNoteException;
import com.example.myapplication.model.Note;
import com.example.myapplication.notes.NoteManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.UUID;

public class NoteManagerTest {

    private NoteManager noteManager;

    @Before
    public void construct_noteManager() {
        noteManager = new NoteManager();
    }

    @Test
    public void init_returnsNonNull() {
        HashMap<UUID, Note> notes = noteManager.init("");
        assertNotNull(notes);
    }

    @Test
    public void create_failsAndThrows() {
        HashMap<UUID, Note> notes = noteManager.init("");

        UUID randomUUID = UUID.randomUUID();
        notes.put(randomUUID, new Note(randomUUID, "test"));

        InvalidNoteException exception = assertThrows(InvalidNoteException.class, () -> noteManager.create("test"));

        assertEquals(exception.getMessage(), "Note name \"test\" already exists");
    }

    @Test
    public void create_succeeded() {
        HashMap<UUID, Note> notes = noteManager.init("");

        assertEquals(notes.size(), 0);

        try {
            noteManager.create("test");
        } catch (InvalidNoteException exception) {
            Assert.fail("Assertion failed on note creation test: create_succeeded()");
        }
        assertEquals(notes.size(), 1);
    }

    @Test
    public void saveNote() {
        HashMap<UUID, Note> notes = noteManager.init("");

        UUID randomUUID = UUID.randomUUID();
        notes.put(randomUUID, new Note(randomUUID, "test"));

        Note note = noteManager.getNote(randomUUID);

        String text = "New text";

        noteManager.save(note, text);

        assertEquals(note.getText(), text);
    }

    @Test
    public void getNote_succeed() {
        HashMap<UUID, Note> notes = noteManager.init("");

        UUID randomUUID = UUID.randomUUID();
        notes.put(randomUUID, new Note(randomUUID, "test"));

        Note note = noteManager.getNote(randomUUID);

        assertNotNull(note);
    }

    @Test
    public void getNote_invalid() {
        noteManager.init("");

        UUID randomUUID = UUID.randomUUID();
        Note note = noteManager.getNote(randomUUID);

        assertNull(note);
    }

    @Test
    public void containsNote_true() {
        HashMap<UUID, Note> notes = noteManager.init("");

        UUID randomUUID1 = UUID.randomUUID();
        UUID randomUUID2 = UUID.randomUUID();
        notes.put(randomUUID1, new Note(randomUUID1, "test1"));
        notes.put(randomUUID2, new Note(randomUUID2, "test2"));

        assertTrue(noteManager.contains("test2"));
    }

    @Test
    public void containsNote_false() {
        HashMap<UUID, Note> notes = noteManager.init("");

        UUID randomUUID = UUID.randomUUID();
        notes.put(randomUUID, new Note(randomUUID, "test1"));

        assertFalse(noteManager.contains("test2"));
    }
}
