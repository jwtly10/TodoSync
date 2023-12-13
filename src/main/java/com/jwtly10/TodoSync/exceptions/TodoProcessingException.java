package com.jwtly10.TodoSync.exceptions;

public class TodoProcessingException extends RuntimeException {
    public TodoProcessingException(String message) {
        super(message);
    }
}