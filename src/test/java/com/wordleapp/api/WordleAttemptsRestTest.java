package com.wordleapp.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureMockMvc
class WordleAttemptsRestTest {

    @BeforeAll
    static void setup() {
        String port = System.getProperty("server.port", "8080");
        RestAssured.baseURI = "http://localhost:" + port + "/api/wordle";
    }

    private static final String TEST_USER = "testUser";

    @BeforeEach
    void resetBeforeEachTest() {
        given()
                .contentType("application/json")
                .when()
                .post("/reset?user=" + TEST_USER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Game reset! You have 6 attempts."));
    }

    @AfterEach
    void resetAfterEachTest() {
        given()
                .contentType("application/json")
                .when()
                .post("/reset?user=" + TEST_USER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Game reset! You have 6 attempts."));
    }

    @Test
    void testInvalidInput() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/guess?guess=APP&user=" + TEST_USER)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(equalTo("Invalid input: The word must be 5 letters long."));
    }

    @Test
    void shouldRejectInvalidCharactersInGuess() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/guess?guess=H3LLO&user=" + TEST_USER)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(equalTo("Invalid input: Only characters from the alphabet are allowed."));
    }
    @Test
    void shouldAllowUpToFiveIncorrectAttempts() {
        for (int i = 1; i <= 5; i++) {
            given()
                    .contentType("application/json")
                    .when()
                    .post("/guess?guess=WRONG&user=" + TEST_USER)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(equalTo("Try again! Attempts left: " + (6 - i)));

        }
        given()
                .contentType("application/json")
                .when()
                .post("/guess?guess=WRONG&user=" + TEST_USER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Game over! You've used all attempts."));

    }

    @Test
    void shouldBlockUserAfterSixIncorrectAttempts() {
        for (int i = 1; i <= 5; i++) {
            given()
                    .contentType("application/json")
                    .when()
                    .post("/guess?guess=WRONG&user=" + TEST_USER)
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        given()
                .contentType("application/json")
                .when()
                .post("/guess?guess=WRONG&user=" + TEST_USER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Game over! You've used all attempts."));

        given()
                .contentType("application/json")
                .when()
                .post("/guess?guess=PLANE&user=" + TEST_USER)
                .then()
                .statusCode(HttpStatus.TOO_MANY_REQUESTS.value())
                .body(equalTo("You have reached the maximum number of attempts."));

    }

    @Test
    void shouldAllowWinBeforeSixAttempts() {
        // Simula tres intentos fallidos antes de acertar
        for (int i = 1; i <= 3; i++) {
            given()
                    .contentType("application/json")
                    .when()
                    .post("/guess?guess=WRONG&user=" + TEST_USER)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body( equalTo("Try again! Attempts left: " + (6 - i)));
        }

        // Ahora introduce la palabra correcta
        given()
                .contentType("application/json")
                .when()
                .post("/guess?guess=PLANE&user=" + TEST_USER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Correct!"));

        // Asegura que después de ganar, ya no puede seguir intentando
        given()
                .contentType("application/json")
                .when()
                .post("/guess?guess=WRONG&user=" + TEST_USER)
                .then()
                .statusCode(HttpStatus.TOO_MANY_REQUESTS.value())
                .body(equalTo("Game over! You've already won."));
    }


    @Test
    void shouldResetAttemptsOnNewGame() {
        for (int i = 1; i <= 6; i++) {
            given()
                    .contentType("application/json")
                    .when()
                    .post("/guess?guess=WRONG&user=" + TEST_USER);
        }

        given()
                .contentType("application/json")
                .when()
                .post("/reset?user=" + TEST_USER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Game reset! You have 6 attempts."));

        given()
                .contentType("application/json")
                .when()
                .post("/guess?guess=WRONG&user=" + TEST_USER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Try again! Attempts left: 5"));
    }
}
