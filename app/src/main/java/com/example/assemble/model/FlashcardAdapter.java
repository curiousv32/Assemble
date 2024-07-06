package com.example.assemble.model;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.assemble.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlashcardAdapter extends BaseAdapter {
    private Context context;
    private List<Flashcard> flashcards;
    private Set<Integer> showingAnswers;

    public FlashcardAdapter(Context context, List<Flashcard> flashcards) {
        this.context = context;
        this.flashcards = flashcards;
        this.showingAnswers = new HashSet<>();
    }

    @Override
    public int getCount() {
        return flashcards.size();
    }

    @Override
    public Object getItem(int position) {
        return flashcards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.flashcard_item, parent, false);
        }

        final Flashcard flashcard = flashcards.get(position);
        final TextView textView = convertView.findViewById(R.id.flashcardTextView);

        // Set the text based on whether the answer is being shown or not
        if (showingAnswers.contains(position)) {
            textView.setText(flashcard.getAnswer());
        } else {
            textView.setText(flashcard.getQuestion());
        }

        // Set an onClickListener to toggle between question and answer with animation
        textView.setOnClickListener(new View.OnClickListener() {
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
                            textView.setText(flashcard.getQuestion());
                        } else {
                            showingAnswers.add(position);
                            textView.setText(flashcard.getAnswer());
                        }
                        textView.startAnimation(flipIn);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                textView.startAnimation(flipOut);
            }
        });

        return convertView;
    }
}