
package com.wordleapp.unit;

import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.SecretWordRepository;
import com.wordleapp.service.SecretWordInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

class SecretWordInitializerTest {

    private SecretWordRepository repository;
    private SecretWordInitializer initializer;

    @BeforeEach
    void setUp() {
        repository = mock(SecretWordRepository.class);
        initializer = new SecretWordInitializer(repository);
    }

    @Test
    void shouldSkipSavingIfWordAlreadyExists() {
        // Prepare a test word that already exists
        String existingWord = "HELLO";
        String fileContent = existingWord + "\nWORLD";

        InputStream mockInput = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        // Spying to mockeying file reading
        SecretWordInitializer spyInitializer = spy(initializer);
        doReturn(new BufferedReader(new InputStreamReader(mockInput)))
                .when(spyInitializer).getBufferedReaderForResource();

        // Mock: the word “HELLO” already exists
        when(repository.existsByWord("HELLO")).thenReturn(true);
        when(repository.existsByWord("WORLD")).thenReturn(false);

        spyInitializer.initWordsFromFile();

        // Verify that “HELLO” is NOT saved but “WORLD” is.
        verify(repository, never()).save(argThat(word -> word.getWord().equals("HELLO")));
        verify(repository).save(argThat(word -> word.getWord().equals("WORLD")));
    }


    @Test
    void shouldInsertWordsFromFileIfDatabaseIsEmpty() throws Exception {
        when(repository.count()).thenReturn(0L);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Simulate words.txt with content
        String mockWords = "apple\nbread\ngrape\nindex";
        InputStream mockInput = new ByteArrayInputStream(mockWords.getBytes());

        // Replacing getResourceAsStream using reflection
        Field field = initializer.getClass().getDeclaredField("secretWordRepository");
        field.setAccessible(true);
        field.set(initializer, repository);

        SecretWordInitializer spyInitializer = spy(initializer);
        doReturn(new BufferedReader(new InputStreamReader(mockInput)))
                .when(spyInitializer)
                .getBufferedReaderForResource();

        spyInitializer.initWordsFromFile();

        verify(repository, times(4)).save(any(SecretWord.class));
    }
}
