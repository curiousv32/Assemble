package com.example.assemble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assemble.R;
import com.example.assemble.model.User;
import com.example.assemble.service.SessionManager;
import com.example.assemble.service.UserSettingsManager;

import java.util.UUID;

public class UserProfileActivity extends AppCompatActivity {

    private EditText newUsernameEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button updateButton;
    private UserSettingsManager userSettingsManager;
    private UUID currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        newUsernameEditText = findViewById(R.id.editTextNewUsername);
        newPasswordEditText = findViewById(R.id.editTextNewPassword);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        updateButton = findViewById(R.id.buttonUpdate);

        // Assume currentUserId is passed as an Intent extra
        currentUserId = UUID.fromString(getIntent().getStringExtra("CURRENT_USER_ID"));
        userSettingsManager = new UserSettingsManager(this, currentUserId);

        updateButton.setOnClickListener(v -> updateUserProfile());
    }

    public void updateUserProfile() {
        String newUsername = newUsernameEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (!newPassword.equals(confirmPassword)) {
            // Show toast message for password mismatch
            Toast.makeText(UserProfileActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUserId != null) {
            // Update user profile using UserSettingsManager
            userSettingsManager.updateUserProfile(newUsername, newPassword);

            // Update the session with the new username
            SessionManager.getInstance().setCurrentUsername(newUsername);

            // Show toast message for successful update
            Toast.makeText(UserProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to home page
            startActivity(new Intent(UserProfileActivity.this, HomePageActivity.class));
            finish();
        } else {
            Log.e("UserProfileActivity", "Current user ID not found");
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
