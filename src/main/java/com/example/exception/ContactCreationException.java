package com.example.exception;

public class ContactCreationException extends RuntimeException  {

    public ContactCreationException(String message) {
        super(message);
    }
}
