package com.wordleapp.api;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
class WordleCluesTest {

    @BeforeAll
    static void setup() {
        String port = System.getProperty("server.port", "8080");
        RestAssured.baseURI = "http://localhost:" + port + "/api/wordle";
    }

    @BeforeEach
    void resetBeforeEachTest() {
        given()
                .contentType("application/json")
                .when()
                .post("/reset")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Game reset! You have 6 attempts."));
    }

    @AfterEach
    void resetAfterEachTest() {
        given()
                .contentType("application/json")
                .when()
                .post("/reset")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Game reset! You have 6 attempts."));
    }

    @Test
    void testCorrectLetterPositions() {

        SessionFilter sessionFilter = new SessionFilter();

        given()
                .contentType("application/json")
                .filter(sessionFilter)
                .when()
                .post("/guessWithHint?guess=SESGO")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("hint", equalTo("S E _ _ O"));
    }

    @Test
    void testIncorrectLetters() {

        SessionFilter sessionFilter = new SessionFilter();

        given()
                .contentType("application/json")
                .filter(sessionFilter)
                .when()
                .post("/guessWithHint?guess=MUNDO")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("hint", equalTo("_ _ _ _ O"));
    }

    @Test
    void testNoMemoryOfPreviousAttempts() {

        SessionFilter sessionFilter = new SessionFilter();

        given()
                .contentType("application/json")
                .filter(sessionFilter)
                .when()
                .post("/guessWithHint?guess=SESGo")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("hint", equalTo("S E _ _ O"));

        given()
                .contentType("application/json")
                .filter(sessionFilter)
                .when()
                .post("/guessWithHint?guess=siete")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("hint", equalTo("S _ _ T _"));
    }
}
