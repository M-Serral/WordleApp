package com.wordleapp.service;

import com.wordleapp.util.Constants;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Service
public class WordleGameService {

    public ResponseEntity<String> checkWord(String guess, HttpSession session) {

        try {
            validateGameState(session);
            validateGuess(guess);
        } catch (ResponseStatusException e) {
            String lastHint = (String) session.getAttribute(Constants.LAST_HINT_KEY);

            HttpStatus status = e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS
                    ? HttpStatus.TOO_MANY_REQUESTS
                    : HttpStatus.BAD_REQUEST;

            throw new ResponseStatusException(status,
                    e.getReason() + (lastHint != null ? " Hint: " + lastHint : ""));
        }

        String hint = generateHint(guess, session);
        session.setAttribute(Constants.LAST_HINT_KEY, hint);

        if (guess.equalsIgnoreCase(Constants.SECRET_WORD)) {
            session.setAttribute(Constants.GAME_WON_KEY, true);
            return ResponseEntity.ok("Correct! The word was: " + Constants.SECRET_WORD + ". Hint: " + hint);
        }

        int attempts = updateAttempts(session);
        return buildResponse(attempts, hint);

    }

    private void validateGameState(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute(Constants.GAME_WON_KEY))) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Game over! You've already won.");
        }

        if (getAttempts(session) >= Constants.MAX_ATTEMPTS) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "You have reached the maximum number of attempts.");
        }
    }

    private void validateGuess(String guess) {
        if (!guess.matches("^[A-Za-zñÑ]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: Only characters from the alphabet are allowed.");
        }
        if (guess.length() != Constants.WORD_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: The word must be " + Constants.WORD_LENGTH + " letters long.");
        }
    }

    private String generateHint(String guess, HttpSession session) {
        // Get the hint stored in the session or initialize it with “_”.
        char[] hint = (char[]) session.getAttribute(Constants.HINT_SESSION_KEY);
        if (hint == null) {
            hint = new char[Constants.WORD_LENGTH];
            Arrays.fill(hint, '_'); // Initially the whole word is “_”.
        }

        // Convert input to uppercase
        String upperGuess = guess.toUpperCase();

        // Compare the attempt with the secret word and update the hint
        for (int i = 0; i < Constants.WORD_LENGTH; i++) {
            if (upperGuess.charAt(i) == Constants.SECRET_WORD.charAt(i)) {
                hint[i] = upperGuess.charAt(i); // Letter is in the correct position
            }
        }

        // Save the updated hint in the session
        session.setAttribute(Constants.HINT_SESSION_KEY, hint);

        // Convert hint to space-separated string format
        return new String(hint).replace("", " ").trim();
    }


    private int getAttempts(HttpSession session) {
        return session.getAttribute(Constants.ATTEMPTS_KEY) != null ? (int) session.getAttribute(Constants.ATTEMPTS_KEY) : 0;
    }

    private int updateAttempts(HttpSession session) {
        int attempts = getAttempts(session) + 1;
        session.setAttribute(Constants.ATTEMPTS_KEY, attempts);
        return attempts;
    }

    private ResponseEntity<String> buildResponse(int attempts, String hint) {
        return ResponseEntity.ok(
                attempts == Constants.MAX_ATTEMPTS
                        ? "Game over! You've used all attempts. Hint: " + hint
                        : "Try again! Attempts left: " + (Constants.MAX_ATTEMPTS - attempts) + ". Hint: " + hint
        );
    }
}
