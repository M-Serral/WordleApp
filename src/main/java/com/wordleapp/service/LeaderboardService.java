package com.wordleapp.service;

import com.wordleapp.dto.LeaderboardEntry;
import com.wordleapp.model.Game;
import com.wordleapp.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LeaderboardService {

    private final GameRepository gameRepository;

    public LeaderboardService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<LeaderboardEntry> getLeaderboard(String orderBy) {
        Comparator<Game> comparator = Comparator.comparing(Game::getCreatedAt).reversed();

        if ("attempts".equalsIgnoreCase(orderBy)) {
            comparator = Comparator.comparingInt(Game::getAttempts);
        }

        return gameRepository.findAll().stream()
                .sorted(comparator)
                .map(game -> new LeaderboardEntry(
                        game.getUsername(),
                        game.getSecretWord().getWord(),
                        game.getAttempts(),
                        game.getCreatedAt()
                ))
                .toList();
    }
}
