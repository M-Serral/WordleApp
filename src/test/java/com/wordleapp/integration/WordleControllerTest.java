package com.wordleapp.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
@AutoConfigureMockMvc
class WordleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void resetGame() throws Exception {
        mockMvc.perform(post("/api/wordle/reset"))
                        .andExpect(status().isOk());
    }

    @Test
    void testUserWinsGameAndCannotKeepPlaying() throws Exception {

        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/api/wordle/guess")
                        .param("guess", "SEXTO")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Correct! The word was: SEXTO")));
        mockMvc.perform(post("/api/wordle/guess")
                        .param("guess", "sexto")
                        .session(session))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().string(containsString("Game over! You've already won.")));
    }



    @Test
    void testUserAttemptsAndFails() throws Exception {

        MockHttpSession session = new MockHttpSession();

        for (int i = 1; i <= 5; i++) {
            String guess = "WRON" + (char) ('A' + i);
            mockMvc.perform(post("/api/wordle/guess")
                            .param("guess", guess)
                            .session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("Try again! Attempts left: " + (6 - i))));
        }
        mockMvc.perform(post("/api/wordle/guess")
                        .param("guess", "WRONG")
                        .session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("Game over! You've used all attempts.")));
    }

    @Test
    void testGuessWithHint() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/api/wordle/guess")
                        .param("guess", "sexta")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("S E X T _")));
        mockMvc.perform(post("/api/wordle/guess")
                        .param("guess", "S3XTO")
                        .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("S E X T _")));
    }

}