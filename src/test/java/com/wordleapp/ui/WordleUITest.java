package com.wordleapp.ui;

import com.wordleapp.WordleAppApplication;
import com.wordleapp.service.WordSelectorService;
import com.wordleapp.testsupport.BaseTestConfiguration;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = WordleAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WordleUITest extends BaseTestConfiguration {

    @MockBean
    private WordSelectorService wordSelectorService;

    @LocalServerPort
    int port;

    private WebDriver driver;
    private WordlePage wordlePage;

    private final String secretTestWord = "noble".toUpperCase();


    @BeforeEach
    void setUp() {

        Mockito.when(wordSelectorService.getCurrentWord()).thenReturn(secretTestWord);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

        driver.get("http://localhost:" + this.port + "/");
        wordlePage = new WordlePage(driver);
        wordlePage.enterUsernameAndStart("TESTUSER");
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testTryAttemptsAndWin() {

        wordlePage.makeGuessUI("JUNIO");
        wordlePage.getResultMessage();
        List<String> row0Classes = wordlePage.getTileClasses(0);
        List<String> expectedRow0 = List.of("gray", "gray", "orange", "gray", "orange");
        for (int i = 0; i < 5; i++) {

            assertTrue(row0Classes.get(i).contains(expectedRow0.get(i)),
                    "In row 0, column " + i + " expected " + expectedRow0.get(i)
                            + " but obtained: " + row0Classes.get(i));
        }

        wordlePage.makeGuessUI("NORTE");
        wordlePage.getResultMessage();
        List<String> row1Classes = wordlePage.getTileClasses(1);
        List<String> expectedRow1 = List.of("green", "green", "gray", "gray", "green");
        for (int i = 0; i < 5; i++) {
            assertTrue(row1Classes.get(i).contains(expectedRow1.get(i)),
                    "In row 1, column " + i + " expected " + expectedRow1.get(i)
                            + " but obtained: " + row1Classes.get(i));
        }

        wordlePage.makeGuessUI("NOBLE");
        String message3 = wordlePage.getResultMessage();
        List<String> row2Classes = wordlePage.getTileClasses(2);
        List<String> expectedRow2 = List.of("green", "green", "green", "green", "green");
        for (int i = 0; i < 5; i++) {
            assertTrue(row2Classes.get(i).contains(expectedRow2.get(i)),
                    "In row 2, column " + i + " was expected " + expectedRow2.get(i)
                            + " but obtained: " + row2Classes.get(i));
        }
        assertTrue(message3.contains("CORRECT"), "The message does not indicate victory on the third attempt.");
    }

    @Test
    void testReachMaximumAttemps() {

        String[] guesses = {"CUSTA", "VIGIA", "MARCA", "TURMA", "CAPAR", "RUMIA"};
        List<String> expectedColors = List.of("gray", "gray", "gray", "gray", "gray");

        for (int i = 0; i < guesses.length; i++) {
            wordlePage.makeGuessUI(guesses[i]);
            wordlePage.getResultMessage();
            List<String> rowClasses = wordlePage.getTileClasses(i);
            for (int j = 0; j < 5; j++) {
                assertTrue(rowClasses.get(j).contains(expectedColors.get(j)),
                        "In row 2, column " + i + ", column " + j + " was expected " + expectedColors.get(j)
                                + " but obtained: " + rowClasses.get(j));
            }
        }

        String finalMessage = wordlePage.getResultMessage();
        assertTrue(finalMessage.contains("GAME OVER!"), "The final message does not indicate that the maximum number of attempts has been reached.");

        wordlePage.makeGuessUI("NOBLE");

        String postMessage = wordlePage.getResultMessage();
        assertTrue(postMessage.contains("GAME OVER!"),
                "After reaching the maximum number of attempts, the GAME OVER message should still be displayed.");
    }

    @Test
    void testResetGame() {
        String[] wrongGuesses = {"CUSTA", "VIGIA", "MARCA", "TURMA", "CAPAR", "RUMIA"};
        for (String guess : wrongGuesses) {
            wordlePage.makeGuessUI(guess);
            wordlePage.getResultMessage();
        }

        assertTrue(wordlePage.isGameOver(), "Game should be over after maximum attempts.");
        assertTrue(wordlePage.isResetButtonVisible(), "Reset button should be visible when game is over.");

        wordlePage.clickResetButton();

        wordlePage.getTileTexts(0);
        for (int row = 0; row < 6; row++) {
            List<String> rowTexts = wordlePage.getTileTexts(row);
            for (int col = 0; col < rowTexts.size(); col++) {
                assertTrue(rowTexts.get(col).isEmpty(),
                        "After reset, tile in row " + row + ", column " + col + " should be empty.");
            }
        }
    }

    @Test
    void testInvalidWordClearsRowAndShowsError() {
        String invalidWord = "TRAZO"; // palabra no en available_word

        wordlePage.makeGuessUI(invalidWord);

        String result = wordlePage.getResultMessage();
        assertTrue(result.contains("Not in the list of valid words"),
                "Expected error message for invalid word");

        List<String> tileTexts = wordlePage.getTileTexts(0);
        for (int i = 0; i < tileTexts.size(); i++) {
            assertTrue(tileTexts.get(i).isEmpty(), "Tile at row 0, col " + i + " should be empty after invalid word");
        }
    }

}