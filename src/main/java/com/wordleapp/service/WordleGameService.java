package com.wordleapp.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
public class WordleGameService {

    private static final String SECRET_WORD = "PLANE"; // Hardcoded for now
    private final Map<String, Integer> attemptsMap = new HashMap<>();
    private final Map<String, Boolean> gameWonMap = new HashMap<>(); //  New map to manage if user won


    public ResponseEntity<String> checkWord(@RequestParam String guess, @RequestParam String user) {
        validateGuess(guess);

        // If user already won, he cannot keep playing
        if (Boolean.TRUE.equals(gameWonMap.get(user))) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Game over! You've already won.");
        }

        attemptsMap.putIfAbsent(user, 0);

        int attempts = attemptsMap.get(user);

        if (attempts >= 6) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "You have reached the maximum number of attempts.");
        }

        if (guess.equalsIgnoreCase(SECRET_WORD)) {
            gameWonMap.put(user, true); // Mark user as winner
            return ResponseEntity.ok("Correct!");
        }

        attempts++;
        attemptsMap.put(user, attempts);

        return (attempts == 6)
                ? ResponseEntity.ok(("Game over! You've used all attempts."))
                : ResponseEntity.ok("Try again! Attempts left: " + (6 - attempts));

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

    public void resetGame(String user) {
        attemptsMap.remove(user);
        gameWonMap.remove(user);
    }

}
