package com.wordleapp.integration;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("excluded")
@SpringBootTest
@AutoConfigureMockMvc
class WordleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(strings = {"PLANE", "plane", "plano"})
    void testWordGuess(String word) throws Exception {
        boolean isCorrect = word.equalsIgnoreCase("PLANE");

        mockMvc.perform(post("/api/wordle/guess").param("guess", word))
                .andExpect(status().isOk())
                .andExpect(content().string(isCorrect ? "Correct!" : "Try again!"));
    }
}
