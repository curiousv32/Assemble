package com.example.assemble.util;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assemble.R;
import com.example.assemble.model.Task;

import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {
    private List<Task> tasks;

    public TodoListAdapter(List<Task> tasks) {
        this.tasks = tasks;
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
        holder.taskDeadline.setText(task.getDeadline().toString());
        holder.taskPriority.setText(task.getPriority());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(List<Task> newTasks) {
        this.tasks = newTasks;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskDescription, taskDeadline, taskPriority;

        public ViewHolder(View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            taskDeadline = itemView.findViewById(R.id.taskDeadline);
            taskPriority = itemView.findViewById(R.id.taskPriority);
        }
    }
}
