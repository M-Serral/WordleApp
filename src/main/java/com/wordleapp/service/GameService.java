package com.wordleapp.service;

import com.wordleapp.model.Game;
import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.GameRepository;
import com.wordleapp.repository.SecretWordRepository;
import com.wordleapp.utils.Constants;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final SecretWordRepository secretWordRepository;

    public GameService(GameRepository gameRepository, SecretWordRepository secretWordRepository) {
        this.gameRepository = gameRepository;
        this.secretWordRepository = secretWordRepository;
    }

    public void saveGameIfWon(HttpSession session) {
        Boolean gameWon = (Boolean) session.getAttribute(Constants.GAME_WON_KEY);
        if (gameWon == null || !gameWon) {
            return; // Do not save if the game was not won
        }

        String username = (String) session.getAttribute(Constants.USERNAME_KEY);
        Integer attempts = (Integer) session.getAttribute(Constants.ATTEMPTS_KEY);
        String wordStr = (String) session.getAttribute(Constants.SECRET_WORD_KEY);

        if (username == null || attempts == null || wordStr == null) return;

        Optional<SecretWord> secretWordOpt = secretWordRepository.findByWord(wordStr);
        if (secretWordOpt.isEmpty()) return;

        Game game = new Game();
        game.setUsername(username);
        game.setAttempts(attempts);
        game.setSecretWord(secretWordOpt.get());

        gameRepository.save(game);
    }
}
