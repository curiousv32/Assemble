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
import com.example.assemble.notes.NoteManager;

import java.util.UUID;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_page);

        NoteManager noteManager = NoteManager.getInstance(this);
        Note foundNote = null;

        //todo: this is a stub note id, we need to get the first note from a userid
        UUID noteId = UUID.randomUUID();
        foundNote = noteManager.get(noteId, Note.class);

        Button goBackButton = findViewById(R.id.notes_go_back);
        TextView textContents = findViewById(R.id.note_contents);

        if (foundNote != null) {
            textContents.setText(foundNote.getText().toCharArray(), 0, foundNote.getText().length()); //show the note text
        } else {
            textContents.setText("".toCharArray(), 0, 0);
            foundNote = new Note(noteId, DatabaseManager.STUB_NOTE_NAME);
            try {
                noteManager.add(foundNote); //create a default note
            } catch (InvalidNoteException exception) {
                Log.e(null, "Add Note Failed");
                goBackToHome();
                return;
            }
        }

        textContents.requestFocus();

        Note finalFoundNote = foundNote;
        goBackButton.setOnClickListener(v -> {
            goBackToHome();

            noteManager.update(finalFoundNote.getID(), finalFoundNote);

            Toast.makeText(this, "Returned to Home Page", Toast.LENGTH_SHORT).show();
        });
    }

    private void goBackToHome() {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }
}
