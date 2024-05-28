package com.example.myapplication;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    // we can have a list of users for now

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remember to change this to activity_login.xml and change the filename of activity_main to activity_login
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEditText.getText().toString();

                User user = new User(1, "laraib","sike");
                if (user != null && user.getPassword().equals(password)) {
                    System.out.println("Login successful!");
                } else {
                    System.out.println("Invalid username or password.");
                }
            }
        });
    }
}
