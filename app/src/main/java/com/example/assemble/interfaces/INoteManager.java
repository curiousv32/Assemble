package com.example.assemble.interfaces;

import com.example.assemble.exceptions.InvalidNoteException;
import com.example.assemble.model.Note;
import java.util.UUID;

public interface INoteManager extends IDatabaseManager<Note>{
    @Override
    void add(Note note) throws InvalidNoteException;

    @Override
    Note get(UUID noteId, Class<Note> type);

    @Override
    void update(UUID noteId, Note note);

    @Override
    void delete(UUID noteId);
}
