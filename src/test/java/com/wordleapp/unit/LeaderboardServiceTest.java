package com.wordleapp.unit;

import com.wordleapp.dto.LeaderboardEntry;
import com.wordleapp.model.Game;
import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.GameRepository;
import com.wordleapp.service.LeaderboardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LeaderboardServiceTest")
class LeaderboardServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private LeaderboardService leaderboardService;

    @Test
    @DisplayName("Should return entries ordered by date descending")
    void shouldReturnEntriesOrderedByDateDesc() {
        // given
        List<Game> games = List.of(
                createGame("Luis", "RATON", 4, LocalDateTime.of(2024, 4, 2, 12, 0)),
                createGame("Ana", "CASAS", 3, LocalDateTime.of(2024, 4, 1, 10, 0))
                );
        when(gameRepository.findAllByOrderByAttemptsAsc()).thenReturn(games);

        List<LeaderboardEntry> result = leaderboardService.getLeaderboard("attempts");

        // then
        assertEquals(2, result.size());
        assertEquals("Luis", result.get(0).username());
        assertEquals("RATON", result.get(0).word());
        assertEquals("Ana", result.get(1).username());
        assertEquals("CASAS", result.get(1).word());
    }

    @Test
    @DisplayName("Should return entries ordered by lowest number of attempts")
    void shouldReturnEntriesOrderedByAttemptsAsc() {
        // given
        List<Game> games = List.of(
                createGame("Luis", "RATON", 2, LocalDateTime.of(2024, 4, 2, 12, 0)),
                createGame("Ana", "CASAS", 3, LocalDateTime.of(2024, 4, 1, 10, 0))
        );
        when(gameRepository.findAllByOrderByAttemptsAsc()).thenReturn(games);

        // when
        List<LeaderboardEntry> result = leaderboardService.getLeaderboard("attempts");

        // then
        assertEquals(2, result.size());
        assertEquals("Luis", result.get(0).username());
        assertEquals(2, result.get(0).attempts());
        assertEquals("Ana", result.get(1).username());
        assertEquals(3, result.get(1).attempts());
    }

    private Game createGame(String user, String word, int attempts, LocalDateTime time) {
        Game g = new Game();
        g.setUsername(user);
        g.setAttempts(attempts);
        g.setCreatedAt(time);
        g.setSecretWord(new SecretWord(word));
        return g;
    }
}
