package com.wordleapp.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void enterGuess(String guess) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputField));
        wait.until(ExpectedConditions.elementToBeClickable(inputField));
        driver.findElement(inputField).clear();
        driver.findElement(inputField).sendKeys(guess);
    }


    public void submitGuess() {
        driver.findElement(submitButton).click();
    }

    public String getResultMessage() {

        wait.until(ExpectedConditions.presenceOfElementLocated(resultMessage));
        WebElement resultElement = wait.until(ExpectedConditions.visibilityOfElementLocated(resultMessage));
        return resultElement.getText();
    }


    public void makeGuess(String guess) {
        if (isGameOver()) {
            return; // If the game is over, do not try to type in the input
        }
        enterGuess(guess);
        submitGuess();
    }

    public boolean isGameOver() {
        try {
            if (driver.findElements(resultMessage).isEmpty()) {
                return false; // If the result has not yet appeared, the game is not over.
            }
            String resultText = getResultMessage();
            return resultText.contains("GAME OVER!") || resultText.contains("CORRECT!");
        } catch (Exception e) {
            return false; // If there are any errors, we assume that the game is still in progress.
        }
    }


}
