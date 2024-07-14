package com.example.assemble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assemble.R;
import com.example.assemble.service.SessionManager;
import com.example.assemble.service.UserManager;
import com.example.assemble.model.User;
import com.example.assemble.service.SessionManager;

import java.util.UUID;

public class UserProfileActivity extends AppCompatActivity {

    private EditText newUsernameEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button updateButton;
    private UserManager userManager;
    private String currentUserId; // Store the current user ID
    private String toastMessage; // Field to store the last shown toast message
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        newUsernameEditText = findViewById(R.id.editTextNewUsername);
        newPasswordEditText = findViewById(R.id.editTextNewPassword);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        updateButton = findViewById(R.id.buttonUpdate);
        userManager = new UserManager(this);

        // Assume currentUserId is passed as an Intent extra
        currentUserId = getIntent().getStringExtra("CURRENT_USER_ID");

        updateButton.setOnClickListener(v -> updateUserProfile());
    }

    public String getToastMessage() {
        return toastMessage;
    }

    public void updateUserProfile() {
        String newUsername = newUsernameEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (!newPassword.equals(confirmPassword)) {
            // Show toast message for password mismatch
            toastMessage = "Passwords do not match";
            Toast.makeText(UserProfileActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUserId != null) {
            // Create a new User object with updated details
            UUID userId = UUID.fromString(currentUserId);

            userManager.update(userId, new User(userId, newUsername, newPassword));

            // Update the session with the new username
            SessionManager.getInstance().setCurrentUsername(newUsername);

            // Show toast message for successful update
            toastMessage = "Profile updated successfully";
            Toast.makeText(UserProfileActivity.this, toastMessage, Toast.LENGTH_SHORT).show();

            // Navigate back to home page
            Intent intent = new Intent(UserProfileActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.e("UserProfileActivity", "Current user ID not found");
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
