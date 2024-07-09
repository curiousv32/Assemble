package com.example.assemble.database;
import com.example.assemble.model.Flashcard;

import java.util.ArrayList;
import java.util.List;

public class FlashcardsStub {
    private List<Flashcard> flashcards = new ArrayList<>();
    private String userName = "dave";

    public void addFlashcard(String userName, String question, String answer) {
        flashcards.add(new Flashcard(userName, question, answer));
    }

    public List<Flashcard> getFlashcards(String username) {
        List<Flashcard> userFlashcards = new ArrayList<>();
        for (Flashcard flashcard : flashcards) {
            if (flashcard.getUsername().equals(username)) {
                userFlashcards.add(flashcard);
            }
        }
        return userFlashcards;
    }
}
