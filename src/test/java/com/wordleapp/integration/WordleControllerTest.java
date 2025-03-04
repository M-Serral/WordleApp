package com.wordleapp.integration;

import com.wordleapp.controller.WordleController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WordleControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @ParameterizedTest
    @CsvSource({
            "PLANE, Correct!",
    })
    public void testUserWinsGame(String guess, String expectedMessage) throws Exception {
        mockMvc.perform(post("/api/wordle/guess")
                        .param("guess", guess)
                        .param("user", "testUser"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));
    }

}