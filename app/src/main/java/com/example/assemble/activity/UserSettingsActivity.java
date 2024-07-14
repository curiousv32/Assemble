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
import com.example.assemble.model.User;
import com.example.assemble.service.UserManager;

import java.util.UUID;

public class UserSettingsActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    // private Button saveButton;
    private Button updateButton;
    private Button logoutButton;
    private UserManager userManager;

    private static final String TAG = "UserSettingsActivity";
    private boolean isPasswordVisible = false;
    private UUID currentUserId; // Store the current user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        Log.d(TAG, "onCreate: Initializing UI components");
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        //saveButton = findViewById(R.id.buttonSave);
        updateButton = findViewById(R.id.buttonUpdate);
        logoutButton = findViewById(R.id.buttonLogout);
        userManager = new UserManager(this);

        // Assume currentUserId is passed as an Intent extra
        String currentUserIdString = getIntent().getStringExtra("CURRENT_USER_ID");

        // Check if currentUserIdString is null or empty
        if (currentUserIdString == null || currentUserIdString.isEmpty()) {
            Log.e(TAG, "User ID is null or empty");
            Toast.makeText(this, "User ID is invalid. Please log in again.", Toast.LENGTH_SHORT).show();
            navigateToLogin();
            return;
        }

        try {
            currentUserId = UUID.fromString(currentUserIdString);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Invalid User ID format: " + currentUserIdString, e);
            Toast.makeText(this, "User ID is invalid. Please log in again.", Toast.LENGTH_SHORT).show();
            navigateToLogin();
            return;
        }

        Log.d(TAG, "onCreate: Received currentUserId: " + currentUserId);

        loadUserProfile();

//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "saveButton clicked");
//                saveUserProfile();
//            }
//        });

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

        if (currentUserId != null) {
            User userProfile = userManager.get(currentUserId, User.class);
            if (userProfile != null) {
                Log.d(TAG, "loadUserProfile: Loaded user profile - Username: " + userProfile.getUsername() + ", Password: " + userProfile.getPassword());
                usernameEditText.setText(userProfile.getUsername());
                passwordEditText.setText(userProfile.getPassword());
            } else {
                Log.d(TAG, "loadUserProfile: User profile is null");
                Toast.makeText(this, "User profile not found. Please log in again.", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            }
        } else {
            Log.e(TAG, "loadUserProfile: Current user ID not found");
            Toast.makeText(this, "User ID is invalid. Please log in again.", Toast.LENGTH_SHORT).show();
            navigateToLogin();
        }
    }

    private void saveUserProfile() {
        Log.d(TAG, "saveUserProfile: Saving user profile");
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        Log.d(TAG, "saveUserProfile: Username: " + username + ", Password: " + password);

        User userProfile = new User(currentUserId, username, password);
        userManager.update(currentUserId, userProfile);

        Toast.makeText(UserSettingsActivity.this, "Profile saved successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(UserSettingsActivity.this, HomePageActivity.class);
        intent.putExtra("USER_NAME", username); // Pass updated username to home page
        startActivity(intent);
        finish();
    }

    private void navigateToUserProfile() {
        Intent intent = new Intent(UserSettingsActivity.this, UserProfileActivity.class);
        intent.putExtra("CURRENT_USER_ID", currentUserId.toString()); // Pass current user ID to UserProfileActivity
        startActivity(intent);
    }

    private void logoutUser() {
        // No need to clear SharedPreferences, just navigate to LoginActivity
        startActivity(new Intent(UserSettingsActivity.this, LoginActivity.class));
        finish();
    }
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    private void navigateToLogin() {
        Intent intent = new Intent(UserSettingsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
