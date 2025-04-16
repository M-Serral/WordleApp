package com.wordleapp.service;

import com.wordleapp.model.AvailableWord;
import com.wordleapp.repository.AvailableWordRepository;
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
public class AvailableWordInitializer {

    private final AvailableWordRepository availableWordRepository;

    public AvailableWordInitializer(AvailableWordRepository availableWordRepository) {
        this.availableWordRepository = availableWordRepository;
    }

    @PostConstruct
    public void initWordsFromFile() {
        if (availableWordRepository.count() > 0) {
            log.info("✅ Available words already initialized. Skipping load.");
            return;
        }

        try (BufferedReader reader = createReaderFromDictionary()) {
            int inserted = insertValidWords(reader);
            log.info("✅ Inserted {} new words into available_word", inserted);
        } catch (IOException e) {
            throw new IllegalStateException("❌ Failed to load available words", e);
        }
    }

    public BufferedReader createReaderFromDictionary() {
        InputStreamReader inputStreamReader = new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("dictionary.txt"))
        );
        return new BufferedReader(inputStreamReader);
    }

    private int insertValidWords(BufferedReader reader) {
        return reader.lines()
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(word -> word.length() == 5)
                .distinct()
                .filter(word -> !availableWordRepository.existsByWord(word))
                .map(word -> {
                    try {
                        availableWordRepository.save(new AvailableWord(word));
                        return 1;
                    } catch (Exception e) {
                        log.warn("⚠️ Could not insert '{}': {}", word, e.getMessage());
                        return 0;
                    }
                })
                .mapToInt(Integer::intValue)
                .sum();
    }

}