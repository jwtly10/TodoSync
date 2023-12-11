package com.jwtly10.TodoSync.exceptions;

public class MissingConfigException extends RuntimeException {
    public MissingConfigException(String message) {
        super(message);
    }
}
