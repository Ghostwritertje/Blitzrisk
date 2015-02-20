package integration.web;

import be.kdg.dao.UserService;
import be.kdg.model.User;
import be.kdg.services.UserManagerService;
import integration.MyServerConfiguration;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;


/**
 * Created by user jorandeboever
 * Date:9/02/15.
 */

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dispatcher.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class LogInIT {
    private final String URL = MyServerConfiguration.URL;
    private static WebDriver driver;

    @Autowired
    private UserManagerService userService;


    @BeforeClass
    public static void insertUser() {
        System.setProperty("webdriver.chrome.driver", MyServerConfiguration.getChromedriverlocation());
        String workingDir = System.getProperty("user.dir");
        System.out.println(workingDir);

    }

    @Before
    public void createDriver() {
        driver = new ChromeDriver();
    }

    @After
    public void quitDriver() {
        driver.quit();
    }

    @Test
    public void testNotLoggedIn() {
        driver.get(URL + "#/game");
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.getCurrentUrl().equals(URL + "#/login"));
    }


    @Test
    public void testCorrectLogin() {
        userService.addUser("seleniumTestUser", "seleniumTestUser", "seleniumTestUser");
        driver.get(URL);
        WebElement usernameElement = driver.findElement(By.id("username"));
        usernameElement.sendKeys("seleniumTestUser");
        WebElement passwordElement = driver.findElement(By.id("password"));
        passwordElement.sendKeys("seleniumTestUser");
        usernameElement.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.getCurrentUrl().equals(URL + "#/overview"));
        userService.removeUser("seleniumTestUser");
    }

    @Test
    public void testIncorrectLogin() {
        driver.get(URL);
        WebElement element = driver.findElement(By.id("username"));
        element.sendKeys("Seleniumuser");
        element = driver.findElement(By.id("password"));
        element.sendKeys("seleniumuser");
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElement(By.className("errorMessage")));
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
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElement(By.id("registerSuccess")));

        driver.get(URL);
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElement(By.id("username")));
        element = driver.findElement(By.id("username"));
        element.sendKeys(user.getName());
        element = driver.findElement(By.id("password"));
        element.sendKeys(user.getPassword());
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.getCurrentUrl().equals(URL + "#/overview"));

        driver.get(URL + "#/register");
         element = driver.findElement(By.id("username"));
        element.sendKeys(user.getName());
        element = driver.findElement(By.id("password"));
        element.sendKeys(user.getPassword());
        element = driver.findElement(By.id("email"));
        element.sendKeys(user.getEmail());
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElement(By.className("errorMessage")));
        userService.removeUser(user.getName());
    }
}
