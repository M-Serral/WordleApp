package com.wordleapp.integration;

import com.wordleapp.testsupport.BaseTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest extends BaseTestConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldHandleResponseStatusException() throws Exception {
        Map<String, String> testCases = Map.of(
                "12+", "Invalid input: Only characters from the alphabet are allowed.",
                "12345", "Invalid input: Only characters from the alphabet are allowed.",
                "APP", "Invalid input: The word must be 5 letters long.",
                "plan", "Invalid input: The word must be 5 letters long.",
                "<apa", "Invalid input: Only characters from the alphabet are allowed.",
                "Tbl?", "Invalid input: Only characters from the alphabet are allowed.",
                "fuel,", "Invalid input: Only characters from the alphabet are allowed.",
                "plAn3", "Invalid input: Only characters from the alphabet are allowed."
        );

        for (Map.Entry<String, String> testCase : testCases.entrySet()) {
            String guess = testCase.getKey();
            String expectedMessage = testCase.getValue();

            mockMvc.perform(post("/api/wordle/guess")
                            .param("guess", guess))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(expectedMessage));
        }
    }
}
