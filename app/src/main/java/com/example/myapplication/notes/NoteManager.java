package com.example.myapplication.notes;

import com.example.myapplication.exceptions.InvalidNote;
import com.example.myapplication.model.Note;

import java.util.HashMap;
import java.util.UUID;

public class NoteManager {

    private HashMap<UUID, Note> notes;

    public NoteManager(String ownerUUID) {
        this.notes = init(ownerUUID);
    }

    private HashMap<UUID, Note> init(String ownerUUID) {
        HashMap<UUID, Note> notesTemp = new HashMap<>();

        // TODO: Random data for now, in the future init() will load all data from the database for that specific user.
        UUID uuid = UUID.randomUUID();

        notesTemp.put(uuid, new Note(uuid, "blank for now"));

        return notesTemp;
    }

    public void create(String name) throws InvalidNote {
        if (contains(name)) {
            throw new InvalidNote("Note name \"" + name + "\" already exists");
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
