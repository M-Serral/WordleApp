package com.wordleapp.test;

import com.wordleapp.model.Game;
import com.wordleapp.repository.GameRepository;
import com.wordleapp.repository.SecretWordRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TestDataLoader {

    private final GameRepository gameRepository;
    private final SecretWordRepository secretWordRepository;

    public TestDataLoader(GameRepository gameRepository, SecretWordRepository secretWordRepository) {
        this.gameRepository = gameRepository;
        this.secretWordRepository = secretWordRepository;
    }

    public void insertTestGames() {
        String secretWordText = "LIBRO";

        secretWordRepository.findByWord(secretWordText).ifPresentOrElse(secretWord -> {
            gameRepository.save(new Game("User1", 2, secretWord, LocalDateTime.now()));
            gameRepository.save(new Game("User2", 3, secretWord, LocalDateTime.now().minusMinutes(10)));
            gameRepository.save(new Game("User3", 1, secretWord, LocalDateTime.now().minusMinutes(20)));
            gameRepository.save(new Game("User4", 2, secretWord, LocalDateTime.now().minusMinutes(30)));
            gameRepository.save(new Game("User5", 4, secretWord, LocalDateTime.now().minusMinutes(40)));
            gameRepository.save(new Game("User5", 5, secretWord, LocalDateTime.now().minusMinutes(40)));

            System.out.println("✅ Test games for LIBRO inserted successfully!");
        }, () -> {
            System.err.println("❌ Secret word 'LIBRO' not found. No test games inserted.");
        });
    }
}
