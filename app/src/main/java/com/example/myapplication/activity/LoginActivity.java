package com.example.myapplication.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.User;
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
        // remember to change this to activity_login.xml and change the filename of activity_main to activity_login
        setContentView(R.layout.activity_login);
        User testUser = new User(1, "john", "doe");
        users.add(testUser);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);
        Button signUpButton = findViewById(R.id.signup_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                // implement the getUser function
                User currUser = findUser(userName);
                if (currUser != null && currUser.getPassword().equals(password)) {
                    System.out.println("Login successful!");
                } else {
                    System.out.println("Invalid username or password.");
                }
            }
        });
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
    // can move this to a better place, thinking of creating a userManager class
    private User findUser(String userName){
        User curr = null;
        for(int i = 0; i < users.size(); i++) {
            if(users.get(i).getUsername().equals(userName)){
                curr = users.get(i);
            }
        }
        return curr;
    }
}
