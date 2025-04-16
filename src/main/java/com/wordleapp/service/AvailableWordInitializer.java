package com.wordleapp.service;

import com.wordleapp.model.AvailableWord;
import com.wordleapp.repository.AvailableWordRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Component
@Profile({"local", "docker"}) // Only loaded in production or development
@Slf4j
@Service
public class AvailableWordInitializer {

    private final AvailableWordRepository availableWordRepository;

    private final WordSelectorService wordSelectorService;

    public AvailableWordInitializer(AvailableWordRepository availableWordRepository,
                                    WordSelectorService wordSelectorService) {
        this.availableWordRepository = availableWordRepository;
        this.wordSelectorService = wordSelectorService;
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
                        if (!availableWordRepository.existsByWord(word)) {
                            availableWordRepository.save(new AvailableWord(word));
                            log.info("✅ Inserted available word: {}", word);
                        } else {
                            log.info("ℹ️ Word already exists in dictionary: {}", word);
                        }
                    });
        } catch (IOException e) {
            throw new IllegalStateException("Error reading dictionary.txt", e);
        }
        wordSelectorService.selectRandomWord();
    }

    // Visible for testing
    public BufferedReader getBufferedReaderForResource() {
        InputStreamReader inputStreamReader = new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("dictionary.txt"))
        );
        return new BufferedReader(inputStreamReader);
    }
}
