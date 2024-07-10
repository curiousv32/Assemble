package com.example.assemble.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assemble.R;
import com.example.assemble.activity.HomePageActivity;
import com.example.assemble.activity.NoteActivity;
import com.example.assemble.model.Note;
import com.example.assemble.service.NoteManager;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context context;
    private ArrayList<Note> notes;

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
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

        public NoteViewHolder(@NonNull View noteView) {
            super(noteView);

            noteName = noteView.findViewById(R.id.note_name);
            lastUpdatedDate = noteView.findViewById(R.id.note_last_updated);
        }
    }
}
