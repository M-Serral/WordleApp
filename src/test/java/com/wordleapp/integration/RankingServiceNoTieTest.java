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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Transactional
class RankingServiceNoTieTest extends BaseTestConfiguration {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SecretWordRepository secretWordRepository;

    @Autowired
    private RankingService rankingService;

    @BeforeEach
    void setUp() {
        SecretWord secretWord = new SecretWord("CLARO");
        secretWordRepository.save(secretWord);

        gameRepository.save(new Game("A", 1, secretWord, LocalDateTime.now()));
        gameRepository.save(new Game("B", 2, secretWord, LocalDateTime.now().minusMinutes(10)));
        gameRepository.save(new Game("C", 2, secretWord, LocalDateTime.now().minusMinutes(20)));
        gameRepository.save(new Game("D", 4, secretWord, LocalDateTime.now().minusMinutes(30)));
        gameRepository.save(new Game("F", 4, secretWord, LocalDateTime.now().minusMinutes(50)));
        gameRepository.save(new Game("E", 5, secretWord, LocalDateTime.now().minusMinutes(40)));
    }

    @Test
    void shouldReturnExactTop1WhenNoTies() {
        List<Game> top1 = rankingService.findTopRankingsBySecretWord("CLARO", 1);

        assertEquals(1, top1.size());
        assertEquals("A", top1.get(0).getUsername());
    }

    @Test
    void shouldReturnExactTop3WhenNoTies() {
        List<Game> top3 = rankingService.findTopRankingsBySecretWord("CLARO", 3);

        assertEquals(3, top3.size());
        assertEquals("A", top3.get(0).getUsername());
        assertEquals("B", top3.get(1).getUsername());
        assertEquals("C", top3.get(2).getUsername());
    }

    @Test
    void shouldReturnExactTop5WhenNoTies() {
        List<Game> top5 = rankingService.findTopRankingsBySecretWord("CLARO", 5);

        assertEquals(5, top5.size());
        assertEquals("A", top5.get(0).getUsername());
        assertEquals("B", top5.get(1).getUsername());
        assertEquals("C", top5.get(2).getUsername());
        assertEquals("D", top5.get(3).getUsername());
        assertEquals("F", top5.get(4).getUsername());
    }
}

