package com.wordleapp.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WordlePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By inputField = By.id("guessInput");
    private final By submitButton = By.tagName("button");
    private final By resultMessage = By.id("result");

    public WordlePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void enterGuess(String guess) {
        wait.until(ExpectedConditions.presenceOfElementLocated(inputField));
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputField));
        wait.until(ExpectedConditions.elementToBeClickable(inputField)).clear();
        driver.findElement(inputField).sendKeys(guess);
    }


    public void submitGuess() {
        driver.findElement(submitButton).click();
    }

    public String getResultMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(resultMessage)).getText();
    }

    public void makeGuess(String guess) {
        enterGuess(guess);
        submitGuess();
    }
}
