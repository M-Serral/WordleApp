
package com.wordleapp.integration;

import com.wordleapp.service.WordSelectorService;
import com.wordleapp.testsupport.BaseTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WordSelectorServiceIntegrationTest extends BaseTestConfiguration {

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
