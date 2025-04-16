package com.wordleapp.unit;

import com.wordleapp.model.AvailableWord;
import com.wordleapp.repository.AvailableWordRepository;
import com.wordleapp.service.AvailableWordInitializer;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.mockito.Mockito.*;

class AvailableWordInitializerTest {

    @Test
    void shouldLoadWordsWhenRepositoryIsEmpty() {
        // Arrange
        AvailableWordRepository repository = mock(AvailableWordRepository.class);
        when(repository.count()).thenReturn(0L);
        when(repository.existsByWord(anyString())).thenReturn(false);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Spy on initializer to mock private method
        AvailableWordInitializer initializer = spy(new AvailableWordInitializer(repository));

        // Simulated reader mock
        BufferedReader mockReader = new BufferedReader(new StringReader("CASAS\nPERRO\nRATON"));

        // Inject the reader into the private method using doReturn
        doReturn(mockReader).when(initializer).createReaderFromDictionary();

        // Act
        initializer.initWordsFromFile();

        // Assert
        verify(repository, times(3)).save(any(AvailableWord.class));
    }
}
