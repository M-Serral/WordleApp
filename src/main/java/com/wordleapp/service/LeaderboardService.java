package com.wordleapp.service;

import com.wordleapp.dto.LeaderboardEntry;
import com.wordleapp.model.Game;
import com.wordleapp.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardService {

    private final GameRepository gameRepository;

    public LeaderboardService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<LeaderboardEntry> getLeaderboard(String orderBy) {
        List<Game> games;

        if ("attempts".equalsIgnoreCase(orderBy)) {
            games = gameRepository.findAllByOrderByAttemptsAsc();
        } else {
            games = gameRepository.findAllByOrderByCreatedAtDesc();
        }

        return games.stream()
                .map(game -> new LeaderboardEntry(
                        game.getUsername(),
                        game.getSecretWord().getWord(),
                        game.getAttempts(),
                        game.getCreatedAt()
                ))
                .toList();
    }

}
