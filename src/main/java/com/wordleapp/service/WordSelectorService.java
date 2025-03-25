package com.wordleapp.service;

import com.wordleapp.exception.WordLoadingException;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.List;

import jakarta.annotation.PostConstruct;

@Service
public class WordSelectorService {
    private static final String WORDS_FILE_PATH = "src/main/resources/words.txt";
    private List<String> words;
    @Getter
    private String currentWord;
    private final SecureRandom random = new SecureRandom();

    @PostConstruct
    private void loadWords() {
        try {
            Path path = Paths.get(WORDS_FILE_PATH);
            words = Files.readAllLines(path);
            selectNewWord();
        } catch (IOException e) {
            throw new WordLoadingException("Failed to load words file", e);
        }
    }

    public void selectNewWord() {
        if (words.isEmpty()) {
            throw new IllegalStateException("No words available in the file.");
        }
        currentWord = words.get(random.nextInt(words.size())).toUpperCase();
    }

}
