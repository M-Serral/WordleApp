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
    private static final int MAX_LONG = 5;


    public ResponseEntity<String> checkWord(String guess, HttpSession session) {

        if (isGameOver(session)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Game over! You've already won.");
        }

        int attempts = getAttempts(session);

        if (attempts >= MAX_ATTEMPTS) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "You have reached the maximum number of attempts.");        }

        if (guess.equalsIgnoreCase(SECRET_WORD)) {
            session.setAttribute(GAME_WON_KEY, true);
            return ResponseEntity.ok("Correct! The word was: " + SECRET_WORD);
        }

        // Obtener la última pista almacenada en la sesión (si existe)
        String lastHint = (String) session.getAttribute("lastHint");

        try {
            validateGuess(guess);  // Validar el intento
        } catch (ResponseStatusException e) {
            // Si la palabra es inválida, devolver la última pista existente
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    e.getReason() + (lastHint != null ? " Hint: " + lastHint : ""));
        }

        String hint = checkGuess(guess);
        session.setAttribute("lastHint", hint); // Guardar la pista en la sesión

        attempts = updateAttempts(session, attempts);

        return (attempts == MAX_ATTEMPTS)
                ? ResponseEntity.ok("Game over! You've used all attempts. Hint: " + hint)
                : ResponseEntity.ok("Try again! Attempts left: " + (MAX_ATTEMPTS - attempts) + ". Hint: " + hint);
    }

    public String checkGuess(String guess) {
        StringBuilder hint = new StringBuilder();
        String upperGuess = guess.toUpperCase();
        String upperSecret = SECRET_WORD.toUpperCase();
        for (int i = 0; i < 5; i++) {
            if (upperGuess.charAt(i) == upperSecret.charAt(i)) {
                hint.append(upperGuess.charAt(i)).append(" ");
            } else {
                hint.append("_ ");
            }
        }
        return hint.toString().trim();
    }

    public void resetGame(HttpSession session) {
        session.removeAttribute(ATTEMPTS_KEY);
        session.removeAttribute(GAME_WON_KEY);
    }

    private void validateGuess(String guess) {
        if (!guess.matches("[A-Za-zñÑ]+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: Only characters from the alphabet are allowed.");
        }

        else if (guess.length() != MAX_LONG) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: The word must be 5 letters long.");
        }
    }

    private int getAttempts(HttpSession session) {
        return (session.getAttribute(ATTEMPTS_KEY) != null) ? (Integer) session.getAttribute(ATTEMPTS_KEY) : 0;
    }

    private boolean isGameOver(HttpSession session) {
        Boolean gameWon = (Boolean) session.getAttribute(GAME_WON_KEY);
        return gameWon != null && gameWon;
    }

    private int updateAttempts(HttpSession session, int attempts) {
        int newAttempts = attempts + 1;
        session.setAttribute(ATTEMPTS_KEY, newAttempts);
        return newAttempts;
    }
}
