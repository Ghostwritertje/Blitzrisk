package integration.web;

import be.kdg.model.User;
import integration.MyServerConfiguration;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

/**
 * Created by user jorandeboever
 * Date:1/03/15.
 */
public class TestUserService {

    public static void registerUser(WebDriver driver, String username, String password, String email) {
        driver.get(MyServerConfiguration.getURL() + "#/register");
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElement(By.id("username")));
        WebElement element = driver.findElement(By.id("username"));
        element.sendKeys(username);
        element = driver.findElement(By.id("password"));
        element.sendKeys(password);
        element = driver.findElement(By.id("email"));
        element.sendKeys(email);
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElement(By.id("registerSuccess")));
    }

    public static void loginUser(WebDriver driver, String username, String password){
        driver.get(MyServerConfiguration.getURL() + "#/login");
        WebElement usernameElement = driver.findElement(By.id("username"));
        usernameElement.sendKeys(username);
        WebElement passwordElement = driver.findElement(By.id("password"));
        passwordElement.sendKeys(password);
        usernameElement.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> !d.getCurrentUrl().equals(MyServerConfiguration.getURL() + "#/login"));
    }
}
