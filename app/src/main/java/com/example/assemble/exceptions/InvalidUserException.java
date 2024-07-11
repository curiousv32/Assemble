package com.example.assemble.exceptions;

public class InvalidUserException extends Exception{

    public InvalidUserException(String errorMessage) {
        super(errorMessage);
    }
}
