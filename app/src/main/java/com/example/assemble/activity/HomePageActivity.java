package com.example.assemble.activity;

import android.content.Intent;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.assemble.R;
import com.example.assemble.service.SessionManager;

public class HomePageActivity extends AppCompatActivity {
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Link to home.xml

        username = SessionManager.getInstance().getCurrentUsername();

        // Find the welcome TextView and update it with the user's name
        TextView welcomeTextView = findViewById(R.id.textView3);
        if (username != null && !username.isEmpty()) {
            String welcomeMessage = getString(R.string.welcome_message, username);
            welcomeTextView.setText(welcomeMessage);
        }

        // Find buttons by their IDs
        Button noteButton = findViewById(R.id.button);
        Button todolistButton = findViewById(R.id.button2);
        Button flashcardButton = findViewById(R.id.button3);

        // Set click listeners for buttons
        noteButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, NoteActivity.class);
            startActivity(intent);
        });

        todolistButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, TodoListActivity.class);
            startActivity(intent);
        });

        flashcardButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, FlashcardsActivity.class);
            intent.putExtra("USER_NAME", username);
            startActivity(intent);
        });

    }
}
