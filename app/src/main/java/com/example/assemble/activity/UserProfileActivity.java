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
import com.example.assemble.database.DatabaseManager;
import com.example.assemble.util.SharedPreferencesManager;

public class UserProfileActivity extends AppCompatActivity {

    private EditText newUsernameEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button updateButton;
    private DatabaseManager databaseManager;
    private SharedPreferencesManager sharedPreferencesManager;
    private String toastMessage; // Field to store the last shown toast message

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        newUsernameEditText = findViewById(R.id.editTextNewUsername);
        newPasswordEditText = findViewById(R.id.editTextNewPassword);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        updateButton = findViewById(R.id.buttonUpdate);
        databaseManager = DatabaseManager.getInstance(this);
        sharedPreferencesManager = new SharedPreferencesManager(this);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });
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
            Toast.makeText(UserProfileActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUsername = sharedPreferencesManager.getCurrentUser();
        if (currentUsername != null) {
            // Delete old profile
            databaseManager.deleteUserProfile(currentUsername);

            // Add new profile with updated details
            databaseManager.addUserProfile(newUsername, newPassword);

            // Update shared preferences with new username
            sharedPreferencesManager.saveCurrentUser(newUsername);

            // Show toast message for successful update
            Toast.makeText(UserProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to home page
            Intent intent = new Intent(UserProfileActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.e("UserProfileActivity", "Current username not found in SharedPreferences");
        }
    }
}
