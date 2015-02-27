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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by user jorandeboever
 * Date:19/02/15.
 */
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dispatcher.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ChangeProfileIT {
    private final String URL = MyServerConfiguration.getURL();
    private static WebDriver driver;

    @Autowired
    public UserService userService;

    @BeforeClass
    public static void insertUser() {
        System.setProperty("webdriver.chrome.driver", MyServerConfiguration.getChromedriverlocation());
        driver = new ChromeDriver();
    }

    @Before
    public void logInAsSeleniumUser() {

        try {
            userService.addUser("seleniumuser", "seleniumpass", "user@selenium.com");

        } catch (IllegalArgumentException e) {

        }
        //Log in

        driver.get(MyServerConfiguration.getURL());
        (new WebDriverWait(driver, 15)).until((WebDriver d) -> d.findElement(By.id("username")));
        WebElement element = driver.findElement(By.id("username"));
        element.sendKeys("seleniumuser");
        element = driver.findElement(By.id("password"));
        element.sendKeys("seleniumpass");
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.getCurrentUrl().equals(MyServerConfiguration.getURL() + "#/overview"));

    }

    @AfterClass
    public static void quitDriver() {
        driver.quit();
    }

    @After
    public void removeSeleniumUsers(){

        try {
            userService.removeUser("seleniumuser");

        } catch (IllegalArgumentException e) {

        }

        try {
            userService.removeUser("seleniumuser2");

        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testChangeEmail() {
        WebElement element = driver.findElement(By.id("profileTab"));
        element.click();

        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElement(By.id("email")));
        element = driver.findElement(By.id("email"));
        element.clear();
        element.sendKeys("newemail@selenium.com");
        element = driver.findElement(By.id("password"));
        element.sendKeys("seleniumpass");

        element.sendKeys(Keys.ENTER);

        element = driver.findElement(By.id("profileTab"));
        element.click();
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.findElement(By.id("email")).getAttribute("value").equals("newemail@selenium.com"));


    }

    @Test
    public void testChangePassword() {
        WebElement element = driver.findElement(By.id("profileTab"));
        element.click();
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.findElement(By.id("username")));

        element = driver.findElement(By.id("newPassword"));
        element.sendKeys("newPassword");

        element = driver.findElement(By.id("confirmNewPassword"));
        element.sendKeys("newPassword");

        element = driver.findElement(By.id("password"));
        element.sendKeys("seleniumpass");
        element.sendKeys(Keys.ENTER);

        element = driver.findElement(By.id("logOut"));
        element.click();
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.findElement(By.id("username")));

        element = driver.findElement(By.id("username"));
        element.sendKeys("seleniumuser");

        element = driver.findElement(By.id("password"));
        element.sendKeys("newPassword");
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.getCurrentUrl().equals(URL + "#/overview"));


    }

    @Test
    public void testChangeUsername() {
        WebElement element = driver.findElement(By.id("profileTab"));
        element.click();
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.findElement(By.id("username")).getAttribute("value").equals("seleniumuser"));

        element = driver.findElement(By.id("username"));
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        element.clear();
        element.clear();
        element.sendKeys("seleniumuser2");

        element = driver.findElement(By.id("password"));
        element.sendKeys("seleniumpass");
        element.sendKeys(Keys.ENTER);

        element = driver.findElement(By.id("logOut"));
        element.click();
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.findElement(By.id("username")));

        element = driver.findElement(By.id("username"));
        element.sendKeys("seleniumuser2");

        element = driver.findElement(By.id("password"));
        element.sendKeys("seleniumpass");
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 20)).until((WebDriver d) -> d.getCurrentUrl().equals(URL + "#/overview"));


    }

    @Test
    public void testChangeAllUserDetails() {
        WebElement element = driver.findElement(By.id("profileTab"));
        element.click();
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.findElement(By.id("username")).getAttribute("value").equals("seleniumuser"));

        element = driver.findElement(By.id("username"));
        element.clear();
        element.sendKeys("seleniumuser2");

        element = driver.findElement(By.id("email"));
        element.clear();
        element.sendKeys("newemail@selenium.com");

        element = driver.findElement(By.id("password"));
        element.sendKeys("seleniumpass");

        element = driver.findElement(By.id("newPassword"));
        element.sendKeys("newseleniumpass");

        element = driver.findElement(By.id("confirmNewPassword"));
        element.sendKeys("newseleniumpass");
        element.sendKeys(Keys.ENTER);

        element = driver.findElement(By.id("logOut"));
        element.click();
        (new WebDriverWait(driver, 10)).until((WebDriver d) -> d.findElement(By.id("username")));

        element = driver.findElement(By.id("username"));
        element.sendKeys("seleniumuser2");

        element = driver.findElement(By.id("password"));
        element.sendKeys("newseleniumpass");
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 20)).until((WebDriver d) -> d.getCurrentUrl().equals(URL + "#/overview"));


    }



}
