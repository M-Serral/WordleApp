package com.wordleapp.test;

import com.wordleapp.model.Game;
import com.wordleapp.repository.GameRepository;
import com.wordleapp.repository.SecretWordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Utility component to insert test items in a controlled manner.
 * <p>
 * ⚠️ Only active when the Spring profile is “local”.
 * <p>
 * Used during development to populate the database with dummy games,
 * Facilitating the validation of filters, rankings and display in the user interface.
 */

@Profile("local")
@Slf4j
@Component
public class TestDataLoader {

    private final GameRepository gameRepository;
    private final SecretWordRepository secretWordRepository;

    public TestDataLoader(GameRepository gameRepository, SecretWordRepository secretWordRepository) {
        this.gameRepository = gameRepository;
        this.secretWordRepository = secretWordRepository;
    }

    /**
     * Insert test items associated with the secret word “BOOK”.
     * This method should only be used during functional validations.
     */

    public void insertTestGames() {
        String secretWordText = "LIBRO";

        secretWordRepository.findByWord(secretWordText).ifPresentOrElse(secretWord -> {
            gameRepository.save(new Game("User1", 1, secretWord, LocalDateTime.now()));
            gameRepository.save(new Game("User2", 3, secretWord, LocalDateTime.now().minusMinutes(10)));
            gameRepository.save(new Game("User3", 1, secretWord, LocalDateTime.now().minusMinutes(20)));
            gameRepository.save(new Game("User4", 2, secretWord, LocalDateTime.now().minusMinutes(30)));
            gameRepository.save(new Game("User5", 4, secretWord, LocalDateTime.now().minusMinutes(40)));
            gameRepository.save(new Game("User6", 2, secretWord, LocalDateTime.now().minusMinutes(50)));
            gameRepository.save(new Game("User7", 5, secretWord, LocalDateTime.now().minusMinutes(60)));
            gameRepository.save(new Game("User8", 3, secretWord, LocalDateTime.now().minusMinutes(70)));


            log.info("✅ Test games for LIBRO inserted successfully!");
        }, () -> log.error("❌ Secret word 'LIBRO' not found. No test games inserted."));
    }
}
