package com.wordleapp.ui;

import com.wordleapp.WordleAppApplication;
import com.wordleapp.model.Game;
import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.GameRepository;
import com.wordleapp.repository.SecretWordRepository;
import com.wordleapp.service.WordSelectorService;
import com.wordleapp.testsupport.BaseTestConfiguration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = WordleAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LeaderboardAndRankingUITest extends BaseTestConfiguration {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SecretWordRepository secretWordRepository;

    @MockBean
    private WordSelectorService wordSelectorService;

    @LocalServerPort
    int port;

    private WebDriver driver;

    private final String secretTestWord = "pesto".toUpperCase();

    @BeforeEach
    void setUp() {
        SecretWord secretWord = secretWordRepository.findByWord(secretTestWord)
                .orElseGet(() -> secretWordRepository.save(new SecretWord(secretTestWord)));

        gameRepository.save(new Game("User1", 1, secretWord, LocalDateTime.now()));

        Mockito.when(wordSelectorService.getCurrentWord()).thenReturn(secretTestWord);

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

        driver.get("http://localhost:" + this.port + "/");
        WordlePage wordlePage = new WordlePage(driver);
        wordlePage.enterUsernameAndStart("TESTUSER");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testLeaderboardPageLoadsAndSorts() {
        driver.get("http://localhost:" + this.port + "/leaderboard");

        WebElement table = driver.findElement(By.id("leaderboard-table"));
        assertNotNull(table, "Leaderboard table should be present");

        WebElement sortSelect = driver.findElement(By.id("sort-select"));
        sortSelect.click();
        sortSelect.findElement(By.cssSelector("option[value='attempts']")).click();

        WebElement leaderboardTable = new WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                .until(drv -> {
                    WebElement t = drv.findElement(By.id("leaderboard-table"));
                    List<WebElement> r = t.findElements(By.tagName("tr"));
                    return r.size() > 1 ? t : null;
                });

        List<WebElement> rows = leaderboardTable.findElements(By.tagName("tr"));

        assertTrue(rows.size() > 1, "There should be at least one row of data");
    }

    @Test
    void testRankingPageLoadsAndFilters() {
        driver.get("http://localhost:" + this.port + "/ranking");

        WebElement input = driver.findElement(By.id("word-input"));
        input.sendKeys("PESTO");

        WebElement topSelect = driver.findElement(By.id("top-select"));
        topSelect.click();
        topSelect.findElement(By.cssSelector("option[value='3']")).click();

        WebElement searchBtn = driver.findElement(By.id("fetch-ranking"));
        searchBtn.click();

        WebElement table = new WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                .until(drv -> {
                    WebElement t = drv.findElement(By.id("ranking-table"));
                    List<WebElement> r = t.findElements(By.tagName("tr"));
                    return r.size() > 1 ? t : null;
                });

        List<WebElement> rows = table.findElements(By.tagName("tr"));
        assertFalse(rows.isEmpty(), "There should be results for the ranking table");

        // Optional: validate column values (e.g., username and attempts)
        List<WebElement> cells = rows.get(1).findElements(By.tagName("td"));
        assertEquals(2, cells.size(), "Ranking rows should only have two columns: Username and Attempts");
    }
}
