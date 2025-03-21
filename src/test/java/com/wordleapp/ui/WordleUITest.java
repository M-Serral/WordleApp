package com.wordleapp.ui;

import com.wordleapp.WordleAppApplication;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = WordleAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WordleUITest {

    @LocalServerPort
    int port;

    private WebDriver driver;
    private WordlePage wordlePage;


    @BeforeEach
    void setUp() {
        resetGameState();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

        driver.get("http://localhost:"+this.port+"/");
        wordlePage = new WordlePage(driver);

    }

    @AfterEach
    void teardown() {
        if(driver != null) {
            driver.quit();
        }
    }


    /**
     * Method to reset the user status before each test.
     */
    void resetGameState() {
        given()
                .contentType("application/json")
                .when()
                .post("http://localhost:"+this.port+"/api/wordle/reset")
                .then()
                .statusCode(200);
    }


    @Test
    void testTryAttemptsAndWin() {

        List<Map.Entry<String, String>> testCases = List.of(
                Map.entry("PELON", "Try again! Attempts left: 5."),
                Map.entry("app", "Invalid input: The word must be 5 letters long."),
                Map.entry("GAÑAN", "Try again! Attempts left: 4."),
                Map.entry("ARbOL", "Try again! Attempts left: 3."),
                Map.entry("pALoS", "Try again! Attempts left: 2."),
                Map.entry("sexto", "Correct! The word was: SEXTO."),
                Map.entry("CASAS", "Game over! You've already won.")
                );

        for (Map.Entry<String, String> testCase : testCases) {
            String guess = testCase.getKey();
            String expectedMessage = testCase.getValue();

            wordlePage.makeGuess(guess);
            String actualMessage = wordlePage.getResultMessage();

            assertEquals(expectedMessage, actualMessage, "Failed for guess: " + guess);
            if (wordlePage.isGameOver()) {
                break;
            }
        }
    }

    @Test
    void testReachMaximumAttemps() {
        List<Map.Entry<String, String>> testCases = List.of(
                Map.entry("MATAS", "Try again! Attempts left: 5."),
                Map.entry("APP", "Invalid input: The word must be 5 letters long."),
                Map.entry("CATAN", "Try again! Attempts left: 4."),
                Map.entry("ARBOL", "Try again! Attempts left: 3."),
                Map.entry("palos", "Try again! Attempts left: 2."),
                Map.entry("PELOS", "Try again! Attempts left: 1."),
                Map.entry("PELUS", "Game over! You've used all attempts."),
                Map.entry("SEXTO", "You have reached the maximum number of attempts."),
                Map.entry("ASA", "You have reached the maximum number of attempts.")
        );

        for (Map.Entry<String, String> testCase : testCases) {
            String guess = testCase.getKey();
            String expectedMessage = testCase.getValue();

            wordlePage.makeGuess(guess);
            String actualMessage = wordlePage.getResultMessage();

            assertEquals(expectedMessage, actualMessage, "Failed for guess: " + guess);
            if (wordlePage.isGameOver()) {
                break;
            }
        }
    }
}
