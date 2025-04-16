package com.wordleapp.integration;

import com.wordleapp.service.WordSelectorService;
import com.wordleapp.testsupport.BaseTestConfiguration;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WordleIntegrationTest extends BaseTestConfiguration {

    @MockBean
    private WordSelectorService wordSelectorService;

    @LocalServerPort
    private int port;

    private SessionFilter sessionFilter;

    private final String  secretTestWord = "sexto".toUpperCase();

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost:" + port + "/api/wordle";

        Mockito.when(wordSelectorService.getCurrentWord()).thenReturn(secretTestWord);

        sessionFilter = new SessionFilter();
    }

    @Test
    void shouldRejectWordNotInAvailableWordList() {

        String[] availableWords = {"APPLE", "GRAPE", "MANGO", "ASFDD", "QWOPE", "LEMON",
                "SUGAW", "BREAK", "ABECD", "SEXTO"};
        String[] expectedResult = {"Try again! Attempts left: 5",
                "Try again! Attempts left: 4",
                "Try again! Attempts left: 3",
                "Not in the list of valid words",
                "Not in the list of valid words",
                "Try again! Attempts left: 2",
                "Not in the list of valid words",
                "Try again! Attempts left: 1",
                "Not in the list of valid words",
                "CORRECT! The secret word was: SEXTO."};
        String[] httpStatus = {String.valueOf(HttpStatus.OK.value()),
                String.valueOf(HttpStatus.OK.value()),
                String.valueOf(HttpStatus.OK.value()),
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                String.valueOf(HttpStatus.OK.value()),
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                String.valueOf(HttpStatus.OK.value()),
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                String.valueOf(HttpStatus.OK.value())
        };

        // 3 valid words first, 2 invalid words after, 1 valid word later,
        // 1 invalid word, 1 valid, 1 invalid, and secret word
        for (int i = 0; i < availableWords.length; i++) {
            given()
                    .contentType("application/json")
                    .filter(sessionFilter)
                    .when()
                    .post("/guess?guess=" + availableWords[i])
                    .then()
                    .statusCode(Integer.parseInt(httpStatus[i]))
                    .body(containsString(expectedResult[i]));
        }
    }
}