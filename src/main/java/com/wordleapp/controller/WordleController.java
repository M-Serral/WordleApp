package com.wordleapp.controller;

import com.wordleapp.service.WordleGameService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/wordle")
public class WordleController {

    private final WordleGameService wordleGameService;

    public WordleController(WordleGameService wordleGameService) {
        this.wordleGameService = wordleGameService;
    }

    @PostMapping("/guess")
    public ResponseEntity<String> checkWord(@RequestParam String guess, HttpSession session) {
        return wordleGameService.checkWord(guess,session);
    }

    @PostMapping("/guessWithHint")
    public ResponseEntity<Map<String, String>> checkWordWithHint(@RequestParam String guess) {
        String hint = wordleGameService.checkGuess(guess);
        Map<String, String> response = new HashMap<>();
        response.put("hint", hint);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetGame(HttpSession session) {
        wordleGameService.resetGame(session);
        return ResponseEntity.ok("Game reset! You have 6 attempts.");
    }
}
