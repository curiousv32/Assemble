package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.User;
import com.example.myapplication.notes.NoteManager;
import com.example.myapplication.util.SharedPreferencesManager;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Link to home.xml

        // Find buttons by their IDs
        Button noteButton = findViewById(R.id.button);
        Button listButton = findViewById(R.id.button2);
        Button flashcardButton = findViewById(R.id.button3);

        // Set click listeners for buttons
        noteButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, NoteActivity.class);
            startActivity(intent);
        });

        listButton.setOnClickListener(v -> {
            //Todo: Implement your logic here when the list button is clicked
            Toast.makeText(HomePageActivity.this, "List Button Clicked", Toast.LENGTH_SHORT).show();
        });

        flashcardButton.setOnClickListener(v -> {
            //Todo: Implement your logic here when the flashcard button is clicked
            Toast.makeText(HomePageActivity.this, "Flashcard Button Clicked", Toast.LENGTH_SHORT).show();
        });

    }
}
