package com.example.assemble.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.assemble.exceptions.InvalidNoteException;
import com.example.assemble.model.Note;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assemble.R;
import com.example.assemble.service.NoteManager;
import com.example.assemble.util.NoteAdapter;

import java.util.ArrayList;
import java.util.List;

public class NoteListsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_lists_page);

        Button createNote = findViewById(R.id.note_create);
        Button searchNote = findViewById(R.id.search_note_button);
        Button resetSearch = findViewById(R.id.reset_search);

        TextView name = findViewById(R.id.new_note_name);
        TextView searchText = findViewById(R.id.search_note_text);

        NoteManager noteManager = NoteManager.getInstance(this);

        List<Note> notes = new ArrayList<>(noteManager.getNotes());

        RecyclerView noteLists = findViewById(R.id.notes);
        noteLists.setLayoutManager(new LinearLayoutManager(this));

        initializeAdapter(this, notes, noteLists);

        createNote.setOnClickListener(v -> {
            if (name.getText().length() > 0 && name.getText().length() <= NoteManager.MAX_NOTE_NAME_SIZE) {

                if (!noteManager.contains(name.getText().toString())) {
                    try {
                        Note note = noteManager.createNote(name.getText().toString());

                        if (note != null) {
                            noteManager.setOpenedNote(note);
                            Intent intent = new Intent(this, NoteActivity.class);
                            startActivity(intent);
                        }
                    } catch (InvalidNoteException exception) {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                        name.setText("");
                        exception.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Note already exists", Toast.LENGTH_LONG).show();
                    name.setText("");
                }
            } else {
                Toast.makeText(this, "Invalid note name", Toast.LENGTH_LONG).show();
                name.setText("");
            }
        });

        searchNote.setOnClickListener(v -> {
            String text = searchText.getText().toString();

            if (text.length() >= NoteManager.MIN_NOTE_SEARCH_SIZE) {
                List<Note> results = noteManager.searchNotes(text);
                initializeAdapter(this, results, noteLists);
            } else {
                Toast.makeText(this, "Must be at least 3 characters", Toast.LENGTH_LONG).show();
            }
        });

        resetSearch.setOnClickListener(v -> {
            initializeAdapter(this, notes, noteLists);
            searchText.setText("");
        });
    }

    private void initializeAdapter(Context context, List<Note> notesToShow, RecyclerView view) {
        RecyclerView.Adapter<NoteAdapter.NoteViewHolder> newAdapter = new NoteAdapter(context, notesToShow);
        view.setAdapter(newAdapter);
    }
}
