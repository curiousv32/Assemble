package com.example.myapplication.notes;

import com.example.myapplication.exceptions.InvalidNoteException;
import com.example.myapplication.model.Note;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class NoteManager {

    private static final NoteManager reference = new NoteManager();

    public static NoteManager getInstance() {
        return reference;
    }

    private HashMap<UUID, Note> notes;

    public HashMap<UUID, Note> init(String ownerUUID) {
        notes = new HashMap<>();

        // TODO: Random data for now, in the future init() will load all data from the database for that specific user.
        //UUID uuid = UUID.randomUUID();

        //notes.put(uuid, new Note(uuid, "blank for now"));

        return notes;
    }

    public Note create(String name) throws InvalidNoteException {
        Note note;

        if (contains(name)) {
            throw new InvalidNoteException("Note name \"" + name + "\" already exists");
        } else {
            UUID noteUUID = UUID.randomUUID();
            note = new Note(noteUUID, name);
            notes.put(noteUUID, note);
        }
        return note;
    }

    public void save(Note note, String text) {
        note.setText(text);
    }

    public Note getNote(UUID uuid) {
        return notes.get(uuid);
    }

    public Note getNoteByName(String name) {
        return notes.values().stream()
                .filter(note -> note.getName().equals(name))
                .findFirst().orElse(null);
    }

    public boolean contains(String noteName) {
        return notes.values().stream().anyMatch(note -> note.getName().equals(noteName));
    }
}
