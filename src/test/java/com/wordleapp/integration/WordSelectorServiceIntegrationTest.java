
package com.wordleapp.integration;

import com.wordleapp.service.WordSelectorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/sql/secret-words.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class WordSelectorServiceIntegrationTest {

    @Autowired
    private WordSelectorService wordSelectorService;

    @Test
    void shouldSelectRandomWordFromDatabase() {
        wordSelectorService.selectRandomWord();
        String selected = wordSelectorService.getCurrentWord();

        assertNotNull(selected);
        assertEquals(5, selected.length());
    }
}
