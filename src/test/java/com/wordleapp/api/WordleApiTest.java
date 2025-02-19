package com.wordleapp.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class WordleApiTest {

    @BeforeAll
    static void setup() {
        String port = System.getProperty("server.port", "8080");
        RestAssured.baseURI = "http://localhost:" + port + "/api/wordle";
    }

    @Test
    void testCorrectGuess() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/guess?guess=APPLE")
                .then()
                .statusCode(200)
                .body(equalTo("Correct!"));
    }

    @Test
    void testIncorrectGuess() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/guess?guess=PEACH")
                .then()
                .statusCode(200)
                .body(equalTo("Try again!"));
    }

    @Test
     void testInvalidInput() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/guess?guess=APP")
                .then()
                .statusCode(200)
                .body(equalTo("Invalid input. The word must be 5 letters long."));
    }
}
