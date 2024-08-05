package com.example.assemble.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assemble.R;
import com.example.assemble.activity.NoteListsActivity;
import com.example.assemble.activity.TodoFormActivity;
import com.example.assemble.model.Note;
import com.example.assemble.model.Task;
import com.example.assemble.service.TaskManager;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {
    private static List<Task> tasks;

    public TodoListAdapter(List<Task> tasks) {
        TodoListAdapter.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.taskTitle.setText(task.getTitle());
        holder.taskDescription.setText(task.getDescription());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(task.getDeadline());
        holder.taskDeadline.setText(formattedDate);

        holder.taskPriority.setText(task.getPriority());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(List<Task> newTasks) {
        tasks = newTasks;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View deleteButton, editButton, relatedNotesButton;
        TextView taskTitle, taskDescription, taskDeadline, taskPriority;

        public ViewHolder(View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            taskDeadline = itemView.findViewById(R.id.taskDeadline);
            taskPriority = itemView.findViewById(R.id.taskPriority);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);

            relatedNotesButton = itemView.findViewById(R.id.relatedNotesButton);
            relatedNotesButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Task task = tasks.get(position);
                    showRelatedNotesDialog(task.getId());
                }
            });

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Task task = tasks.get(position);
                    TaskManager.getInstance(itemView.getContext()).delete(task.getId());
                    tasks.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(itemView.getContext(), "Task deleted", Toast.LENGTH_SHORT).show();

                }
            });

            editButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Task task = tasks.get(position);
                    Intent intent = new Intent(itemView.getContext(), TodoFormActivity.class);
                    intent.putExtra("TASK_ID", task.getId().toString());
                    itemView.getContext().startActivity(intent);
                }
            });

        }

        private void showRelatedNotesDialog(UUID taskId) {
            try {
                List<Note> linkedNotes = TaskManager.getInstance(itemView.getContext()).getLinkedNotesForTask(taskId);
                NoteLinkAdapter adapter = new NoteLinkAdapter(itemView.getContext(), linkedNotes, taskId);

                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setTitle("Related Notes");

                ListView listView = new ListView(itemView.getContext());
                listView.setAdapter(adapter);
                builder.setView(listView);

                builder.setPositiveButton("Link New Note", (dialog, which) -> {
                    Intent linkIntent = new Intent(itemView.getContext(), NoteListsActivity.class);
                    linkIntent.putExtra("TASK_ID", taskId.toString());
                    itemView.getContext().startActivity(linkIntent);
                });

                builder.setNegativeButton("Close", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            } catch (SQLException e) {
                Toast.makeText(itemView.getContext(), "Error loading notes: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }
}
