package com.wordleapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/wordle")
public class WordleController {

    private static final String SECRET_WORD = "PLANE"; // Hardcoded for now
    private final Map<String, Integer> attemptsMap = new HashMap<>();
    private final Map<String, Boolean> gameWonMap = new HashMap<>(); //  New map to manage if user won


    @PostMapping("/guess")
    public String checkWord(@RequestParam String guess, @RequestParam String user) {
        validateGuess(guess);

        // If user already won, he cannot keep playing
        if (gameWonMap.getOrDefault(user, false)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Game over! You've already won.");
        }

        if (!attemptsMap.containsKey(user)) {
            attemptsMap.put(user, 0);
        }

        int attempts = attemptsMap.get(user);

        if (attempts >= 6) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Game over! You've used all attempts.");
        }

        if (guess.equalsIgnoreCase(SECRET_WORD)) {
            gameWonMap.put(user, true); // Mark user as winner
            return "Correct!";
        }

        attempts++;
        attemptsMap.put(user, attempts);

        if (attempts >= 6) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Game over! You've used all attempts.");
        }

        return "Try again! Attempts left: " + (6 - attempts);    }

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

    @PostMapping("/reset")
    public String resetGame(@RequestParam String user) {
        attemptsMap.put(user, 0);
        gameWonMap.put(user, false); // reset user victory
        return "Game reset! You have 6 attempts.";
    }

}
