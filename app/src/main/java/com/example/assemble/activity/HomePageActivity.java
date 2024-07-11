package com.example.assemble.activity;

import android.content.Intent;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.assemble.R;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Link to home.xml

        String userName = getIntent().getStringExtra("USER_NAME");

        // Find the welcome TextView and update it with the user's name
        TextView welcomeTextView = findViewById(R.id.textView3);
        if (userName != null && !userName.isEmpty()) {
            String welcomeMessage = getString(R.string.welcome_message, userName);
            welcomeTextView.setText(welcomeMessage);
        }

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
            Toast.makeText(HomePageActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
        });

        flashcardButton.setOnClickListener(v -> {
            Toast.makeText(HomePageActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
        });

        // Set click listener for settings icon
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserSettingsActivity.class);
            startActivity(intent);
        });

    }
}
