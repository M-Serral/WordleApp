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
        if (guess.length() != 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: The word must be 5 letters long.");
        }

        // Código duplicado: validación en dos lugares diferentes
        boolean isValid = true;
        for (char c : guess.toCharArray()) {
            if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == 'ñ' || c == 'Ñ')) {
                isValid = false;
            }
        }

        // Complejidad innecesaria: condicional anidado
        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: only characters from alphabet are allowed.");        }



        return guess.equalsIgnoreCase(SECRET_WORD) ? "Correct!" : "Try again!";
    }
}

