package com.wordleapp.service;

import com.wordleapp.model.Game;
import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.GameRepository;
import com.wordleapp.repository.SecretWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final GameRepository gameRepository;
    private final SecretWordRepository secretWordRepository;

    public List<Game> findTopRankingsBySecretWord(String word, int topN) {
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("The word must not be empty");
        }

        Optional<SecretWord> secretOpt = secretWordRepository.findByWord(word.toUpperCase());
        if (secretOpt.isEmpty()) {
            return List.of(); // Word not found
        }

        List<Game> allGames = gameRepository.findAll();
        List<Game> matchingGames = new ArrayList<>();

        for (Game game : allGames) {
            if (game.getSecretWord() != null && game.getSecretWord().getWord().equalsIgnoreCase(word)) {
                matchingGames.add(game);
            }
        }

        for (int i = 0; i < matchingGames.size(); i++) {
            for (int j = i + 1; j < matchingGames.size(); j++) {
                if (matchingGames.get(i).getAttempts() > matchingGames.get(j).getAttempts()) {
                    Game temp = matchingGames.get(i);
                    matchingGames.set(i, matchingGames.get(j));
                    matchingGames.set(j, temp);
                }
            }
        }

        List<Game> result = new ArrayList<>();
        if (!matchingGames.isEmpty()) {
            if (topN >= matchingGames.size()) {
                result.addAll(matchingGames);
            } else {
                int attemptsLimit = matchingGames.get(topN - 1).getAttempts();
                for (Game game : matchingGames) {
                    if (game.getAttempts() <= attemptsLimit) {
                        result.add(game);
                    }
                }
            }
        }
        return result;
    }
}
