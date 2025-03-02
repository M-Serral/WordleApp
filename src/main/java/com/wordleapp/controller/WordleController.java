package com.wordleapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/wordle")
public class WordleController {

    private static final String SECRET_WORD = "APPLE"; // Hardcoded for now

    @PostMapping("/guess")
    public String checkWord(@RequestParam String guess) {

        // Validación de caracteres: Solo letras del alfabeto inglés + "ñ"
        if (!guess.matches("[A-Za-zñÑ]{5}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: Only characters from alphabet are allowed.");
        }

        if (guess.length() != 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: The word must be 5 letters long.");
        }

        return guess.equalsIgnoreCase(SECRET_WORD) ? "Correct!" : "Try again!";
    }
}
