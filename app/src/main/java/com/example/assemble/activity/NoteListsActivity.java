package com.example.assemble.activity;

import android.content.Intent;
import android.os.Bundle;
import com.example.assemble.model.Note;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assemble.R;
import com.example.assemble.service.NoteManager;

import java.util.ArrayList;

public class NoteListsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_lists_page);

        ArrayList<Note> notes = new ArrayList<>(NoteManager.getInstance(this).getNotes());

        RecyclerView noteLists = findViewById(R.id.notes);
        noteLists.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter<NoteAdapter.NoteViewHolder> adapter = new NoteAdapter(this, notes);

        noteLists.setAdapter(adapter);

        Button createNote = findViewById(R.id.note_create);
        TextView name = findViewById(R.id.new_note);

        createNote.setOnClickListener(v -> {
            if (name.getText().length() > 0) {
                Intent intent = new Intent(this, NoteActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Invalid note name", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
