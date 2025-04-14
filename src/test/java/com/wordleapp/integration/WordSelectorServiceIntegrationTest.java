
package com.wordleapp.integration;

import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.SecretWordRepository;
import com.wordleapp.service.WordSelectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class WordSelectorServiceIntegrationTest {

    @Autowired
    private SecretWordRepository secretWordRepository;

    @Autowired
    private WordSelectorService wordSelectorService;

    @BeforeEach
    void setUp() {
        // Insert test data into H2
        secretWordRepository.save(new SecretWord("MOUSE"));
        secretWordRepository.save(new SecretWord("PLANT"));
        secretWordRepository.save(new SecretWord("TABLE"));
    }

    @Test
    void shouldSelectRandomWordFromDatabase() {
        wordSelectorService.selectRandomWord();
        String selected = wordSelectorService.getCurrentWord();

        assertNotNull(selected);
        assertEquals(5, selected.length());
    }
}
