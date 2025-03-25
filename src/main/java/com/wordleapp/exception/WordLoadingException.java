package com.wordleapp.exception;

public class WordLoadingException extends RuntimeException {
    public WordLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
