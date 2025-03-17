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
        // Esperar hasta que el mensaje esté presente en el DOM
        wait.until(ExpectedConditions.presenceOfElementLocated(resultMessage));

        // Esperar hasta que sea visible
        WebElement resultElement = wait.until(ExpectedConditions.visibilityOfElementLocated(resultMessage));

        // Obtener y devolver el texto
        return resultElement.getText();
    }


    public void makeGuess(String guess) {
        if (isGameOver()) {
            return; // Si el juego terminó, no intentar escribir en el input
        }
        enterGuess(guess);
        submitGuess();
    }

    public boolean isGameOver() {
        try {
            if (driver.findElements(resultMessage).isEmpty()) {
                return false; // Si el resultado aún no ha aparecido, no ha terminado el juego.
            }
            String resultText = getResultMessage();
            return resultText.contains("Game over!") || resultText.contains("Correct!");
        } catch (Exception e) {
            return false; // Si hay algún error, asumimos que el juego sigue en curso.
        }
    }


}
