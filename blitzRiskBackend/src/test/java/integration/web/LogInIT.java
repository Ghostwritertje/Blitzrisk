package integration.web;

import be.kdg.dao.UserService;
import be.kdg.model.User;
import integration.MyServerConfiguration;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;


/**
 * Created by user jorandeboever
 * Date:9/02/15.
 */
public class LogInIT {
    private final String URL = MyServerConfiguration.URL;
    private static WebDriver driver;

    @BeforeClass
    public static void insertUser() {
        System.setProperty("webdriver.chrome.driver", MyServerConfiguration.CHROMEDRIVERLOCATION);
        String workingDir = System.getProperty("user.dir");
        System.out.println(workingDir);
        UserService userService = new UserService();
        try {
            userService.addUser("seleniumuser", "seleniumpass", "user@selenium.com");
        } catch (Exception e) {

        }
        driver = new ChromeDriver();
    }

    @Test
    public void testNotLoggedIn() {
        driver.get(URL + "#/game");
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.getCurrentUrl().equals(URL + "#/login"));

    }

    @Test
    public void testCorrectLogin() {
        driver.get(URL);
        WebElement usernameElement = driver.findElement(By.id("username"));
        usernameElement.sendKeys("seleniumuser");
        WebElement passwordElement = driver.findElement(By.id("password"));
        passwordElement.sendKeys("seleniumpass");
        usernameElement.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.getCurrentUrl().equals(URL + "#/game"));
    }

    @Test
    public void testIncorrectLogin() {
        driver.get(URL);
        WebElement element = driver.findElement(By.id("username"));
        element.sendKeys("seleniumuser");
        element = driver.findElement(By.id("password"));
        element.sendKeys("wrongpassword");
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.findElement(By.className("errorMessage")));
    }


    @Test
    public void testRegisterNewUser() {
        User user = new User();
        Random random = new Random();
        user.setName("Selenium" + random.nextInt(9999));
        user.setPassword(user.getName());
        user.setEmail(user.getName() + "@kdg.be");

        driver.get(URL + "#/register");
        WebElement element = driver.findElement(By.id("username"));
        element.sendKeys(user.getName());
        element = driver.findElement(By.id("password"));
        element.sendKeys(user.getPassword());
        element = driver.findElement(By.id("email"));
        element.sendKeys(user.getEmail());
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.findElement(By.id("registerSuccess")));

        driver.get(URL);
        element = driver.findElement(By.id("username"));
        element.sendKeys(user.getName());
        element = driver.findElement(By.id("password"));
        element.sendKeys(user.getPassword());
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.getCurrentUrl().equals(URL + "#/game"));

        driver.get(URL + "#/register");
         element = driver.findElement(By.id("username"));
        element.sendKeys(user.getName());
        element = driver.findElement(By.id("password"));
        element.sendKeys(user.getPassword());
        element = driver.findElement(By.id("email"));
        element.sendKeys(user.getEmail());
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.findElement(By.className("errorMessage")));


    }

    @AfterClass
    public static void quitDriver() {
        driver.quit();
    }
}
