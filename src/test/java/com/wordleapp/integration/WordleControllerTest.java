package com.wordleapp.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WordleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void resetGame() throws Exception {
        mockMvc.perform(post("/api/wordle/reset")
                        .param("user", "testUser"))
                .andExpect(status().isOk());
    }

    @Test
    void testUserWinsGameAndCannotKeepPlaying() throws Exception {
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



    @Test
    void testUserAttemptsAndFails() throws Exception {
        for (int i = 1; i <= 5; i++) {
            String guess = "WRON" + (char) ('A' + i);
            mockMvc.perform(post("/api/wordle/guess")
                            .param("guess", guess)
                            .param("user", "testUser"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Try again! Attempts left: " + (6 - i)));
        }
        mockMvc.perform(post("/api/wordle/guess")
                        .param("guess", "WRONG")
                        .param("user", "testUser"))
                .andExpect(status().isOk())
                .andExpect(content().string("Game over! You've used all attempts."));


    }
}