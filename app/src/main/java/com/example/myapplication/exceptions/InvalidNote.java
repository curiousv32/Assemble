package com.example.myapplication.exceptions;

public class InvalidNote extends Exception {

    public InvalidNote(String errorMessage) {
        super(errorMessage);
    }
}
