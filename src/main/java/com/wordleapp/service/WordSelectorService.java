package com.wordleapp.service;

import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.SecretWordRepository;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class WordSelectorService {

    private final SecretWordRepository secretWordRepository;
    private String currentWord;
    private static final Logger logger = LoggerFactory.getLogger(WordSelectorService.class);

    public WordSelectorService(SecretWordRepository secretWordRepository) {
        this.secretWordRepository = secretWordRepository;
    }

    public void selectRandomWord() {
        long total = secretWordRepository.count();
        if (total == 0) {
            throw new IllegalStateException("No words available in the database.");
        }

        long randomId = ThreadLocalRandom.current().nextLong(1, total + 1);
        this.currentWord = secretWordRepository.findById(randomId)
                .map(SecretWord::getWord)
                .orElseThrow(() -> new IllegalStateException("Random word not found."));
    }

    @PostConstruct
    public void init() {
        logger.debug("🌱 Selecting random word on startup...");
        selectRandomWord();
    }


    public String getCurrentWord() {
        if (currentWord == null) {
            throw new IllegalStateException("No word selected. Use selectNewWord() first.");
        }
        return currentWord;
    }

}
