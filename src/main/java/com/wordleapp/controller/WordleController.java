package com.wordleapp.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wordle")
public class WordleController {

    private static final String SECRET_WORD = "APPLE"; // Hardcoded for now

    @PostMapping("/guess")
    public String checkWord(@RequestParam String guess) {
        if (guess.length() != 5) {
            return "Invalid input. The word must be 5 letters long."; // Ensure correct message
        }
        return guess.equalsIgnoreCase(SECRET_WORD) ? "Correct!" : "Try again!";
    }
}

