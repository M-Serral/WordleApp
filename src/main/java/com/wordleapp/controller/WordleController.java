package com.wordleapp.controller;

import com.wordleapp.service.WordleGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wordle")
public class WordleController {

    private final WordleGameService wordleGameService;

    public WordleController(WordleGameService wordleGameService) {
        this.wordleGameService = wordleGameService;
    }

    @PostMapping("/guess")
    public ResponseEntity<String> checkWord(@RequestParam String guess, @RequestParam String user) {
        return wordleGameService.checkWord(guess, user);
    }

    @PostMapping("/reset")
    public String resetGame(@RequestParam String user) {
        wordleGameService.resetGame(user);
        return "Game reset! You have 6 attempts.";
    }
}
