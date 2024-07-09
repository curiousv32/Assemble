package com.example.assemble.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.assemble.R;
import com.example.assemble.service.UserManager;
import com.example.assemble.service.NoteManager;

public class LoginActivity extends AppCompatActivity {

    private UserManager userManager;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userManager = new UserManager(this);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (userManager.validateLogin(username, password)) {
                Intent intent = new Intent(this, HomePageActivity.class);
                intent.putExtra("USER_NAME", username); // Pass the username to HomePageActivity
                startActivity(intent);
                NoteManager.getInstance(this).init(userManager.getID(username));
            } else {
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onRegisterClick(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
