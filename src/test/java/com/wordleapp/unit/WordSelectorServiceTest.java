
package com.wordleapp.unit;

import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.SecretWordRepository;
import com.wordleapp.service.WordSelectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WordSelectorServiceTest {

    private SecretWordRepository secretWordRepository;
    private WordSelectorService wordSelectorService;

    @BeforeEach
    void setUp() {
        secretWordRepository = mock(SecretWordRepository.class);
        wordSelectorService = new WordSelectorService(secretWordRepository);
    }

    @Test
    void shouldSetCurrentWordWhenRandomWordExists() {
        // given
        SecretWord mockWord = new SecretWord("GRAPE");
        when(secretWordRepository.findRandomWord()).thenReturn(Optional.of((mockWord)));

        // when
        wordSelectorService.selectRandomWord();

        // then
        assertEquals("GRAPE", wordSelectorService.getCurrentWord());
    }

    @Test
    void shouldThrowExceptionWhenRandomWordIsNull() {
        when(secretWordRepository.findRandomWord()).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                wordSelectorService.selectRandomWord());

        assertEquals("Random word not found.", exception.getMessage());
    }

}
