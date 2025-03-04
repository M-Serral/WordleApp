package com.wordleapp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", ex.getStatusCode().value());

        // Fix: The error message now matches the status code.
        String errorType = switch (ex.getStatusCode().value()) {
            case 400 -> "Bad Request";
            case 429 -> "Too Many Requests";
            default -> "Error";
        };

        body.put("error", errorType);
        body.put("message", ex.getReason() != null ? ex.getReason() : "Invalid request");

        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }
}
