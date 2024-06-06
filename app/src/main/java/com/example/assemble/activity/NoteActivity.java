package com.example.assemble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assemble.R;
import com.example.assemble.exceptions.InvalidNoteException;
import com.example.assemble.model.Note;
import com.example.assemble.notes.NoteManager;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_page);

        NoteManager noteManager = NoteManager.getInstance();
        Note foundNote = null;

        if (noteManager.contains("stub")) {
            foundNote = noteManager.getNoteByName("stub");
        }

        Button goBackButton = findViewById(R.id.notes_go_back);
        TextView textContents = findViewById(R.id.note_contents);

        if (foundNote != null) {
            textContents.setText(foundNote.getText().toCharArray(), 0, foundNote.getText().length());
        } else {
            textContents.setText("".toCharArray(), 0, 0);
            try {
                foundNote = noteManager.create("stub");
            } catch (InvalidNoteException e) {
                throw new RuntimeException(e);
            }
        }

        textContents.requestFocus();

        Note finalFoundNote = foundNote;
        goBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
            noteManager.save(finalFoundNote, textContents.getText().toString());

            Toast.makeText(this, "Returned to Home Page", Toast.LENGTH_SHORT).show();
        });
    }
}
