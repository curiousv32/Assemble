package com.example.assemble.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;
import java.util.UUID;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context context;
    private List<Note> notes;
    private UUID currentTaskId; // Curr task ID to link notes to

    public NoteAdapter(Context context, List<Note> notes, UUID taskId) {
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

        holder.deleteButton.setOnClickListener(v ->
            new AlertDialog.Builder(context)
            .setTitle("Confirm delete")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes", (dialogInterface, i) -> {
                NoteManager.getInstance(context).delete(note.getID());
                notes.remove(position);
                Toast.makeText(context, "Note deleted", Toast.LENGTH_LONG).show();
                notifyItemRemoved(position);
            })
            .setNegativeButton("No", null).create().show());

        holder.renameButton.setOnClickListener(v ->
        {
            EditText editText = new EditText(context);
            editText.setHint("New note name");

            new AlertDialog.Builder(context)
            .setTitle("Confirm rename")
            .setView(editText)
            .setMessage("Are you sure you want to rename this note?")
            .setPositiveButton("Yes", (dialogInterface, i) -> {
                if (editText.getText().length() <= NoteManager.MAX_NOTE_NAME_SIZE) {
                    NoteManager.getInstance(context).rename(note, editText.getText().toString());
                    Toast.makeText(context, "Note renamed", Toast.LENGTH_LONG).show();
                    notifyItemChanged(position);
                } else {
                    Toast.makeText(context, "Name too long", Toast.LENGTH_LONG).show();
                }
            })
            .setNegativeButton("No", null).create().show();
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteName;
        TextView lastUpdatedDate;
        Button deleteButton;
        Button renameButton;
        Button linkButton;

        public NoteViewHolder(@NonNull View noteView) {
            super(noteView);

            noteName = noteView.findViewById(R.id.note_name);
            lastUpdatedDate = noteView.findViewById(R.id.note_last_updated);
            deleteButton = noteView.findViewById(R.id.delete_button);
            renameButton = noteView.findViewById(R.id.rename_button);
            linkButton = noteView.findViewById(R.id.link_to_task_button);
        }
    }
}
