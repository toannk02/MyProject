package com.example.myproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NameAlreadyExistsException extends RuntimeException {
    public NameAlreadyExistsException(String field) {
        super(field + " is already existed!");
    }
}
