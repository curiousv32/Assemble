package com.example.assemble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assemble.R;
import com.example.assemble.database.DatabaseManager;
import com.example.assemble.exceptions.InvalidNoteException;
import com.example.assemble.model.Note;
import com.example.assemble.service.NoteManager;

import java.util.UUID;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_page);

        NoteManager noteManager = NoteManager.getInstance(this);
        Note foundNote = noteManager.getOpenNote();

        Button goBackButton = findViewById(R.id.notes_go_back);
        TextView textContents = findViewById(R.id.note_contents);

        if (foundNote != null) {
            textContents.setText(foundNote.getText().toCharArray(), 0, foundNote.getText().length()); //show the note text
        }

        textContents.requestFocus();
        goBackButton.setOnClickListener(v -> {
            startActivity(new Intent(this, NoteListsActivity.class));
            noteManager.setText(foundNote, textContents.getText().toString());
            noteManager.update(foundNote.getID(), foundNote);

            noteManager.setOpenedNote(null);

            Toast.makeText(this, "Returned to Home Page", Toast.LENGTH_SHORT).show();
        });
    }
}
