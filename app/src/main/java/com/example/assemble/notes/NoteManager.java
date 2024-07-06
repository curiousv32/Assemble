package com.example.assemble.notes;

import android.content.Context;
import com.example.assemble.database.DatabaseManager;
import com.example.assemble.exceptions.InvalidNoteException;
import com.example.assemble.model.Note;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NoteManager {

    private static NoteManager REFERENCE;
    private DatabaseManager dbManager;
    private HashMap<UUID, Note> notes;

    private NoteManager(Context context) {
        this.notes = new HashMap<>();
        this.dbManager = DatabaseManager.getInstance(context);
    }

    public static synchronized NoteManager getInstance(Context context) {
        if (REFERENCE == null) {
            REFERENCE = new NoteManager(context);
        }
        return REFERENCE;
    }

    public HashMap<UUID, Note> init(String ownerUUID) {
        List<Note> notesList = dbManager.getUserNotes(ownerUUID);
        notesList.forEach(noteItem -> notes.put(noteItem.getID(), noteItem));
        return notes;
    }

    public Note create(String name) throws InvalidNoteException {
        if (contains(name)) {
            throw new InvalidNoteException("Note name \"" + name + "\" already exists");
        }
        UUID noteUUID = UUID.randomUUID();
        Note note = new Note(noteUUID, name);
        notes.put(noteUUID, note);
        note.setCreationDate();
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

    public int getNotesSize() {
        return notes.size();
    }

    public void clearNotes() {
        notes.clear();
    }
}
