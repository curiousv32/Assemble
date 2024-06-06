package com.example.assemble.notes;

import com.example.assemble.database.DatabaseManager;
import com.example.assemble.exceptions.InvalidNoteException;
import com.example.assemble.model.Note;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NoteManager {

    private static final NoteManager reference = new NoteManager();

    public static NoteManager getInstance() {
        return reference;
    }

    private HashMap<UUID, Note> notes;

    public NoteManager() {
        this.notes = new HashMap<>();
    }

    public HashMap<UUID, Note> init(String ownerUUID) {

        List<Note> notesList = DatabaseManager.getInstance().getUserNotes(ownerUUID);
        notesList.forEach(noteItem -> notes.put(noteItem.getID(), noteItem));

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
            note.setCreationDate();
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

    public int getNotesSize() {
        return notes.size();
    }
}
