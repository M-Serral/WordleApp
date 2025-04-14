package com.wordleapp.service;

import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.SecretWordRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;


@Service
@DependsOn("secretWordInitializer")
public class WordSelectorService {

    private final SecretWordRepository secretWordRepository;
    private String currentWord;

    public WordSelectorService(SecretWordRepository secretWordRepository) {
        this.secretWordRepository = secretWordRepository;
    }

    @PostConstruct
    public void selectRandomWord() {
        SecretWord randomWord = secretWordRepository.findRandomWord()
                .orElseThrow(() -> new IllegalStateException("Random word not found."));
        this.currentWord = randomWord.getWord();
    }


    public String getCurrentWord() {
        if (currentWord == null) {
            throw new IllegalStateException("No word selected. Use selectNewWord() first.");
        }
        return currentWord;
    }

}
