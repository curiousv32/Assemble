package com.example.assemble.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assemble.R;
import com.example.assemble.activity.NoteActivity;
import com.example.assemble.model.Note;
import com.example.assemble.model.Task;
import com.example.assemble.service.NoteManager;
import com.example.assemble.service.TaskManager;

import java.util.ArrayList;
import java.util.UUID;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context context;
    private ArrayList<Note> notes;
    private UUID currentTaskId; // Curr task ID to link notes to

    public NoteAdapter(Context context, ArrayList<Note> notes, UUID taskId) {
        this.context = context;
        this.notes = notes;
        this.currentTaskId = taskId;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);

        holder.noteName.setText(note.getName());
        holder.lastUpdatedDate.setText(note.getLastUpdatedDate().toString());

        if (currentTaskId == null) {
            holder.linkButton.setVisibility(View.GONE);
        } else {
            holder.linkButton.setVisibility(View.VISIBLE);
            holder.linkButton.setOnClickListener(v -> {
                TaskManager.getInstance(context).linkNoteToTask(currentTaskId, note.getID());
                Toast.makeText(context, "Linking " + note.getName() + " to task: " + TaskManager.getInstance(context).get(currentTaskId, Task.class).getTitle(), Toast.LENGTH_SHORT).show();
            });
        }

        holder.itemView.setOnClickListener(v -> {
            NoteManager.getInstance(context).setOpenedNote(note);
            context.startActivity(new Intent(context, NoteActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteName;
        TextView lastUpdatedDate;
        Button linkButton;

        public NoteViewHolder(@NonNull View noteView) {
            super(noteView);

            noteName = noteView.findViewById(R.id.note_name);
            lastUpdatedDate = noteView.findViewById(R.id.note_last_updated);
            linkButton = noteView.findViewById(R.id.link_to_task_button);
        }
    }
}
