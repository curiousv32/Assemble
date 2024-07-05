package com.example.assemble.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.assemble.R;
import com.example.assemble.notes.NoteManager;
import com.example.assemble.util.SharedPreferencesManager;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferencesManager sharedPreferencesManager;
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
                intent.putExtra("USER_NAME", username); // Pass the username to HomePageActivity
                startActivity(intent);
                NoteManager.getInstance().init(sharedPreferencesManager.getID());
            } else {
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateLogin(String username, String password) {
        String storedUsername = sharedPreferencesManager.getUsername(username);
        String storedPassword = sharedPreferencesManager.getPassword(password);

        return username.equals(storedUsername) && password.equals(storedPassword);
    }

    public void onRegisterClick(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
