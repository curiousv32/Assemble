package com.example.assemble.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.example.assemble.R;
import com.example.assemble.model.Flashcard;
import com.example.assemble.service.FlashcardManager;

import java.util.List;

public class FlashcardAdapter extends ArrayAdapter<Flashcard> {
    private Context context;
    private List<Flashcard> flashcards;
    private FlashcardManager flashcardManager;
    private String username;

    public FlashcardAdapter(@NonNull Context context, @NonNull List<Flashcard> flashcards, FlashcardManager flashcardManager, String username) {
        super(context, R.layout.flashcard_item, flashcards);
        this.context = context;
        this.flashcards = flashcards;
        this.flashcardManager = flashcardManager;
        this.username = username;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.flashcard_item, parent, false);
        }

        TextView flashcardQuestionTextView = convertView.findViewById(R.id.flashcardQuestionTextView);
        Button deleteFlashcardButton = convertView.findViewById(R.id.deleteFlashcardButton);

        final Flashcard flashcard = getItem(position);
        flashcardQuestionTextView.setText(flashcard.getQuestion());

        deleteFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardManager.deleteFlashcard(username, flashcard.getQuestion());
                Toast.makeText(context, "Flashcard deleted", Toast.LENGTH_SHORT).show();
                flashcards.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
