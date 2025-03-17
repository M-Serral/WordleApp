package com.wordleapp.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class WordleGameService {

    private static final String SECRET_WORD = "SEXTO";
    private static final String ATTEMPTS_KEY = "attempts";
    private static final String GAME_WON_KEY = "gameWon";
    private static final int MAX_ATTEMPTS = 6;
    private static final int WORD_LENGTH = 5;
    private static final String LAST_HINT_KEY = "lastHint";

    public ResponseEntity<String> checkWord(String guess, HttpSession session) {

        validateGameState(session);

        try {
            validateGuess(guess);
        } catch (ResponseStatusException e) {
            // Si hay error, devuelve el mensaje junto con la última pista guardada
            String lastHint = (String) session.getAttribute(LAST_HINT_KEY);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    e.getReason() + (lastHint != null ? " Hint: " + lastHint : ""));        }

        if (guess.equalsIgnoreCase(SECRET_WORD)) {
            session.setAttribute(GAME_WON_KEY, true);
            return ResponseEntity.ok("Correct! The word was: " + SECRET_WORD);
        }

        String hint = generateHint(guess);
        session.setAttribute(LAST_HINT_KEY, hint);

        int attempts = updateAttempts(session);
        return buildResponse(attempts, hint);
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

    public String generateHint(String guess) {
        StringBuilder hint = new StringBuilder();
        String upperGuess = guess.toUpperCase();
        for (int i = 0; i < WORD_LENGTH; i++) {
            hint.append(upperGuess.charAt(i) == SECRET_WORD.charAt(i) ? upperGuess.charAt(i) + " " : "_ ");
        }
        return hint.toString().trim();
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

    public void resetGame(HttpSession session) {
        session.removeAttribute(ATTEMPTS_KEY);
        session.removeAttribute(GAME_WON_KEY);
        session.removeAttribute(LAST_HINT_KEY);
    }
}
