package com.wordleapp.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
 class WordleUITest {

    private WebDriver driver;

    @BeforeAll
    void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);

        String port = System.getProperty("server.port", "8080");
        driver.get("http://localhost:" + port + "/");
    }

    @ParameterizedTest
    @CsvSource({
            "PLANE, Correct!",
            "PEACH, Try again!",
            "APP, Invalid input: The word must be 5 letters long.",
            "H3LLO, Invalid input: Only characters from the alphabet are allowed."
    })
    void testWordleUI(String guess, String expectedMessage) {
        WebElement inputField = driver.findElement(By.id("guessInput"));
        WebElement submitButton = driver.findElement(By.tagName("button"));

        inputField.clear();
        inputField.sendKeys(guess);
        submitButton.click();

        WebElement result = driver.findElement(By.id("result"));
        assertEquals(expectedMessage, result.getText());
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
