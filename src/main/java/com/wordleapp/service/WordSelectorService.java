package com.wordleapp.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.security.SecureRandom;


@Service
public class WordSelectorService {

    private static final List<String> WORDS = List.of(
            "girar", "nacer", "luzco", "cielo", "marco", "perro", "plaza", "sabor", "banco", "verde",
            "negro", "vapor", "danza", "canto", "hojas", "raton", "robar", "globo", "piano", "perla",
            "corto", "larga", "turno", "hotel", "fuego", "radio", "acero", "metal", "denso", "brisa"
    );

    private final SecureRandom random = new SecureRandom();

    @Getter
    private String currentWord;

    public WordSelectorService() {
        selectNewWord();
    }

    public void selectNewWord() {
        this.currentWord = WORDS.get(random.nextInt(WORDS.size())).toUpperCase();
    }

    public void setFixedWordForTesting(String word) {
        this.currentWord = word.toUpperCase();
    }
}
