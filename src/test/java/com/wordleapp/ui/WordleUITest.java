package com.wordleapp.ui;

import com.wordleapp.WordleAppApplication;
import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = WordleAppApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WordleUITest {

    private WebDriver driver;
    private WordlePage wordlePage;

    @BeforeAll
    void setUpAll() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        wordlePage = new WordlePage(driver);
    }

    @BeforeEach
    void setUp() {
        resetGameState();

        driver.manage().deleteAllCookies();

        // Check if sessionStorage is available before deletion
        try {
            Boolean isStorageAvailable = (Boolean) ((JavascriptExecutor) driver).executeScript("return typeof window.sessionStorage !== 'undefined';");
            if (Boolean.TRUE.equals(isStorageAvailable)) {
                ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
                ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
            }
        } catch (Exception e) {
            System.out.println("sessionStorage is not available in this environment.");
        }

        driver.get("http://localhost:8080/");
    }



    /**
     * Método para resetear el estado del usuario antes de cada test.
     */
    void resetGameState() {
        given()
                .contentType("application/json")
                .when()
                .post("http://localhost:8080/api/wordle/reset")
                .then()
                .statusCode(200);
    }

    @AfterAll
    void tearDownAll() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testTryAttemptsAndWin() {
        List<Map.Entry<String, String>> testCases = List.of(
                Map.entry("PELON", "Try again! Attempts left: 5."),
                Map.entry("app", "Invalid input: The word must be 5 letters long."),
                Map.entry("GAÑAN", "Try again! Attempts left: 4."),
                Map.entry("H3LLO", "Invalid input: Only characters from the alphabet are allowed."),
                Map.entry("ARbOL", "Try again! Attempts left: 3."),
                Map.entry("pALoS", "Try again! Attempts left: 2."),
                Map.entry("sexto", "Correct! The word was: SEXTO"),
                Map.entry("CASAS", "Game over! You've already won."),
                Map.entry("P0T4S", "Game over! You've already won.")
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
                Map.entry("H3LLO", "Invalid input: Only characters from the alphabet are allowed."),
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
