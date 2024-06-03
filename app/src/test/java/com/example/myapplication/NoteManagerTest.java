package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

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

        Assert.assertEquals(notes.size(), 0);

        try {
            noteManager.create("test");
        } catch (InvalidNoteException exception) {
            //
        }
        Assert.assertEquals(notes.size(), 1);
    }
}
