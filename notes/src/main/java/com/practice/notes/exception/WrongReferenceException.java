package com.practice.notes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class WrongReferenceException extends RuntimeException {
    public WrongReferenceException(String message) {
        super(message);
    }

    public WrongReferenceException(String message, Throwable cause) {
        super(message, cause);
    }
}