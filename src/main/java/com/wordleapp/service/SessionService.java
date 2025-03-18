package com.wordleapp.service;

import com.wordleapp.util.Constants;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    public void resetGame(HttpSession session) {
        session.removeAttribute(Constants.ATTEMPTS_KEY);
        session.removeAttribute(Constants.GAME_WON_KEY);
        session.removeAttribute(Constants.LAST_HINT_KEY);
    }
}
