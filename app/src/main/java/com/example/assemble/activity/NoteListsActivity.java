package com.example.assemble.activity;

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

public class NoteListsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_lists_page);

        Button createNote = findViewById(R.id.note_create);
        TextView name = findViewById(R.id.new_note_name);
        NoteManager noteManager = NoteManager.getInstance(this);

        ArrayList<Note> notes = new ArrayList<>(noteManager.getNotes());

        RecyclerView noteLists = findViewById(R.id.notes);
        noteLists.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter<NoteAdapter.NoteViewHolder> adapter = new NoteAdapter(this, notes);

        noteLists.setAdapter(adapter);

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
    }
}
