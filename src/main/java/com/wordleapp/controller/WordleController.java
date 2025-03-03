package com.wordleapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/wordle")
public class WordleController {

    private static final String SECRET_WORD = "PLANE"; // Hardcoded for now

    @PostMapping("/guess")
    public String checkWord(@RequestParam String guess) {
        validateGuess(guess);
        return guess.equalsIgnoreCase(SECRET_WORD) ? "Correct!" : "Try again!";
    }

    /**
     * Validates the guess, ensuring it is exactly 5 letters and contains only allowed characters.
     * @param guess The input word to validate.
     */
    private void validateGuess(String guess) {
        String errorMessage = null;

        if (!guess.matches("[A-Za-zñÑ]+")) {
            errorMessage = "Invalid input: Only characters from the alphabet are allowed.";
        } else if (guess.length() != 5) {
            errorMessage = "Invalid input: The word must be 5 letters long.";
        }

        if (errorMessage != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }

}
