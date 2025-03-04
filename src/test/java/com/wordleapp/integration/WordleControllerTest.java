package com.wordleapp.integration;

import com.wordleapp.controller.WordleController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WordleController.class)
public class WordleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WordleController wordleController;

    @Test
    public void testUserAlreadyWon() throws Exception {
        when(wordleController.checkWord("PLANE", "user1"))
                .thenThrow(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Game over! You've already won."));

        mockMvc.perform(post("/api/wordle/guess")
                        .param("guess", "PLANE")
                        .param("user", "user1"))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().string("Game over! You've already won."));
    }

    @Test
    public void testGameOverAttemptsExceeded() throws Exception {
        when(wordleController.checkWord("PLANE", "user2"))
                .thenThrow(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Game over! You've used all attempts."));

        mockMvc.perform(post("/api/wordle/guess")
                        .param("guess", "PLANE")
                        .param("user", "user2"))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().string("Game over! You've used all attempts."));
    }

    @Test
    public void testCorrectGuess() throws Exception {
        when(wordleController.checkWord("PLANE", "user3")).thenReturn("Correct!");

        mockMvc.perform(post("/api/wordle/guess")
                        .param("guess", "PLANE")
                        .param("user", "user3"))
                .andExpect(status().isOk())
                .andExpect(content().string("Correct!"));
    }

    @Test
    public void testIncorrectGuessWithAttemptsLeft() throws Exception {
        when(wordleController.checkWord("WRONG", "user4")).thenReturn("Try again! Attempts left: 5");

        mockMvc.perform(post("/api/wordle/guess")
                        .param("guess", "WRONG")
                        .param("user", "user4"))
                .andExpect(status().isOk())
                .andExpect(content().string("Try again! Attempts left: 5"));
    }

    @Test
    public void testResetGame() throws Exception {
        when(wordleController.resetGame("user5")).thenReturn("Game reset! You have 6 attempts.");

        mockMvc.perform(post("/api/wordle/reset")
                        .param("user", "user5"))
                .andExpect(status().isOk())
                .andExpect(content().string("Game reset! You have 6 attempts."));
    }
}

