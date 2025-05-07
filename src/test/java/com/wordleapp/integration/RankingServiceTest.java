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
        SecretWord secretWord = new SecretWord("LIBRO");
        secretWordRepository.save(secretWord);

        gameRepository.save(new Game("User1", 1, secretWord, LocalDateTime.now()));
        gameRepository.save(new Game("User2", 3, secretWord, LocalDateTime.now().minusMinutes(10)));
        gameRepository.save(new Game("User3", 1, secretWord, LocalDateTime.now().minusMinutes(20)));
        gameRepository.save(new Game("User4", 2, secretWord, LocalDateTime.now().minusMinutes(30)));
        gameRepository.save(new Game("User5", 4, secretWord, LocalDateTime.now().minusMinutes(40)));
        gameRepository.save(new Game("User6", 2, secretWord, LocalDateTime.now().minusMinutes(50)));
        gameRepository.save(new Game("User7", 5, secretWord, LocalDateTime.now().minusMinutes(60)));
        gameRepository.save(new Game("User8", 3, secretWord, LocalDateTime.now().minusMinutes(70)));



    }

    @Test
    void shouldReturnTop1IncludingTies() {
        List<Game> rankings = rankingService.findTopRankingsBySecretWord("LIBRO", 1);

        assertEquals(2, rankings.size()); // tie in first position
        assertEquals("User1", rankings.get(0).getUsername());
        assertEquals("User3", rankings.get(1).getUsername());
    }

    @Test
    void shouldReturnTop3IncludingTies() {
        List<Game> rankings = rankingService.findTopRankingsBySecretWord("LIBRO", 3);

        assertEquals(4, rankings.size()); // 2 ties, in first position and in second position
        assertEquals("User1", rankings.get(0).getUsername());
        assertEquals("User3", rankings.get(1).getUsername());
        assertEquals("User4", rankings.get(2).getUsername());
        assertEquals("User6", rankings.get(3).getUsername());
    }

    @Test
    void shouldReturnTop5IncludingTies() {
        List<Game> rankings = rankingService.findTopRankingsBySecretWord("LIBRO", 5);

        assertEquals(6, rankings.size()); // 3 ties, first,second,third position
        assertEquals("User1", rankings.get(0).getUsername());
        assertEquals("User3", rankings.get(1).getUsername());
        assertEquals("User4", rankings.get(2).getUsername());
        assertEquals("User6", rankings.get(3).getUsername());
        assertEquals("User2", rankings.get(4).getUsername());
        assertEquals("User8", rankings.get(5).getUsername());
    }

    @Test
    void shouldReturnEmptyListForUnknownWord() {
        List<Game> rankings = rankingService.findTopRankingsBySecretWord("ZZZZZ", 3);
        assertTrue(rankings.isEmpty());
    }
}

