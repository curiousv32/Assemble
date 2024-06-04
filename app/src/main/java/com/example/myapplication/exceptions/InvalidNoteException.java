package com.example.myapplication.exceptions;

public class InvalidNoteException extends Exception {

    public InvalidNoteException(String errorMessage) {
        super(errorMessage);
    }
}
