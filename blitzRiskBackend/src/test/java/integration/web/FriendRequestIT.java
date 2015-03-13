package integration.web;

import be.kdg.services.UserService;
import integration.MyServerConfiguration;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * Selenium test for friend-requests
 */
@ContextConfiguration(locations = {"/testcontext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class FriendRequestIT {
    private final String URL = MyServerConfiguration.getURL();
    private static WebDriver driver;
    private static WebDriver driver2;

    @Autowired
    public UserService userService;

    @BeforeClass
    public static void insertUser() {
        System.setProperty("webdriver.chrome.driver", MyServerConfiguration.getChromedriverlocation());
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver2 = new ChromeDriver();
        driver2.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void quitDriver() {
        driver.quit();
        driver2.quit();
    }

    @Before
    public void registerUsers() {
        TestUserService.registerUser(driver, "testfriend1", "test", "testfriend1@test.be");
        TestUserService.registerUser(driver2, "testfriend2", "test", "testfriend2@test.be");
    }


    @After
    public void removeSeleniumUsers() {

    }

    @Test
    public void addFriend() {


        TestUserService.loginUser(driver, "testfriend1", "test");

        //user 1: add friend 2
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElement(By.id("newFriend")));

        WebElement element = driver.findElement(By.id("newFriend"));
        element.sendKeys("testfriend2");

        element = driver.findElement(By.id("addFriendBtn"));
        element.click();

        //friend 2 accept request
        TestUserService.loginUser(driver2, "testfriend2", "test");
        (new WebDriverWait(driver2, 15)).until((WebDriver d) -> d.findElement(By.className("acceptFriendBtn")));

        element = driver2.findElement(By.className("acceptFriendBtn"));
        element.click();

    }
}
