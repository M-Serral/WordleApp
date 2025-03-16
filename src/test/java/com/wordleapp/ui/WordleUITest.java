package com.wordleapp.ui;

import com.wordleapp.WordleAppApplication;
import org.junit.jupiter.api.*;
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
    private static final String TEST_USER = "testUser";

    @BeforeEach
    void setUp() throws InterruptedException {

        if (driver != null) {
            driver.quit();
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        wordlePage = new WordlePage(driver);

        resetGameState();

        driver.get("http://localhost:8080/");
    }

    /**
     * Método para resetear el estado del usuario antes de cada test.
     */
    void resetGameState() {
        given()
                .contentType("application/json")
                .when()
                .post("http://localhost:8080/api/wordle/reset?user=" + TEST_USER)
                .then()
                .statusCode(200);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        resetGameState();
    }

    @Test
    void testTryAttemptsAndWin() {
        List<Map.Entry<String, String>> testCases = List.of(
                Map.entry("PELON", "Try again! Attempts left: 5"),
                Map.entry("APP", "Invalid input: The word must be 5 letters long."),
                Map.entry("CATAN", "Try again! Attempts left: 4"),
                Map.entry("H3LLO", "Invalid input: Only characters from the alphabet are allowed."),
                Map.entry("ARBOLES", "Try again! Attempts left: 3"), // La palabra introducida es ARBOL
                Map.entry("PALOS", "Try again! Attempts left: 2"),
                Map.entry("PLANE", "Correct!"),
                Map.entry("CASAS", "Game over! You've already won.")
        );

        for (Map.Entry<String, String> testCase : testCases) {
            String guess = testCase.getKey();
            String expectedMessage = testCase.getValue();

            wordlePage.makeGuess(guess);
            String actualMessage = wordlePage.getResultMessage();

            assertEquals(expectedMessage, actualMessage, "Failed for guess: " + guess);
        }
    }

    @Test
    void testReachMaximumAttemps() {
        List<Map.Entry<String, String>> testCases = List.of(
                Map.entry("MATAS", "Try again! Attempts left: 5"),
                Map.entry("APP", "Invalid input: The word must be 5 letters long."),
                Map.entry("CATAN", "Try again! Attempts left: 4"),
                Map.entry("H3LLO", "Invalid input: Only characters from the alphabet are allowed."),
                Map.entry("ARBOLES", "Try again! Attempts left: 3"),
                Map.entry("PALOS", "Try again! Attempts left: 2"),
                Map.entry("PELOS", "Try again! Attempts left: 1"),
                Map.entry("PELOS", "Game over! You've used all attempts."),
                Map.entry("PLANE", "You have reached the maximum number of attempts.")
        );

        for (Map.Entry<String, String> testCase : testCases) {
            String guess = testCase.getKey();
            String expectedMessage = testCase.getValue();

            wordlePage.makeGuess(guess);
            String actualMessage = wordlePage.getResultMessage();

            assertEquals(expectedMessage, actualMessage, "Failed for guess: " + guess);
        }
    }
}
