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
import java.util.stream.Collectors;

@Slf4j
@Service
@Profile({"local", "docker"})
public class SecretWordInitializer {

    private final SecretWordRepository secretWordRepository;
    private final WordSelectorService wordSelectorService;

    public SecretWordInitializer(SecretWordRepository secretWordRepository, WordSelectorService wordSelectorService) {
        this.secretWordRepository = secretWordRepository;
        this.wordSelectorService = wordSelectorService;
    }

    @PostConstruct
    public void initSecretWordsFromFile() {

        // we need to delete all data from table to add new secret word
        if (secretWordRepository.count() > 0) {
            log.info("✅ Secret words already loaded. Skipping load.");
        } else {
            try (BufferedReader reader = createReader()) {

                var words = reader.lines()
                        .map(String::trim)
                        .map(String::toUpperCase)
                        .filter(word -> word.length() == 5)
                        .distinct()
                        .map(SecretWord::new)
                        .toList();


                secretWordRepository.saveAll(words);
                log.info("✅ Loaded {} secret words from words.txt", words.size());

            } catch (IOException e) {
                throw new IllegalStateException("❌ Failed to load secret words", e);
            }
        }

        wordSelectorService.selectRandomWord();
        log.info("✅ Random word selected after secret word load");
    }

    // Testing
    public BufferedReader createReader() {
        return new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("words.txt"))
        ));
    }
}