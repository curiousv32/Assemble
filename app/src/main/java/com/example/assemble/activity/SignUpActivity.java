package com.example.assemble.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assemble.R;
import com.example.assemble.database.DatabaseManager;
import com.example.assemble.util.SharedPreferencesManager;

public class SignUpActivity extends AppCompatActivity {

    private SharedPreferencesManager sharedPreferencesManager;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private DatabaseManager databaseManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sharedPreferencesManager = new SharedPreferencesManager(this);
        databaseManager = DatabaseManager.getInstance(this);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        Button signUpButton = findViewById(R.id.register_button);

        signUpButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if (!username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    confirmPasswordEditText.setText("");
                    confirmPasswordEditText.requestFocus();
                    return;
                }

                if (sharedPreferencesManager.saveNewUser(username, password)) {
                    // Save user profile in the database
                    databaseManager.addUserProfile(username, password);
                    Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "Username already exists. Please try another one.", Toast.LENGTH_LONG).show();
                    usernameEditText.setText("");
                    usernameEditText.requestFocus();
                }
            } else {
                Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
