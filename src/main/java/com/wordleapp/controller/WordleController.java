package com.wordleapp.controller;

import com.wordleapp.service.WordleGameService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import static com.wordleapp.service.WordleGameService.LAST_HINT_KEY;

@RestController
@RequestMapping("/api/wordle")
public class WordleController {

    private final WordleGameService wordleGameService;

    public WordleController(WordleGameService wordleGameService) {
        this.wordleGameService = wordleGameService;
    }

    @PostMapping("/guess")
    public ResponseEntity<String> checkWord(@RequestParam String guess, HttpSession session) {
        return wordleGameService.checkWord(guess, session);
    }

    @PostMapping("/guessWithHint")
    public ResponseEntity<Map<String, String>> checkWordWithHint(@RequestParam String guess, HttpSession session) {
        try {
            // We call checkWord to validate the attempt and handle error states
            wordleGameService.checkWord(guess, session);

            // If the validation is successful, we generate the hint
            String hint = wordleGameService.generateHint(guess, session);

            // We build the answer
            Map<String, String> response = new HashMap<>();
            response.put("hint", hint);

            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            // We obtain the last hint stored in the session (if it exists).
            String lastHint = (String) session.getAttribute(LAST_HINT_KEY);

            // We construct the response with the error and the last available hint
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getReason());
            if (lastHint != null) {
                errorResponse.put("hint", lastHint);
            }

            return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
        }
    }


    @PostMapping("/reset")
    public ResponseEntity<String> resetGame(HttpSession session) {
        wordleGameService.resetGame(session);
        return ResponseEntity.ok("Game reset! You have 6 attempts.");
    }
}
