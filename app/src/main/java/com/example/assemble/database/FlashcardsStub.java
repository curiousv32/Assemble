package com.example.assemble.database;
import com.example.assemble.model.Flashcard;

import java.util.ArrayList;
import java.util.List;

public class FlashcardsStub {
    private List<Flashcard> flashcards = new ArrayList<>();
    private int nextId = 1;

    public void addFlashcard(int userId, String question, String answer) {
        flashcards.add(new Flashcard(nextId++, userId, question, answer));
    }

    public List<Flashcard> getFlashcards(int userId) {
        List<Flashcard> userFlashcards = new ArrayList<>();
        for (Flashcard flashcard : flashcards) {
            if (flashcard.getUserId() == userId) {
                userFlashcards.add(flashcard);
            }
        }
        return userFlashcards;
    }
}
