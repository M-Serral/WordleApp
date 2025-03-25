package com.wordleapp.service;

import com.wordleapp.utils.Constants;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Service
public class WordleGameService {

    private final WordSelectorService wordSelectorService;


    public WordleGameService(WordSelectorService wordSelectorService) {
        this.wordSelectorService = wordSelectorService;
    }


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

        String upperGuess = guess.toUpperCase();
        String hint = generateHint(upperGuess, session);
        session.setAttribute(Constants.LAST_HINT_KEY, hint);

        if (upperGuess.equals(wordSelectorService.getCurrentWord())) {
            session.setAttribute(Constants.GAME_WON_KEY, true);
            return ResponseEntity.ok("CORRECT! The secret word was: " + wordSelectorService.getCurrentWord()
                    + Constants.HINT + guess + Constants.ARROW + hint);
        }

        int attempts = updateAttempts(session);

        return buildResponse(attempts, hint, upperGuess);

    }

    private void validateGameState(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute(Constants.GAME_WON_KEY))) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "GAME OVER! You've already won.");
        }

        if (getAttempts(session) >= Constants.MAX_ATTEMPTS) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "You have reached the maximum number of attempts.");
        }
    }

    private void validateGuess(String guess) {
        if (!guess.matches("^[A-Za-zñÑ]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid input: Only characters from the alphabet are allowed.");
        }
        if (guess.length() != Constants.WORD_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid input: The word must be " + Constants.WORD_LENGTH + " letters long.");
        }
    }

    public String generateHint(String upperGuess, HttpSession session) {
        char[] hint = initializeHint();
        boolean[] matched = new boolean[Constants.WORD_LENGTH];
        boolean[] used = new boolean[Constants.WORD_LENGTH];

        markCorrectPositions(upperGuess, hint, matched, used);
        markMisplacedLetters(upperGuess, hint, matched, used);

        session.setAttribute(Constants.HINT_SESSION_KEY, hint);
        return formatHint(hint);
    }

    private char[] initializeHint() {
        char[] hint = new char[Constants.WORD_LENGTH];
        Arrays.fill(hint, '_');
        return hint;
    }

    private void markCorrectPositions(String guess, char[] hint, boolean[] matched, boolean[] used) {
        for (int i = 0; i < Constants.WORD_LENGTH; i++) {
            if (guess.charAt(i) == wordSelectorService.getCurrentWord().charAt(i)) {
                hint[i] = guess.charAt(i);
                matched[i] = true;
                used[i] = true;
            }
        }
    }

    private void markMisplacedLetters(String guess, char[] hint, boolean[] matched, boolean[] used) {
        for (int i = 0; i < Constants.WORD_LENGTH; i++) {
            if (hint[i] == '_') {
                for (int j = 0; j < Constants.WORD_LENGTH; j++) {
                    if (!matched[j] && !used[j] && guess.charAt(i) == wordSelectorService.getCurrentWord().charAt(j)) {
                        hint[i] = '?';
                        used[j] = true;
                        break;
                    }
                }
            }
        }
    }

    private String formatHint(char[] hint) {
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

    private ResponseEntity<String> buildResponse(int attempts, String hint, String guess) {
        return ResponseEntity.ok(
                attempts == Constants.MAX_ATTEMPTS
                        ? "GAME OVER! The secret word was: " + wordSelectorService.getCurrentWord()
                        + Constants.HINT  + guess + Constants.ARROW + hint

                        : "Try again! Attempts left: " + (Constants.MAX_ATTEMPTS - attempts)
                        + Constants.HINT  + guess + Constants.ARROW + hint
        );
    }
}
