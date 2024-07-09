package com.example.assemble.model;
public class Flashcard{
    private String username;
    private String question;
    private String answer;

    // Constructor
    public Flashcard(String username, String question, String answer) {
        this.username= username;
        this.question = question;
        this.answer = answer;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
