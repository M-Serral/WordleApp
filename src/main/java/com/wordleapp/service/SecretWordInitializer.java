package com.wordleapp.service;

import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.SecretWordRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecretWordInitializer {

    private final SecretWordRepository secretWordRepository;

    public SecretWordInitializer(SecretWordRepository secretWordRepository) {
        this.secretWordRepository = secretWordRepository;
    }

    @PostConstruct
    public void initSecretWords() {
        if (secretWordRepository.count() == 0) {
            List<String> initialWords = List.of(
                    "APPLE", "BREAD", "CLEAN", "DRIVE", "EARTH", "FINAL", "GRAPE", "HONEY", "INDEX", "JAZZY"
            );

            initialWords.forEach(word -> secretWordRepository.save(new SecretWord(word)));
        }
    }
}
