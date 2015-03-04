package integration.web;

import be.kdg.model.User;
import integration.MyServerConfiguration;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

/**
 * Service for tests to register and log in.
 */
public class TestUserService {

    public static void registerUser(WebDriver driver, String username, String password, String email) {
        driver.get(MyServerConfiguration.getURL() + "#/register");
        (new WebDriverWait(driver, 15)).until((WebDriver d) -> d.findElement(By.id("username")));
        WebElement element = driver.findElement(By.id("username"));
        element.sendKeys(username);
        element = driver.findElement(By.id("password"));
        element.sendKeys(password);
        element = driver.findElement(By.id("email"));
        element.sendKeys(email);
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver,10)).until((WebDriver d) -> d.findElement(By.id("registerSuccess")));
    }

    public static void loginUser(WebDriver driver, String username, String password){
        driver.get(MyServerConfiguration.getURL() + "#/login");
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.findElement(By.id("username")));
        WebElement usernameElement = driver.findElement(By.id("username"));
        try {
            usernameElement.sendKeys(username);
        }catch (StaleElementReferenceException e){
            usernameElement = driver.findElement(By.id("username"));
            usernameElement.sendKeys(username);
        }
        WebElement passwordElement = driver.findElement(By.id("password"));
        passwordElement.sendKeys(password);
        usernameElement.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> !d.getCurrentUrl().equals(MyServerConfiguration.getURL() + "#/login"));
    }
}
