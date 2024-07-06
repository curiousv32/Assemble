package com.example.assemble.model;
public class Flashcard{
    private int id;
    private int userId;
    private String question;
    private String answer;

    // Constructor
    public Flashcard(int id, int userId, String question, String answer) {
        this.id = id;
        this.userId = userId;
        this.question = question;
        this.answer = answer;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
