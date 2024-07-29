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
import com.example.assemble.service.UserSettingsManager;
import com.example.assemble.model.User;

import java.util.UUID;

public class UserSettingsActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button updateButton;
    private Button logoutButton;
    private UserSettingsManager userSettingsManager;

    private static final String TAG = "UserSettingsActivity";
    private boolean isPasswordVisible = false;
    private UUID currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        Log.d(TAG, "onCreate: Initializing UI components");
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        updateButton = findViewById(R.id.buttonUpdate);
        logoutButton = findViewById(R.id.buttonLogout);

        String currentUserIdString = getIntent().getStringExtra("CURRENT_USER_ID");
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

        userSettingsManager = new UserSettingsManager(this, currentUserId);
        loadUserProfile();

        updateButton.setOnClickListener(v -> {
            Log.d(TAG, "updateButton clicked");
            navigateToUserProfile();
        });

        logoutButton.setOnClickListener(v -> {
            Log.d(TAG, "logoutButton clicked");
            logoutUser();
        });

        setupPasswordVisibilityToggle();
    }

    private void setupPasswordVisibilityToggle() {
        ImageButton togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);
        togglePasswordVisibility.setOnClickListener(v -> togglePasswordVisibility());
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isPasswordVisible = false;
            ((ImageButton) findViewById(R.id.togglePasswordVisibility)).setImageResource(R.drawable.ic_password_visibility_off);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            isPasswordVisible = true;
            ((ImageButton) findViewById(R.id.togglePasswordVisibility)).setImageResource(R.drawable.ic_password_visibility_on);
        }
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    private void loadUserProfile() {
        Log.d(TAG, "loadUserProfile: Loading user profile");
        User userProfile = userSettingsManager.loadUserProfile();
        if (userProfile != null) {
            usernameEditText.setText(userProfile.getUsername());
            passwordEditText.setText(userProfile.getPassword());
        } else {
            Toast.makeText(this, "User profile not found. Please log in again.", Toast.LENGTH_SHORT).show();
            navigateToLogin();
        }
    }

    private void navigateToUserProfile() {
        Intent intent = new Intent(UserSettingsActivity.this, UserProfileActivity.class);
        intent.putExtra("CURRENT_USER_ID", currentUserId.toString());
        startActivity(intent);
    }

    private void logoutUser() {
        userSettingsManager.logoutUser(); // Call the method to perform any necessary logout actions
        startActivity(new Intent(UserSettingsActivity.this, LoginActivity.class));
        finish();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(UserSettingsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
