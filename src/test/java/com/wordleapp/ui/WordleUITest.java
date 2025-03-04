package com.wordleapp.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WordleUITest {

    private WebDriver driver;
    private WordlePage wordlePage;

    @BeforeAll
    void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        wordlePage = new WordlePage(driver);

        String port = System.getProperty("server.port", "8080");
        driver.get("http://localhost:" + port + "/");
    }

    @BeforeEach
    void resetBeforeTest() {
        ((JavascriptExecutor) driver).executeScript("fetch('/api/wordle/reset?user=testuser', { method: 'POST' });");
    }

    @AfterEach
    void resetGame() {
            ((JavascriptExecutor) driver).executeScript("fetch('/api/wordle/reset?user=testuser', { method: 'POST' });");
    }


    @ParameterizedTest
    @CsvSource({
            "PELON, Try again! Attempts left: 5",
            "APP, Invalid input: The word must be 5 letters long.",
            "CATAN, Try again! Attempts left: 4",
            "H3LLO, Invalid input: Only characters from the alphabet are allowed.",
            "ARBOLES, Try again! Attempts left: 3", // we are controlling number of characters from front end, the word typed is ARBOL
            "PALOS, Try again! Attempts left: 2",
            "PLANE, Correct!",
            "CASAS, Game over! You've already won."

    })
    void testValidationsAndResponses(String guess, String expectedMessage) {
        wordlePage.makeGuess(guess);
        assertEquals(expectedMessage, wordlePage.getResultMessage());
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
