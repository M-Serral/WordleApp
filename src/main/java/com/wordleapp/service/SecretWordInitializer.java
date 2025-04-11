package com.wordleapp.service;

import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.SecretWordRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class SecretWordInitializer {

    private final SecretWordRepository secretWordRepository;

    public SecretWordInitializer(SecretWordRepository secretWordRepository) {
        this.secretWordRepository = secretWordRepository;
    }

        @PostConstruct
        public void initWordsFromFile() {
            if (secretWordRepository.count() > 0) {
                return;
            }

            try (InputStream input = getClass().getResourceAsStream("/words.txt")) {
                assert input != null;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

                    List<String> words = reader.lines()
                            .map(String::trim)
                            .map(String::toUpperCase) // 👈 AQUI
                            .filter(w -> w.length() == 5)
                            .distinct()
                            .toList();


                    words.forEach(word -> secretWordRepository.save(new SecretWord(word)));
                }
            } catch (IOException e) {
                throw new IllegalStateException("Could not load words.txt", e);
            }
        }
    }