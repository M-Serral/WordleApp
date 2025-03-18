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
import static org.hamcrest.Matchers.containsString;
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
                .post("/guess?guess=SEXTO")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("SEXTO"));

    }


    @Test
    void testAllIncorrectLetters() {

        SessionFilter sessionFilter = new SessionFilter();

        given()
                .contentType("application/json")
                .filter(sessionFilter)
                .when()
                .post("/guess?guess=APPLE")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("_ _ _ _ _"));
    }


    @Test
    void testUpdatedMemoryOfPreviousAttemptsAndLoose() {

        SessionFilter sessionFilter = new SessionFilter();

        String[] attempts = {"SIEME", "MARIO", "CERRO", "BULOS", "SERTO", "CIETO"};
        String[] expectedClues = {"S _ _ _ _", "S _ _ _ O", "S E _ _ O", "S E _ _ O", "S E _ T O", "S E _ T O"};

        for (int i = 0; i < attempts.length; i++) {
            given()
                    .contentType("application/json")
                    .filter(sessionFilter)
                    .when()
                    .post("/guess?guess=" + attempts[i])
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(containsString(expectedClues[i]));
        }
        given()
                .contentType("application/json")
                .filter(sessionFilter)
                .when()
                .post("/guess?guess=SEXTO")
                .then()
                .statusCode(HttpStatus.TOO_MANY_REQUESTS.value()) // seventh try, out of game
                .body(containsString("S E _ T O"));

    }

    @Test
    void testUpdatedMemoryOfPreviousAttemptsAndWin() {

        SessionFilter sessionFilter = new SessionFilter();

        String[] attempts = {"TEJAS", "EXTRA", "CESTO"};
        String[] expectedClues = {"_ E _ _ _", "_ E _ _ _", "_ E _ T O"};

        for (int i = 0; i < attempts.length; i++) {
            given()
                    .contentType("application/json")
                    .filter(sessionFilter)
                    .when()
                    .post("/guess?guess=" + attempts[i])
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(containsString(expectedClues[i]));
        }

        String[] wrongAttempts = {"T3JAS", "SEXT0", "RAX[O", "SEX"}; // words not allowed
        String[] wrongClues = {"_ E _ T O", "_ E _ T O", "_ E _ T O", "_ E _ T O"}; // We validate that the hint is the same

        for (int i = 0; i < attempts.length; i++) {
            given()
                    .contentType("application/json")
                    .filter(sessionFilter)
                    .when()
                    .post("/guess?guess=" + wrongAttempts[i])
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body(containsString(wrongClues[i]));
        }

        given()
                .contentType("application/json")
                .filter(sessionFilter)
                .when()
                .post("/guess?guess=SEXTO")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("S E X T O"));
    }
}
