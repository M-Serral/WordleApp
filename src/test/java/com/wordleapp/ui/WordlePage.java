package com.wordleapp.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class WordlePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By resultMessage = By.id("result");
    private final By board = By.id("board");


    public WordlePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    public String getResultMessage() {

        wait.until(ExpectedConditions.presenceOfElementLocated(resultMessage));
        WebElement resultElement = wait.until(ExpectedConditions.visibilityOfElementLocated(resultMessage));
        return resultElement.getText();
    }


    public void makeGuessUI(String guess) {
        WebElement body = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        body.sendKeys(guess + Keys.ENTER);
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

    public List<String> getTileClasses(int row) {
        List<String> classes = new ArrayList<>();
        for (int col = 0; col < 5; col++) {
            WebElement tile = driver.findElement(By.id("tile-" + row + "-" + col));
            classes.add(tile.getAttribute("class"));
        }
        return classes;
    }

    public void clickResetButton() {
        WebElement resetBtn = driver.findElement(By.id("resetButton"));
        resetBtn.click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(board));
    }


    public List<String> getTileTexts(int row) {
        List<String> texts = new ArrayList<>();
        for (int col = 0; col < 5; col++) {
            WebElement tile = driver.findElement(By.id("tile-" + row + "-" + col));
            texts.add(tile.getText());
        }
        return texts;
    }


    public boolean isResetButtonVisible() {
        try {
            return driver.findElement(By.id("resetButton")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void enterUsernameAndStart(String username) {
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username-input")));
        usernameInput.clear();
        usernameInput.sendKeys(username);

        WebElement startButton = driver.findElement(By.id("username-submit"));
        startButton.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("username-modal")));
    }

}
