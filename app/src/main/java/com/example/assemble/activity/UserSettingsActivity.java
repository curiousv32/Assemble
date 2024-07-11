package com.example.assemble.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assemble.R;
import com.example.assemble.database.DatabaseManager;
import com.example.assemble.model.UserProfile;

public class UserSettingsActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button saveButton;
    private Button updateButton;
    private Button logoutButton;
    private DatabaseManager databaseManager;
    private static final String TAG = "UserSettingsActivity";
    private boolean isPasswordVisible = false;
    private String currentUsername; // Store the current username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        Log.d(TAG, "onCreate: Initializing UI components");
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        saveButton = findViewById(R.id.buttonSave);
        updateButton = findViewById(R.id.buttonUpdate);
        logoutButton = findViewById(R.id.buttonLogout);
        databaseManager = DatabaseManager.getInstance(this);
        databaseManager.createUserProfileTable();

        // Assume currentUsername is passed as an Intent extra
        currentUsername = getIntent().getStringExtra("CURRENT_USERNAME");

        loadUserProfile();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "saveButton clicked");
                saveUserProfile();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "updateButton clicked");
                navigateToUserProfile();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "logoutButton clicked");
                logoutUser();
            }
        });

        // Initialize password visibility toggle
        setupPasswordVisibilityToggle();
    }

    private void setupPasswordVisibilityToggle() {
        ImageButton togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);
        togglePasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide the password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isPasswordVisible = false;
            ImageButton togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);
            togglePasswordVisibility.setImageResource(R.drawable.ic_password_visibility_off);
        } else {
            // Show the password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            isPasswordVisible = true;
            ImageButton togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);
            togglePasswordVisibility.setImageResource(R.drawable.ic_password_visibility_on);
        }
        // Move cursor to end of text to maintain cursor position
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    private void loadUserProfile() {
        Log.d(TAG, "loadUserProfile: Loading user profile");

        if (currentUsername != null) {
            UserProfile userProfile = databaseManager.getUserProfile(currentUsername);
            if (userProfile != null) {
                Log.d(TAG, "loadUserProfile: Loaded user profile from database - Username: " + userProfile.getUsername() + ", Password: " + userProfile.getPassword());
                usernameEditText.setText(userProfile.getUsername());
                passwordEditText.setText(userProfile.getPassword());
            } else {
                Log.d(TAG, "loadUserProfile: User profile is null");
            }
        } else {
            Log.e(TAG, "loadUserProfile: Current username not found");
        }
    }

    private void saveUserProfile() {
        Log.d(TAG, "saveUserProfile: Saving user profile");
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        Log.d(TAG, "saveUserProfile: Username: " + username + ", Password: " + password);

        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(username);
        userProfile.setPassword(password);

        databaseManager.updateUserProfile(currentUsername, username, password);

        // Update currentUsername with new username
        currentUsername = username;

        Toast.makeText(UserSettingsActivity.this, "Profile saved successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(UserSettingsActivity.this, HomePageActivity.class);
        intent.putExtra("USER_NAME", currentUsername); // Pass updated username to home page
        startActivity(intent);
        finish();
    }

    private void navigateToUserProfile() {
        Intent intent = new Intent(UserSettingsActivity.this, UserProfileActivity.class);
        intent.putExtra("CURRENT_USERNAME", currentUsername); // Pass current username to UserProfileActivity
        startActivity(intent);
    }

    private void logoutUser() {
        // No need to clear SharedPreferences, just navigate to LoginActivity
        startActivity(new Intent(UserSettingsActivity.this, LoginActivity.class));
        finish();
    }
}
