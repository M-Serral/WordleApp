package com.wordleapp.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.security.SecureRandom;


@Service
public class WordSelectorService {

    private static final List<String> WORDS = List.of(
            "GIRAR", "NACER", "LUZCO", "CIELO", "MARCO", "PERRO", "PLAZA", "SABOR", "BANCO", "VERDE",
            "NEGRO", "VAPOR", "DANZA", "CANTO", "HOJAS", "RATON", "ROBAR", "GLOBO", "PIANO", "PERLA",
            "CORTO", "LARGA", "TURNO", "HOTEL", "FUEGO", "RADIO", "ACERO", "METAL", "DENSO", "BRISA"
    );

    private final SecureRandom random = new SecureRandom();
    @Getter
    private String currentWord;

    public WordSelectorService() {
        selectNewWord();
    }

    public void selectNewWord() {
        this.currentWord = WORDS.get(random.nextInt(WORDS.size()));
    }

    public void setFixedWordForTesting(String word) {
        this.currentWord = word;
    }
}
