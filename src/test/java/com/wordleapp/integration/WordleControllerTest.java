package com.wordleapp.integration;

import com.wordleapp.service.WordSelectorService;
import com.wordleapp.testsupport.BaseTestConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
@AutoConfigureMockMvc
class WordleControllerTest extends BaseTestConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WordSelectorService wordSelectorService;

    private final String  secretTestWord = "sexto".toUpperCase();

    @Test
    void testUserWinsGameAndCannotKeepPlaying() throws Exception {

        MockHttpSession session = new MockHttpSession();

        Mockito.when(wordSelectorService.getCurrentWord()).thenReturn(secretTestWord);

        mockMvc.perform(post("/api/wordle/guess")
                        .param("guess", "SEXTO")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("CORRECT! The secret word was: SEXTO")));
        mockMvc.perform(post("/api/wordle/guess")
                        .param("guess", "sexto")
                        .session(session))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().string(containsString("GAME OVER! You've already won.")));
    }



    @Test
    void testUserAttemptsAndFails() throws Exception {

        MockHttpSession session = new MockHttpSession();

        Mockito.when(wordSelectorService.getCurrentWord()).thenReturn("SEXTO");

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
                .andExpect(content().string(containsString("GAME OVER! The secret word was: " + secretTestWord)));
    }
}