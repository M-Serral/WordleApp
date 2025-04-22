package com.wordleapp.controller;

import com.wordleapp.service.SessionGameService;
import com.wordleapp.service.WordleGameService;
import com.wordleapp.utils.Constants;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


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
        ensureSessionId(session);
        return wordleGameService.checkWord(guess, session);
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetGame(@RequestParam String username, HttpSession session) {
        session.setAttribute(Constants.USERNAME_KEY, username);
        ensureSessionId(session);
        sessionGameService.resetGame(username, session);
        return ResponseEntity.ok("Game reset! You have 6 attempts.");
    }
    @GetMapping("/session-id")
    public ResponseEntity<String> getSessionId(HttpSession session) {
        Object sessionId = session.getAttribute(Constants.SESSION_ID);
        return ResponseEntity.ok(sessionId != null ? sessionId.toString() : "");
    }

    @GetMapping("/init")
    public ResponseEntity<Void> initializeSession(HttpSession session) {
        ensureSessionId(session);
        return ResponseEntity.ok().build();
    }


    private void ensureSessionId(HttpSession session) {
        if (session.getAttribute(Constants.SESSION_ID) == null) {
            session.setAttribute(Constants.SESSION_ID, UUID.randomUUID().toString());
        }
    }
}
