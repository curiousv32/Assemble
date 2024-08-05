package com.example.assemble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assemble.R;
import com.example.assemble.model.Note;
import com.example.assemble.service.NoteManager;

import java.util.UUID;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_page);

        NoteManager noteManager = NoteManager.getInstance(this);
        UUID noteId = noteManager.getOpenNote().getID(); // get the note ID from the NoteManager
        Note foundNote = noteManager.get(noteId, Note.class); // get the note from the database


        Button goBackButton = findViewById(R.id.notes_go_back);
        TextView textContents = findViewById(R.id.note_contents);

        if (foundNote != null) {
            textContents.setText(foundNote.getText().toCharArray(), 0, foundNote.getText().length()); //show the note text
        } else {
            Toast.makeText(this, "Note not found", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, NoteListsActivity.class));
        }

        textContents.requestFocus();
        goBackButton.setOnClickListener(v -> {
            noteManager.setText(foundNote, textContents.getText().toString());
            noteManager.update(foundNote.getID(), foundNote);
            finish();
        });
    }
}
