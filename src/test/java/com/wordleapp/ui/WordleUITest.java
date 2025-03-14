package com.wordleapp.ui;

import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WordleUITest {

    private WebDriver driver;
    private WordlePage wordlePage;

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

        driver.get("http://localhost:8080/");

        resetGameState();

        Thread.sleep(1000);
    }

    void resetGameState() {
        try {
            ((JavascriptExecutor) driver).executeScript("fetch('/api/wordle/reset?user=testuser', { method: 'POST' });");

            driver.manage().deleteAllCookies();
            ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
            ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


    @Test
    void testValidationsAndResponses() {
        List<Map.Entry<String, String>> testCases = List.of(
                Map.entry("PELON", "Try again! Attempts left: 5"),
                Map.entry("APP", "Invalid input: The word must be 5 letters long."),
                Map.entry("CATAN", "Try again! Attempts left: 4"),
                Map.entry("H3LLO", "Invalid input: Only characters from the alphabet are allowed."),
                Map.entry("ARBOLES", "Try again! Attempts left: 3"), // we are controlling number of characters from front end, the word typed is ARBOL
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


}
