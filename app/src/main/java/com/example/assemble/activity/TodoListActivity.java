package com.example.assemble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assemble.R;
import com.example.assemble.model.Task;
import com.example.assemble.service.SessionManager;
import com.example.assemble.service.TaskManager;
import com.example.assemble.util.TodoListAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class TodoListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private TodoListAdapter adapter;
    private List<Task> tasks;
    private DrawerLayout drawerLayout;

    String ownerUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //if null, set to default owner
        ownerUUID = SessionManager.getInstance().getCurrentUserID() == null ? SessionManager.getInstance().getDefaultOwnerID().toString() : SessionManager.getInstance().getCurrentUserID().toString();
        recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasks = TaskManager.getInstance(this).getAllTasks(ownerUUID);
        adapter = new TodoListAdapter(tasks);
        recyclerView.setAdapter(adapter);
    }

    public void launchTodoForm(View view) {
        Intent intent = new Intent(this, TodoFormActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Task> tasks = TaskManager.getInstance(this).getAllTasks(ownerUUID);
        adapter.setTasks(tasks);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, HomePageActivity.class));
        } else if (id == R.id.nav_todo) {
            // Already on Todo List Activity
        } else if (id == R.id.nav_notes) {
            startActivity(new Intent(this, NoteListsActivity.class));
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
