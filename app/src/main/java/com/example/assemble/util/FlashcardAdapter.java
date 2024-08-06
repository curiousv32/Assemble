package com.example.assemble.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.assemble.R;
import com.example.assemble.model.Flashcard;
import com.example.assemble.service.FlashcardManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlashcardAdapter extends ArrayAdapter<Flashcard> {
    private Context context;
    private List<Flashcard> flashcards;
    private FlashcardManager flashcardManager;
    private String username;
    private Set<Integer> showingAnswers;

    public FlashcardAdapter(@NonNull Context context, @NonNull List<Flashcard> flashcards, FlashcardManager flashcardManager, String username) {
        super(context, R.layout.flashcard_item, flashcards);
        this.context = context;
        this.flashcards = flashcards;
        this.flashcardManager = flashcardManager;
        this.username = username;
        this.showingAnswers = new HashSet<>();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.flashcard_item, parent, false);
        }

        final Flashcard flashcard = getItem(position);
        final TextView flashcardQuestionTextView = convertView.findViewById(R.id.flashcardQuestionTextView);
        Button deleteFlashcardButton = convertView.findViewById(R.id.deleteFlashcardButton);

        // Set the text based on whether the answer is being shown or not
        if (showingAnswers.contains(position)) {
            flashcardQuestionTextView.setText(flashcard.getAnswer());
        } else {
            flashcardQuestionTextView.setText(flashcard.getQuestion());
        }

        // Set an onClickListener to toggle between question and answer with animation
        flashcardQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation flipOut = AnimationUtils.loadAnimation(context, R.anim.flip_out);
                Animation flipIn = AnimationUtils.loadAnimation(context, R.anim.flip_in);

                flipOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // No-op
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (showingAnswers.contains(position)) {
                            showingAnswers.remove(position);
                            flashcardQuestionTextView.setText(flashcard.getQuestion());
                        } else {
                            showingAnswers.add(position);
                            flashcardQuestionTextView.setText(flashcard.getAnswer());
                        }
                        flashcardQuestionTextView.startAnimation(flipIn);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                flashcardQuestionTextView.startAnimation(flipOut);
            }
        });

        // Set up delete button functionality
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
