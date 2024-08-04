package com.example.assemble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assemble.R;
import com.example.assemble.exceptions.InvalidNoteException;
import com.example.assemble.model.Note;
import com.example.assemble.service.NoteManager;
import com.example.assemble.util.NoteAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class NoteListsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_lists_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Button createNote = findViewById(R.id.note_create);
        EditText name = findViewById(R.id.new_note_name);
        NoteManager noteManager = NoteManager.getInstance(this);

        ArrayList<Note> notes = new ArrayList<>(noteManager.getNotes());

        RecyclerView noteLists = findViewById(R.id.notes);
        noteLists.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter<NoteAdapter.NoteViewHolder> adapter = new NoteAdapter(this, notes);

        noteLists.setAdapter(adapter);

        createNote.setOnClickListener(v -> {
            if (name.getText().length() > 0 && name.getText().length() <= NoteManager.MAX_NOTE_NAME_SIZE) {

                if (!noteManager.contains(name.getText().toString())) {
                    try {
                        Note note = noteManager.createNote(name.getText().toString());

                        if (note != null) {
                            noteManager.setOpenedNote(note);
                            Intent intent = new Intent(this, NoteActivity.class);
                            startActivity(intent);
                        }
                    } catch (InvalidNoteException exception) {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                        name.setText("");
                        exception.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Note already exists", Toast.LENGTH_LONG).show();
                    name.setText("");
                }
            } else {
                Toast.makeText(this, "Invalid note name", Toast.LENGTH_LONG).show();
                name.setText("");
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, HomePageActivity.class));
        } else if (id == R.id.nav_todo) {
            startActivity(new Intent(this, TodoListActivity.class));
        } else if (id == R.id.nav_notes) {
            // Already on NoteLists Activity
        } else if (id == R.id.nav_flashcards) {
            startActivity(new Intent(this, FlashcardsActivity.class));
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
