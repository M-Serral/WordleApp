package com.wordleapp.api;

import com.wordleapp.service.WordSelectorService;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WordleAttemptsRestTest {

    @Autowired
    private WordSelectorService wordSelectorService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost:" + port + "/api/wordle";
        wordSelectorService.setFixedWordForTesting("sexto");
    }


    @Test
    void testInvalidInput() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/guess?guess=APP")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(equalTo("Invalid input: The word must be 5 letters long."));
    }

    @Test
    void shouldRejectInvalidCharactersInGuess() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/guess?guess=H3LLO")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(equalTo("Invalid input: Only characters from the alphabet are allowed."));
    }
    @Test
    void shouldAllowUpToFiveIncorrectAttempts() {

        SessionFilter sessionFilter = new SessionFilter();

        for (int i = 1; i <= 5; i++) {
            given()
                    .contentType("application/json")
                    .filter(sessionFilter)
                    .when()
                    .post("/guess?guess=WRONG")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(containsString("Try again! Attempts left: " + (6 - i)));

        }
        given()
                .contentType("application/json")
                .filter(sessionFilter)
                .when()
                .post("/guess?guess=WRONG")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("GAME OVER! The secret word was " + wordSelectorService.getCurrentWord()));


    }

    @Test
    void shouldBlockUserAfterSixIncorrectAttempts() {

        SessionFilter sessionFilter = new SessionFilter();

        for (int i = 1; i <= 5; i++) {
            given()
                    .contentType("application/json")
                    .filter(sessionFilter)
                    .when()
                    .post("/guess?guess=WRONG")
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        given()
                .contentType("application/json")
                .filter(sessionFilter)
                .when()
                .post("/guess?guess=WRONG")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("GAME OVER! The secret word was " + wordSelectorService.getCurrentWord()));

        given()
                .contentType("application/json")
                .filter(sessionFilter)
                .when()
                .post("/guess?guess=SEXTO")
                .then()
                .statusCode(HttpStatus.TOO_MANY_REQUESTS.value())
                .body(containsString("You have reached the maximum number of attempts."));


    }

    @Test
    void shouldAllowWinBeforeSixAttempts() {
        // Simulates three unsuccessful attempts before hitting the mark

        SessionFilter sessionFilter = new SessionFilter();

        for (int i = 1; i <= 5; i++) {
            given()
                    .contentType("application/json")
                    .filter(sessionFilter)
                    .when()
                    .post("/guess?guess=WRONG")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(containsString("Try again! Attempts left: " + (6 - i)));
        }

        given()
                .contentType("application/json")
                .filter(sessionFilter)
                .when()
                .post("/guess?guess=SEXTO")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("CORRECT! The word was: SEXTO."));

        // He assures that after winning, he can not keep on trying.
        given()
                .contentType("application/json")
                .filter(sessionFilter)
                .when()
                .post("/guess?guess=WRONG")
                .then()
                .statusCode(HttpStatus.TOO_MANY_REQUESTS.value())
                .body(containsString("GAME OVER! You've already won."));

    }


    @Test
    void shouldResetAttemptsOnNewGame() {

        SessionFilter sessionFilter = new SessionFilter();

        for (int i = 1; i <= 6; i++) {
            given()
                    .contentType("application/json")
                    .filter(sessionFilter)
                    .when()
                    .post("/guess?guess=WRONG");
        }

        given()
                .contentType("application/json")
                .filter(sessionFilter)
                .when()
                .post("/reset")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Game reset! You have 6 attempts."));

        wordSelectorService.setFixedWordForTesting("sexto");

        given()
                .contentType("application/json")
                .filter(sessionFilter)
                .when()
                .post("/guess?guess=NACER")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("Try again! Attempts left: 5"));

        given()
                .contentType("application/json")
                .filter(sessionFilter)
                .when()
                .post("/guess?guess=SEXTO")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("CORRECT! The word was: SEXTO."));
    }

}
