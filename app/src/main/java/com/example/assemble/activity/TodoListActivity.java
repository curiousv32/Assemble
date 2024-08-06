package com.example.assemble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assemble.R;
import com.example.assemble.model.Task;
import com.example.assemble.service.SessionManager;
import com.example.assemble.service.TaskManager;
import com.example.assemble.util.TodoListAdapter;

import java.util.List;

public class TodoListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TodoListAdapter adapter;
    private List<Task> tasks;

    String ownerUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

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

}

