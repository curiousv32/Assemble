package com.example.assemble.activity;

import static com.example.assemble.database.DatabaseManager.STUB_PASSWORD;
import static com.example.assemble.database.DatabaseManager.STUB_USER;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.assemble.database.DatabaseManager;


import androidx.appcompat.app.AppCompatActivity;

import com.example.assemble.R;
import com.example.assemble.service.UserManager;
import com.example.assemble.model.User;

import org.hsqldb.Database;

import java.util.UUID;


public class SignUpActivity extends AppCompatActivity {

    private UserManager userManager;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        dbManager = DatabaseManager.getInstance(this);
        userManager = new UserManager(this);
        User unregisteredUser = new User(UUID.randomUUID() ,STUB_USER, STUB_PASSWORD);
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
                unregisteredUser.setUsername(username);
                unregisteredUser.setPassword(password);
                try {
                    if (userManager.addUser(unregisteredUser)) {
                        dbManager.addUserProfile(username,password);
                        Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Username already exists. Please try another one.", Toast.LENGTH_LONG).show();
                        usernameEditText.setText("");
                        usernameEditText.requestFocus();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
