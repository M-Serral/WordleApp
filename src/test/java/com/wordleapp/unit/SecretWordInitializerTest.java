package com.wordleapp.unit;

import com.wordleapp.repository.SecretWordRepository;
import com.wordleapp.service.SecretWordInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

class SecretWordInitializerTest {

    private SecretWordRepository secretWordRepository;

    @BeforeEach
    void setUp() {
        secretWordRepository = mock(SecretWordRepository.class);
    }

    @Test
    void shouldInsertWordsAndSelectRandomOne() {
        String content = "CASAS\nPERRO\nRATON";
        BufferedReader mockReader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))));

        SecretWordInitializer spy = spy(new SecretWordInitializer(secretWordRepository));
        doReturn(0L).when(secretWordRepository).count();
        doReturn(mockReader).when(spy).createReader();

        when(secretWordRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        spy.initSecretWordsFromFile();

        verify(secretWordRepository).saveAll(anyList());
    }

    @Test
    void shouldSkipIfAlreadyLoaded() {
        SecretWordInitializer initializer = new SecretWordInitializer(secretWordRepository);
        when(secretWordRepository.count()).thenReturn(100L);

        initializer.initSecretWordsFromFile();

        verify(secretWordRepository, never()).save(any());
    }
}
