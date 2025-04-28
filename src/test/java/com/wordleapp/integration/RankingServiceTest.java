package com.wordleapp.integration;

import com.wordleapp.model.Game;
import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.GameRepository;
import com.wordleapp.repository.SecretWordRepository;
import com.wordleapp.service.RankingService;
import com.wordleapp.testsupport.BaseTestConfiguration;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Transactional
class RankingServiceTest extends BaseTestConfiguration {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SecretWordRepository secretWordRepository;

    @Autowired
    private RankingService rankingService;

    @BeforeEach
    void setUp() {
        SecretWord word = new SecretWord("SEXTO");
        secretWordRepository.save(word);

        gameRepository.save(new Game("Ana", 2, word, LocalDateTime.now()));
        gameRepository.save(new Game("Luis", 1, word, LocalDateTime.now()));
        gameRepository.save(new Game("Eva", 3, word, LocalDateTime.now()));
        gameRepository.save(new Game("Javi", 1, word, LocalDateTime.now())); // draw with Luis
    }

    @Test
    void shouldReturnTop3IncludingTies() {
        List<Game> rankings = rankingService.findTopRankingsBySecretWord("SEXTO", 3);

        assertEquals(3, rankings.size()); // Luis, Javi, Ana
        assertEquals("Luis", rankings.get(0).getUsername());
        assertEquals("Javi", rankings.get(1).getUsername());
        assertEquals("Ana", rankings.get(2).getUsername());
    }

    @Test
    void shouldReturnEmptyListForUnknownWord() {
        List<Game> rankings = rankingService.findTopRankingsBySecretWord("ZZZZZ", 3);
        assertTrue(rankings.isEmpty());
    }
}

