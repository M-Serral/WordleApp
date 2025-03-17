package com.wordleapp.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Service
public class WordleGameService {

    private static final String SECRET_WORD = "SEXTO";
    private static final String ATTEMPTS_KEY = "attempts";
    private static final String GAME_WON_KEY = "gameWon";
    private static final int MAX_ATTEMPTS = 6;
    private static final int WORD_LENGTH = 5;
    public static final String LAST_HINT_KEY = "lastHint";
    private static final String HINT_SESSION_KEY = "wordle_hint";



    public ResponseEntity<String> checkWord(String guess, HttpSession session) {

        validateGameState(session);

        try {
            validateGuess(guess);
        } catch (ResponseStatusException e) {
            // If there is an error, it returns the message together with the last saved hint.
            String lastHint = (String) session.getAttribute(LAST_HINT_KEY);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    e.getReason() + (lastHint != null ? " Hint: " + lastHint : ""));
        }

        if (guess.equalsIgnoreCase(SECRET_WORD)) {
            session.setAttribute(GAME_WON_KEY, true);
            return ResponseEntity.ok("Correct! The word was: " + SECRET_WORD);
        }

        String hint = generateHint(guess, session);
        session.setAttribute(LAST_HINT_KEY, hint);

        int attempts = updateAttempts(session);
        return buildResponse(attempts, hint);
    }

    public void resetGame(HttpSession session) {
        session.removeAttribute(ATTEMPTS_KEY);
        session.removeAttribute(GAME_WON_KEY);
        session.removeAttribute(LAST_HINT_KEY);
    }

    private void validateGameState(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute(GAME_WON_KEY))) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Game over! You've already won.");
        }

        if (getAttempts(session) >= MAX_ATTEMPTS) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "You have reached the maximum number of attempts.");
        }
    }

    private void validateGuess(String guess) {
        if (!guess.matches("^[A-Za-zñÑ]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: Only characters from the alphabet are allowed.");
        }
        if (guess.length() != WORD_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: The word must be " + WORD_LENGTH + " letters long.");
        }
    }

    public String generateHint(String guess, HttpSession session) {
        // Get the hint stored in the session or initialize it with “_”.
        char[] hint = (char[]) session.getAttribute(HINT_SESSION_KEY);
        if (hint == null) {
            hint = new char[WORD_LENGTH];
            Arrays.fill(hint, '_'); // Initially the whole word is “_”.
        }

        // Convert input to uppercase
        String upperGuess = guess.toUpperCase();

        // Compare the attempt with the secret word and update the hint
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (upperGuess.charAt(i) == SECRET_WORD.charAt(i)) {
                hint[i] = upperGuess.charAt(i); // Mantener la letra correcta
            }
        }

        // Save the updated hint in the session
        session.setAttribute(HINT_SESSION_KEY, hint);

        // Convert hint to space-separated string format
        return new String(hint).replace("", " ").trim();
    }


    private int getAttempts(HttpSession session) {
        return session.getAttribute(ATTEMPTS_KEY) != null ? (int) session.getAttribute(ATTEMPTS_KEY) : 0;
    }

    private int updateAttempts(HttpSession session) {
        int attempts = getAttempts(session) + 1;
        session.setAttribute(ATTEMPTS_KEY, attempts);
        return attempts;
    }

    private ResponseEntity<String> buildResponse(int attempts, String hint) {
        return ResponseEntity.ok(
                attempts == MAX_ATTEMPTS
                        ? "Game over! You've used all attempts. Hint: " + hint
                        : "Try again! Attempts left: " + (MAX_ATTEMPTS - attempts) + ". Hint: " + hint
        );
    }
}
