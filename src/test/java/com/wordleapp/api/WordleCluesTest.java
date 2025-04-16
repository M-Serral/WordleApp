package com.wordleapp.api;

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
class WordleCluesTest extends BaseTestConfiguration {

    @MockBean
    private WordSelectorService wordSelectorService;

    @LocalServerPort
    int port;

    private SessionFilter sessionFilter;

    private final String  secretTestWord = "sexto".toUpperCase();

    @BeforeEach
    void setUp() {

        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost:" + port + "/api/wordle";

        Mockito.when(wordSelectorService.getCurrentWord()).thenReturn(secretTestWord);

        sessionFilter = new SessionFilter();

    }


    @Test
    void testCorrectLetterPositions() {


        given()
                .contentType("application/json")
                .when()
                .post("/guess?guess=SEXTO")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("S E X T O"));

    }


    @Test
    void testAllIncorrectLetters() {


        given()
                .contentType("application/json")
                .when()
                .post("/guess?guess=CLIMA")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("_ _ _ _ _"));
    }

    @Test
    void testMixedCorrectAndIncorrectPositions() {
        given()
                .contentType("application/json")
                .when()
                .post("/guess?guess=SESGO")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("S E _ _ O"));
    }

    @Test
    void testFirstWordPositions() {
        given()
                .contentType("application/json")
                .when()
                .post("/guess?guess=EUROS")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("? _ _ ? ?"));
    }



    @Test
    void testUpdatedMemoryOfPreviousAttemptsAndLoose() {

        String[] attempts = {"EUROS", "SESGO", "SIETE", "TEXTA", "TEXTO", "RESTO"};
        String[] expectedClues = {"? _ _ ? ?", "S E _ _ O", "S _ ? T _", "_ E X T _", "_ E X T O", "_ E ? T O"};

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
                .body(containsString("_ E ? T O"));

    }

    @Test
    void testUpdatedMemoryOfPreviousAttemptsAndWin() {

        String[] attempts = {"TEJAS", "EXTRA", "CESTO"};
        String[] expectedClues = {"? E _ _ ?", "? ? ? _ _", "_ E ? T O"};

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
        String[] wrongClues = {"_ E ? T O", "_ E ? T O", "_ E ? T O", "_ E ? T O"}; // We validate that the hint is the same

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
