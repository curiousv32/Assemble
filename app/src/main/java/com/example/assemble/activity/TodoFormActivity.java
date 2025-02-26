package com.example.assemble.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assemble.R;
import com.example.assemble.model.Task;
import com.example.assemble.service.TaskManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class TodoFormActivity extends AppCompatActivity {
    private EditText taskTitle, taskDescription, taskDeadline;
    private Spinner taskPriority;
    private UUID taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_form);

        taskTitle = findViewById(R.id.taskTitle);
        taskDescription = findViewById(R.id.taskDescription);
        taskDeadline = findViewById(R.id.taskDeadline);
        taskPriority = findViewById(R.id.taskPriority);
        Button submitButton = findViewById(R.id.submitButton);

        // Set up the priority dropdown
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priorities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskPriority.setAdapter(adapter);

        taskId = getIntent().hasExtra("TASK_ID") ? UUID.fromString(getIntent().getStringExtra("TASK_ID")) : null;
        if (taskId != null) {
            loadTaskDetails(taskId);
        }

        // deadline picker
        taskDeadline.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(TodoFormActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> taskDeadline.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            datePickerDialog.show();
        });

        // submit button
        submitButton.setOnClickListener(v -> {
            if (validateInput()) {
                saveTask();
            } else {
                Toast.makeText(this, "Please fill all fields correctly.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput() {
        return !taskTitle.getText().toString().isEmpty() &&
                !taskDescription.getText().toString().isEmpty() &&
                !taskDeadline.getText().toString().isEmpty();
    }

    private void saveTask() {
        String title = taskTitle.getText().toString();
        String description = taskDescription.getText().toString();
        String deadline = taskDeadline.getText().toString();
        String priority = taskPriority.getSelectedItem().toString();
        Date parsedDate = parseDate(deadline);

        TaskManager taskManager = TaskManager.getInstance(this);

        try {
            if (taskId == null) {
                Task newTask = new Task(UUID.randomUUID(), title, description, parsedDate, priority, "Pending", new ArrayList<>() );
                taskManager.add(newTask);
                Toast.makeText(this, "Task added successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Task existingTask = new Task(taskId, title, description, parsedDate, priority, "Pending", new ArrayList<>());
                taskManager.update(taskId, existingTask);
                Toast.makeText(this, "Task updated successfully!", Toast.LENGTH_SHORT).show();
            }
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to save task: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    private void loadTaskDetails(UUID taskId) {
        Task task = TaskManager.getInstance(this).get(taskId, Task.class);
        if (task != null) {
            taskTitle.setText(task.getTitle());
            taskDescription.setText(task.getDescription());
            taskDeadline.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(task.getDeadline()));
            taskPriority.setSelection(((ArrayAdapter<String>) taskPriority.getAdapter()).getPosition(task.getPriority()));
        }
    }


}
