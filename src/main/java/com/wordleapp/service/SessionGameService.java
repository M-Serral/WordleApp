package com.wordleapp.service;

import com.wordleapp.utils.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SessionGameService {

    private final WordSelectorService wordSelectorService;

    public SessionGameService(WordSelectorService wordSelectorService) {
        this.wordSelectorService = wordSelectorService;
    }

    public void resetGame(HttpSession session) {
        session.removeAttribute(Constants.ATTEMPTS_KEY);
        session.removeAttribute(Constants.GAME_WON_KEY);
        session.removeAttribute(Constants.LAST_HINT_KEY);
        wordSelectorService.selectNewWord();
    }
}
