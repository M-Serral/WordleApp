package com.wordleapp.service;

import com.wordleapp.utils.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessionGameService {

    private final WordSelectorService wordSelectorService;

    public SessionGameService(WordSelectorService wordSelectorService) {
        this.wordSelectorService = wordSelectorService;
    }

    public void resetGame(String username, HttpSession session) {
        session.removeAttribute(Constants.ATTEMPTS_KEY);
        session.removeAttribute(Constants.GAME_WON_KEY);
        session.removeAttribute(Constants.LAST_HINT_KEY);
        wordSelectorService.selectRandomWord();
        session.setAttribute(Constants.SECRET_WORD_KEY, wordSelectorService.getCurrentWord());
        session.setAttribute(Constants.USERNAME_KEY, username);
        // Generate new server session identifier
        if (session.getAttribute(Constants.SESSION_ID) == null) {
            session.setAttribute(Constants.SESSION_ID, UUID.randomUUID().toString());
        }
    }
}
