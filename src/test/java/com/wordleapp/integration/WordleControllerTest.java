package com.wordleapp.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WordleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUserWinsGameAndCannotKeepPlaying() throws Exception {
        mockMvc.perform(post("/api/wordle/guess")
                        .param("guess", "PLANE")
                        .param("user", "testUser"))
                .andExpect(status().isOk())
                .andExpect(content().string("Correct!"));
        mockMvc.perform(post("/api/wordle/guess")
                        .param("guess", "plane")
                        .param("user", "testUser"))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().string("Game over! You've already won."));

    }

    @ParameterizedTest
    @CsvSource({
            "WRONa, Try again! Attempts left: 5",
            "WRONb, Try again! Attempts left: 4",
            "WRONc, Try again! Attempts left: 3",
            "WRONd, Try again! Attempts left: 2",
            "WRONe, Try again! Attempts left: 1",
    })
    public void testUserAttemptsAndFails(String guess, String expectedMessage) throws Exception {
        mockMvc.perform(post("/api/wordle/guess")
                        .param("guess", guess)
                        .param("user", "testUserFails"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));
    }
}