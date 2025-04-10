package com.wordleapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import jakarta.annotation.PostConstruct;

@SuppressWarnings("java:S2245")
@Service
public class WordSelectorService {

    private static final Logger logger = LoggerFactory.getLogger(WordSelectorService.class);
    private static final String WORDS_FILE_PATH = "src/main/resources/words.txt";
    private List<String> words;

    @Getter
    private String currentWord;

    @PostConstruct
    private void loadWords() {
        Path path = Paths.get(WORDS_FILE_PATH);

        if (Files.notExists(path)) {
            logger.error("Words file does not exist at {}", WORDS_FILE_PATH);
            throw new IllegalStateException("Words file not found");
        }

        try {
            words = Files.readAllLines(path);
            selectNewWord();
        } catch (IOException e) {
            logger.error("Failed to load words file from {}", WORDS_FILE_PATH, e);
            throw new IllegalStateException("Failed to load words file", e);
        }
    }

    public void selectNewWord() {
        if (words.isEmpty()) {
            throw new IllegalStateException("No words available in the file.");
        }
        currentWord = words.get(ThreadLocalRandom.current().nextInt(words.size())).toUpperCase();
    }

}
