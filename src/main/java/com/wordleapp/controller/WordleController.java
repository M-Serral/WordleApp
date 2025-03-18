package com.wordleapp.controller;

import com.wordleapp.service.SessionGameService;
import com.wordleapp.service.WordleGameService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/wordle")
public class WordleController {

    private final WordleGameService wordleGameService;
    private final SessionGameService sessionGameService;

    public WordleController(WordleGameService wordleGameService, SessionGameService sessionGameService) {
        this.wordleGameService = wordleGameService;
        this.sessionGameService = sessionGameService;
    }

    @PostMapping("/guess")
    public ResponseEntity<String> checkWord(@RequestParam String guess, HttpSession session) {
        return wordleGameService.checkWord(guess, session);
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetGame(HttpSession session) {
        sessionGameService.resetGame(session);
        return ResponseEntity.ok("Game reset! You have 6 attempts.");
    }
}
