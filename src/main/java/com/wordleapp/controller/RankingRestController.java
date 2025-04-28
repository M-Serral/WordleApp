
package com.wordleapp.controller;

import com.wordleapp.model.AvailableWord;
import com.wordleapp.repository.AvailableWordRepository;
import com.wordleapp.repository.SecretWordRepository;
import com.wordleapp.service.RankingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wordle")
public class RankingRestController {

    private final SecretWordRepository secretWordRepository;
    private final RankingService rankingService;


    public RankingRestController(SecretWordRepository secretWordRepository, RankingService rankingService) {
        this.secretWordRepository = secretWordRepository;
        this.rankingService = rankingService;
    }

    @GetMapping("/ranking")
    public List<Map<String, Object>> getRanking(@RequestParam String word, @RequestParam int top) {
        return rankingService.findTopRankingsBySecretWord(word, top)
                .stream()
                .map(game -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("username", game.getUsername());
                    if (game.getSecretWord() != null) {
                        map.put("word", game.getSecretWord().getWord());
                    } else {
                        map.put("word", "undefined");
                    }
                    if (game.getCreatedAt() != null) {
                        map.put("date", game.getCreatedAt());
                    } else {
                        map.put("date", "Invalid Date");
                    }
                    map.put("attempts", game.getAttempts());
                    return map;
                })
                .toList();
    }

    @GetMapping("/secret-words")
    public List<String> getAvailableSecretWords() {
        return secretWordRepository.findAll()
                .stream()
                .map(secretWord -> secretWord.getWord())
                .toList();
    }

}