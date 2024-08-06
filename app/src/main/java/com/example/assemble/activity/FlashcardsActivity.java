package com.example.assemble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.assemble.R;
import com.example.assemble.model.Flashcard;
import com.example.assemble.service.FlashcardManager;
import com.example.assemble.util.FlashcardAdapter;
import com.google.android.material.navigation.NavigationView;
import com.example.assemble.database.FlashcardsStub;

import java.util.List;

public class FlashcardsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private EditText questionEditText;
    private EditText answerEditText;
    private Button addFlashcardButton;
    private Button deleteFlashcardButton;
    private ListView flashcardsListView;
    private FlashcardManager flashcardManager;
    private String username;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        questionEditText = findViewById(R.id.questionEditText);
        answerEditText = findViewById(R.id.answerEditText);
        addFlashcardButton = findViewById(R.id.addFlashcardButton);
        flashcardsListView = findViewById(R.id.flashcardsListView);
        flashcardManager = new FlashcardManager(this);
        username = getIntent().getStringExtra("USER_NAME");

        addFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = "Question: " + questionEditText.getText().toString();
                String answer = "Answer: " + answerEditText.getText().toString();
                flashcardManager.addFlashcard(username, question, answer);
                Toast.makeText(FlashcardsActivity.this, "Flashcard added", Toast.LENGTH_SHORT).show();
                loadFlashcards();
            }
        });

        loadFlashcards();
    }

    private void loadFlashcards() {
        List<Flashcard> flashcards = flashcardManager.getFlashcards(username);
        FlashcardAdapter adapter = new FlashcardAdapter(this, flashcards, flashcardManager, username);
        flashcardsListView.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, HomePageActivity.class));
        } else if (id == R.id.nav_todo) {
            startActivity(new Intent(this, TodoListActivity.class));
        } else if (id == R.id.nav_notes) {
            startActivity(new Intent(this, NoteListsActivity.class));
        } else if (id == R.id.nav_flashcards) {
            // Already on Flashcards Activity
        } else if (id == R.id.nav_timer) {
            startActivity(new Intent(this, PomodoroActivity.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
