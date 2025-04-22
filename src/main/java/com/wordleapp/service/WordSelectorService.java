package com.wordleapp.service;

import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.SecretWordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WordSelectorService {

    private final SecretWordRepository secretWordRepository;
    private String currentWord;

    public WordSelectorService(SecretWordRepository secretWordRepository) {
        this.secretWordRepository = secretWordRepository;
    }

    public void selectRandomWord() {
        SecretWord randomWord = secretWordRepository.findRandomWord()
                .orElseThrow(() -> new IllegalStateException("Random word not found."));
        this.currentWord = randomWord.getWord();

        // to help debugging via front
        log.info("🕵️  [DEBUG MODE] The secret word for this session is: {}", currentWord);


    }


    public String getCurrentWord() {
        if (currentWord == null) {
            throw new IllegalStateException("No word selected. Use selectNewWord() first.");
        }
        return currentWord;
    }

}
