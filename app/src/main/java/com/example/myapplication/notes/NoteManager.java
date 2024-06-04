package com.example.myapplication.notes;

import com.example.myapplication.exceptions.InvalidNoteException;
import com.example.myapplication.model.Note;

import java.util.HashMap;
import java.util.UUID;

public class NoteManager {

    private HashMap<UUID, Note> notes;

    public HashMap<UUID, Note> init(String ownerUUID) {
        notes = new HashMap<>();

        // TODO: Random data for now, in the future init() will load all data from the database for that specific user.
        //UUID uuid = UUID.randomUUID();

        //notes.put(uuid, new Note(uuid, "blank for now"));

        return notes;
    }

    public void create(String name) throws InvalidNoteException {
        if (contains(name)) {
            throw new InvalidNoteException("Note name \"" + name + "\" already exists");
        } else {
            UUID noteUUID = UUID.randomUUID();
            notes.put(noteUUID, new Note(noteUUID, name));
            // TODO: Insert into database and redirect to note page.
        }
    }

    public void save(Note note, String text) {
        note.setText(text);
    }

    public Note getNote(UUID uuid) {
        return notes.get(uuid);
    }

    public boolean contains(String noteName) {
        return notes.values().stream().anyMatch(note -> note.getName().equals(noteName));
    }
}
