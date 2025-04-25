package com.wordleapp.service;

import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.SecretWordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WordSelectorService {

    private final SecretWordRepository secretWordRepository;
    private SecretWord currentSecretWord;

    public WordSelectorService(SecretWordRepository secretWordRepository) {
        this.secretWordRepository = secretWordRepository;
    }

    public void selectRandomWord() {
        this.currentSecretWord = secretWordRepository.findRandomWord()
                .orElseThrow(() -> new IllegalStateException("Random word not found."));
        log.info("🕵️  [DEBUG MODE] The secret word for this session is: {}", currentSecretWord.getWord());
    }

    public String getCurrentWord() {
        if (currentSecretWord == null) {
            throw new IllegalStateException("No word selected. Use selectNewWord() first.");
        }
        return currentSecretWord.getWord();
    }

    public SecretWord getCurrentSecretWord() {
        if (currentSecretWord == null) throw new IllegalStateException("No word selected.");
        return currentSecretWord;
    }

}