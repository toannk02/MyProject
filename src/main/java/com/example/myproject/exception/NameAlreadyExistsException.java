package com.example.myproject.exception;

public class NameAlreadyExistsException extends RuntimeException {
    public NameAlreadyExistsException(String field) {
        super(field + " is already existed!");
    }
}
