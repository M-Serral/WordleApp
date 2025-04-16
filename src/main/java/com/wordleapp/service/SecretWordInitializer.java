package com.wordleapp.service;

import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.SecretWordRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

@Slf4j
@Service
@Profile({"local", "docker"})
public class SecretWordInitializer {

    private final SecretWordRepository secretWordRepository;

    public SecretWordInitializer(SecretWordRepository secretWordRepository) {
        this.secretWordRepository = secretWordRepository;
    }

    @PostConstruct
    public void initSecretWordsFromFile() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("words.txt"))
        ))) {
            reader.lines()
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .filter(word -> word.length() == 5)
                    .distinct()
                    .forEach(word -> {
                        if (!secretWordRepository.existsByWord(word)) {
                            secretWordRepository.save(new SecretWord(word));
                            log.info("✅ Secret word inserted: {}", word);
                        } else {
                            log.info("ℹ️ Secret word already exists: {}", word);
                        }
                    });
        } catch (IOException e) {
            throw new IllegalStateException("❌ Error loading words.txt", e);
        }
    }
}
