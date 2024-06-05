package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.example.myapplication.model.Note;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

public class NoteTest {

    private UUID randomUUID;
    private String noteName;
    private Note note;

    @Before
    public void construct_note() {
        this.randomUUID = UUID.randomUUID();
        this.noteName = "test";
        this.note = new Note(randomUUID, noteName);
    }

    @Test
    public void text_set_and_get() {
        assertEquals(note.getText(), "");

        String newText = "new text";
        Date lastUpdated = note.getLastUpdatedDate();

        note.setText(newText);

        assertEquals(note.getText(), newText);
        assertNotEquals(lastUpdated, note.getLastUpdatedDate());
    }

    @Test
    public void equals_true() {
        Note other = new Note(randomUUID, noteName);
        assertTrue(note.equals(other));
    }

    @Test
    public void equals_false() {
        Note other = new Note(UUID.randomUUID(), noteName);
        assertFalse(note.equals(other));
    }
}
