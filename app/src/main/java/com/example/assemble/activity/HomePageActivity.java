package com.example.assemble.activity;

import android.content.Intent;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.assemble.R;
import com.example.assemble.database.DatabaseManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class HomePageActivity extends AppCompatActivity {
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Link to home.xml

        username = getIntent().getStringExtra("USER_NAME");

        // Find the welcome TextView and update it with the user's name
        TextView welcomeTextView = findViewById(R.id.textView3);
        if (username != null && !username.isEmpty()) {
            String welcomeMessage = getString(R.string.welcome_message, username);
            welcomeTextView.setText(welcomeMessage);
        }

        // Find buttons by their IDs
        Button noteButton = findViewById(R.id.button);
        Button listButton = findViewById(R.id.button2);
        Button flashcardButton = findViewById(R.id.button3);

        // Set click listeners for buttons
        noteButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, NoteListsActivity.class);
            startActivity(intent);
        });

        listButton.setOnClickListener(v -> {
            Toast.makeText(HomePageActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
        });

        flashcardButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, FlashcardsActivity.class);
            intent.putExtra("USER_NAME", username);
            startActivity(intent);
        });
    }
}
