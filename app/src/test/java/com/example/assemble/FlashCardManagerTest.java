package com.example.assemble;

import static org.junit.Assert.*;

import com.example.assemble.model.Flashcard;
import com.example.assemble.service.FlashcardManager;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class FlashCardManagerTest {
    private FlashcardManager flashcardManager;
    @Before
    public void setUp() {
        flashcardManager = new FlashcardManager();
    }

    @Test
    public void testAddFlashcardStub() {
        String username = "user1";
        String question = "What is Java?";
        String answer = "A programming language";

        flashcardManager.addFlashcard(username, question, answer);

        List<Flashcard> flashcards = flashcardManager.getFlashcards(username);
        assertEquals(1, flashcards.size());
        assertEquals(question, flashcards.get(0).getQuestion());
        assertEquals(answer, flashcards.get(0).getAnswer());
    }

    @Test
    public void testDeleteFlashcardStub() {
        String username = "user1";
        String question = "What is Java?";
        String answer = "A programming language";

        flashcardManager.addFlashcard(username, question, answer);
        flashcardManager.deleteFlashcard(username, question);

        List<Flashcard> flashcards = flashcardManager.getFlashcards(username);
        assertTrue(flashcards.isEmpty());
    }

    @Test
    public void testGetFlashcardsStub() {
        String username = "user1";
        String question1 = "What is Java?";
        String answer1 = "A programming language";
        String question2 = "What is Python?";
        String answer2 = "Another programming language";

        flashcardManager.addFlashcard(username, question1, answer1);
        flashcardManager.addFlashcard(username, question2, answer2);

        List<Flashcard> flashcards = flashcardManager.getFlashcards(username);
        assertEquals(2, flashcards.size());
    }
}
