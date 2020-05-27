package com.example.restservice.exception;

public class ThrottledException extends RuntimeException {
    public ThrottledException(String message) {
        super(message);
    }

    public ThrottledException(String message, Throwable cause) {
        super(message, cause);
    }
}
