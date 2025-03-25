package com.wordleapp.ui;

import com.wordleapp.WordleAppApplication;
import com.wordleapp.service.WordSelectorService;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = WordleAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WordleUITest {

    @Autowired
    private WordSelectorService wordSelectorService;
    @LocalServerPort
    int port;

    private WebDriver driver;
    private WordlePage wordlePage;


    @BeforeEach
    void setUp() {

        wordSelectorService.setFixedWordForTesting("sexto");

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



    @Test
    void testTryAttemptsAndWin() {

        List<Map.Entry<String, String>> testCases = List.of(
                Map.entry("PELON", "Try again! Attempts left: 5."),
                Map.entry("app", "Invalid input: The word must be 5 letters long."),
                Map.entry("GAÑAN", "Try again! Attempts left: 4."),
                Map.entry("ARbOL", "Try again! Attempts left: 3."),
                Map.entry("pALoS", "Try again! Attempts left: 2."),
                Map.entry("sexto", "CORRECT! The word was: " + wordSelectorService.getCurrentWord() + "."),
                Map.entry("CASAS", "GAME OVER! You've already won.")
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
                Map.entry("PELUS", "GAME OVER! The secret word was " + wordSelectorService.getCurrentWord() + "."),
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
