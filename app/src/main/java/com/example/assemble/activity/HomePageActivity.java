package com.example.assemble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.assemble.R;
import com.example.assemble.service.UserManager;
import com.example.assemble.service.SessionManager;

import java.util.UUID;

public class HomePageActivity extends AppCompatActivity {
    private String username;
    private UserManager userManager;
    private UUID currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Link to home.xml

        userManager = new UserManager(this);
        username = SessionManager.getInstance().getCurrentUsername();
        currentUserId = userManager.getCurrentUserId(); // Assuming UserManager has this method

        // Find the welcome TextView and update it with the user's name
        TextView welcomeTextView = findViewById(R.id.textView3);
        updateWelcomeMessage(welcomeTextView);

        // Find buttons by their IDs
        Button noteButton = findViewById(R.id.button);
        Button todolistButton = findViewById(R.id.button2);
        Button flashcardButton = findViewById(R.id.button3);
        AppCompatImageButton settingsButton = findViewById(R.id.settingsButton);

        // Set click listeners for buttons
        noteButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, NoteListsActivity.class);
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

        // Set click listener for settings icon
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserSettingsActivity.class);
            if (currentUserId != null) {
                intent.putExtra("CURRENT_USER_ID", currentUserId);
                startActivity(intent);
            } else {
                Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
                // Redirect to login activity if user ID is not found
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the username and update the welcome message
        username = SessionManager.getInstance().getCurrentUsername();
        TextView welcomeTextView = findViewById(R.id.textView3);
        updateWelcomeMessage(welcomeTextView);
    }

    private void updateWelcomeMessage(TextView welcomeTextView) {
        if (username != null && !username.isEmpty()) {
            String welcomeMessage = getString(R.string.welcome_message, username);
            welcomeTextView.setText(welcomeMessage);
        }
    }
}
