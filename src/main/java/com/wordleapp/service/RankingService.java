package com.wordleapp.service;

import com.wordleapp.model.Game;
import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.GameRepository;
import com.wordleapp.repository.SecretWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final GameRepository gameRepository;
    private final SecretWordRepository secretWordRepository;

    public List<Game> findTopRankingsBySecretWord(String word, int topN) {
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("The word must not be empty");
        }

        SecretWord secretWord = secretWordRepository.findByWord(word.toUpperCase())
                .orElse(null);

        if (secretWord == null) return List.of();

        List<Game> sortedGames = gameRepository.findBySecretWordOrderByAttempts(secretWord);

        if (sortedGames.isEmpty() || topN >= sortedGames.size()) return sortedGames;

        int cutoff = sortedGames.get(topN - 1).getAttempts();
        return sortedGames.stream()
                // avoids further scanning of the list after the limit of
                // attempts has been exceeded (since the list is sorted).
                .takeWhile(g -> g.getAttempts() <= cutoff)
                .toList();
    }
}
