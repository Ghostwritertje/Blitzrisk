package integration.web;

import be.kdg.services.UserService;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by user jorandeboever
 * Date:9/02/15.
 */

@ContextConfiguration(locations = {"/testcontext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class LogInIT {
    private final String URL = MyServerConfiguration.getURL();
    private static WebDriver driver;

    @Autowired
    private UserService userService;


    @BeforeClass
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", MyServerConfiguration.getChromedriverlocation());
        driver = new ChromeDriver();

    }

    @Before
    public void registerUser() {
        TestUserService.registerUser(driver, "seleniumTestUser", "seleniumTestUserpass", "seleniumTestUser@kdg.be");
    }

    @After
    public void removerUser() {
        userService.removeUser("seleniumTestUser");
    }

    @AfterClass
    public static void cleanUp() {
        driver.quit();
    }

    @Test
    public void testNotLoggedIn() {
        driver.get(URL + "#/game");
        driver.get(URL + "#/game");
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.getCurrentUrl().equals(MyServerConfiguration.getURL() + "#/login"));
    }

    @Test
    public void testCorrectLogin() {
        TestUserService.loginUser(driver, "seleniumTestUser", "seleniumTestUserpass");
    }

    @Test
    public void testLoginWithEmail() {
        TestUserService.loginUser(driver, "seleniumTestUser@kdg.be", "seleniumTestUserpass");
    }

    @Test
    public void testIncorrectLogin() {
        driver.get(URL);
        WebElement element = driver.findElement(By.id("username"));
        element.sendKeys("wrongSeleniumUser");
        element = driver.findElement(By.id("password"));
        element.sendKeys("seleniumTestUserpass");
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElement(By.className("errorMessage")));
    }

    @Test
    public void testRegisterExistingUserName() {
        driver.get(URL + "#/register");
        WebElement element = driver.findElement(By.id("username"));
        element.sendKeys("seleniumTestUser");
        element = driver.findElement(By.id("password"));
        element.sendKeys("seleniumTestUserpass");
        element = driver.findElement(By.id("email"));
        element.sendKeys("newEmailForNewTestUser@kdg.be");
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElement(By.className("errorMessage")));
    }
    @Test
    public void testRegisterExistingEmail() {
        driver.get(URL + "#/register");
        WebElement element = driver.findElement(By.id("username"));
        element.sendKeys("newOriginalSeleniumTestUser");
        element = driver.findElement(By.id("password"));
        element.sendKeys("seleniumTestUserpass");
        element = driver.findElement(By.id("email"));
        element.sendKeys("seleniumTestUser@kdg.be");
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElement(By.className("errorMessage")));
    }
}
