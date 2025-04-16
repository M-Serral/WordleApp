package com.wordleapp.unit;

import com.wordleapp.model.AvailableWord;
import com.wordleapp.repository.AvailableWordRepository;
import com.wordleapp.service.AvailableWordInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailableWordInitializerTest {

    @Mock
    private AvailableWordRepository repository;

    @InjectMocks
    private AvailableWordInitializer initializer;

    @Test
    void shouldLoadWordsWhenRepositoryIsEmpty() {
        // Arrange
        AvailableWordRepository repository1 = mock(AvailableWordRepository.class);
        when(repository1.count()).thenReturn(0L);
        when(repository1.existsByWord(anyString())).thenReturn(false);
        when(repository1.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Spy on initializer to mock private method
        AvailableWordInitializer initializer1 = spy(new AvailableWordInitializer(repository1));

        // Simulated reader mock
        BufferedReader mockReader = new BufferedReader(new StringReader("CASAS\nPERRO\nRATON"));

        // Inject the reader into the private method using doReturn
        doReturn(mockReader).when(initializer1).createReaderFromDictionary();

        // Act
        initializer1.initWordsFromFile();

        // Assert
        verify(repository1, times(3)).save(any(AvailableWord.class));
    }

    @Test
    void shouldSkipInitializationIfWordsAlreadyExist() {
        // Arrange: Simulate that there are already words in the database
        when(repository.count()).thenReturn(10L);

        // Act
        initializer.initWordsFromFile();

        // Assert: Must not call save()
        verify(repository, never()).save(any());
        verify(repository, never()).saveAll(any());
        verify(repository).count();
    }


}
