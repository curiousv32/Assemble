package com.example.assemble.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.assemble.R;
import com.example.assemble.activity.NoteActivity;
import com.example.assemble.model.Note;
import com.example.assemble.service.NoteManager;
import com.example.assemble.service.TaskManager;

import java.util.List;
import java.util.UUID;

public class NoteLinkAdapter extends ArrayAdapter<Note> {
    private Context context;
    private List<Note> notes;
    private UUID taskId;

    public NoteLinkAdapter(Context context, List<Note> notes, UUID taskId) {
        super(context, 0, notes);
        this.context = context;
        this.notes = notes;
        this.taskId = taskId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dialog_task_related_notes_list, parent, false);
        }

        TextView noteTitle = convertView.findViewById(R.id.note_title);
        Button removeButton = convertView.findViewById(R.id.remove_note_button);

        Note note = getItem(position);
        noteTitle.setText(note.getName());
        noteTitle.setOnClickListener(v -> {
            NoteManager.getInstance(context).setOpenedNote(note);
            Intent intent = new Intent(context, NoteActivity.class);
            context.startActivity(intent);
        });

        removeButton.setOnClickListener(v -> {
            if (taskId != null) {
                TaskManager.getInstance(context).removeLinkedNote(taskId, note.getID());
            }
            this.notes.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }


}
