package integration.webapp;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;

/**
 * Created by user jorandeboever
 * Date:9/02/15.
 */
public class LogInIT {
    private final String URL = "http://localhost:63343/blitzriskwebclient/index.html#/";
    @Test
    public void testCorrectLogin() {
        WebDriver driver = new HtmlUnitDriver();
        driver.get(URL);
        WebElement element = driver.findElement(By.id("username"));
        element.sendKeys("testuser");
        element = driver.findElement(By.id("password"));
        element.sendKeys("testuserpass");
        element.submit();
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.getCurrentUrl().equals("URL" + "Game"));
    }

    @Test
    public void testIncorrectLogin() {
        WebDriver driver = new HtmlUnitDriver();
        driver.get(URL);
        WebElement element = driver.findElement(By.id("username"));
        element.sendKeys("testuser");
        element = driver.findElement(By.id("password"));
        element.sendKeys("wrongpassword");
        element.submit();
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.getCurrentUrl().equals("URL" + "Game"));
    }
}
