package com.wordleapp.integration;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @CsvSource({
            "12+, Invalid input: Only characters from the alphabet are allowed.",
            "12345, Invalid input: Only characters from the alphabet are allowed.",
            "APP, Invalid input: The word must be 5 letters long.",
            "plan, Invalid input: The word must be 5 letters long.",
            "<apa, Invalid input: Only characters from the alphabet are allowed.",
            "Tbl?, Invalid input: Only characters from the alphabet are allowed.",
            "'fuel,', Invalid input: Only characters from the alphabet are allowed.",
            "plAn3, Invalid input: Only characters from the alphabet are allowed.",

    })
    void shouldHandleResponseStatusException(String guess, String expectedMessage) throws Exception {
        mockMvc.perform(post("/api/wordle/guess").param("guess", guess))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(expectedMessage)));
    }
}
