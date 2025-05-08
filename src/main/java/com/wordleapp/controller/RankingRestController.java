
package com.wordleapp.controller;

import com.wordleapp.model.Game;
import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.SecretWordRepository;
import com.wordleapp.service.RankingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<Game> getRanking(@RequestParam String word, @RequestParam int top) {
        return rankingService.findTopRankingsBySecretWord(word, top);
    }

    @GetMapping("/secret-words")
    public List<String> getAvailableSecretWords() {
        return secretWordRepository.findAll()
                .stream()
                .map(SecretWord::getWord)
                .toList();
    }

}