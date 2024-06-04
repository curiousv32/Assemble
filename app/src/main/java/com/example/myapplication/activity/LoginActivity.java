package com.example.myapplication.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.User;
import com.example.myapplication.notes.NoteManager;
import com.example.myapplication.util.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;
    private List<User> users = new ArrayList<User>();
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferencesManager = new SharedPreferencesManager(this);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (validateLogin(username, password)) {
                Intent intent = new Intent(this, HomePageActivity.class);
                startActivity(intent);
                NoteManager.getInstance().init("");
            } else {
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateLogin(String username, String password) {
        String storedUsername = sharedPreferencesManager.getUsername();
        String storedPassword = sharedPreferencesManager.getPassword();

        return username.equals(storedUsername) && password.equals(storedPassword);
    }

    public void onRegisterClick(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
