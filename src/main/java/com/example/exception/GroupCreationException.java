package com.example.exception;

public class GroupCreationException extends RuntimeException {

    public GroupCreationException(String message) {
        super(message);
    }
}