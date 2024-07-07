package com.example.assemble.activity;

import static com.example.assemble.database.DatabaseManager.STUB_PASSWORD;
import static com.example.assemble.database.DatabaseManager.STUB_USER;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assemble.R;
import com.example.assemble.database.UserManager;
import com.example.assemble.model.User;


public class SignUpActivity extends AppCompatActivity {

    private UserManager userManager;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userManager = new UserManager(this);
        User user = new User(STUB_USER, STUB_PASSWORD);
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
                user.setUsername(username);
                user.setPassword(password);
                if (userManager.saveNewUser(user)) {
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
