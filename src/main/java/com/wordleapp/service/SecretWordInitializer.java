package com.wordleapp.service;

import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.SecretWordRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SecretWordInitializer {

    private final SecretWordRepository secretWordRepository;

    public SecretWordInitializer(SecretWordRepository secretWordRepository) {
        this.secretWordRepository = secretWordRepository;
    }

    @PostConstruct
    public void initWordsFromFile() {
        try (BufferedReader reader = getBufferedReaderForResource()) {
            reader.lines()
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .filter(word -> word.length() == 5)
                    .distinct()
                    .forEach(word -> {
                        if (!secretWordRepository.existsByWord(word)) {
                            secretWordRepository.save(new SecretWord(word));
                            log.info("✅ Inserted word: {}", word);
                        } else {
                            log.info("ℹ️ Word already exists: {}", word);
                        }
                    });
        } catch (IOException e) {
            throw new IllegalStateException("Error reading word list", e);
        }
    }

    // Visible for testing
    public BufferedReader getBufferedReaderForResource() {
        InputStreamReader inputStreamReader = new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("words.txt"))
        );
        return new BufferedReader(inputStreamReader);
    }
}
