package com.example.exception;

public class GroupNotFoundException extends RuntimeException  {

    public GroupNotFoundException(String message) {
        super(message);
    }
}
