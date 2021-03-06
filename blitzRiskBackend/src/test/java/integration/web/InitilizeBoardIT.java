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

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gunther Claessens.
 */
@ContextConfiguration(locations = {"/testcontext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class InitilizeBoardIT {
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
        TestUserService.registerUser(driver, "speler1", "test", "speler1@test.be");
        TestUserService.registerUser(driver2, "speler2", "test", "speler2@test.be");
    }


    @After
    public void removeSeleniumUsers() {

    }

    @Test
    public void testCreateGame2Players() {

        //Log in user 1
        TestUserService.loginUser(driver, "speler1", "test");

        //user 1: create game
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElement(By.id("createGameBtn")));

        WebElement element = driver.findElement(By.id("createGameBtn"));
        element.click();

        //add user 2
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElements(By.className("newUserTxt")).size() > 0);
        List<WebElement> elements = driver.findElements(By.className("newUserTxt"));
        for (WebElement e : elements) {
            e.sendKeys("speler2");
        }
        (new WebDriverWait(driver, 25)).until((WebDriver d) -> d.findElements(By.className("addPlayerBtn")).size() > 0);

        element = driver.findElement(By.className("addPlayerBtn"));
        element.click();


        //Log in user 2
        TestUserService.loginUser(driver2, "speler2", "test");

        //user 2: accept game
        (new WebDriverWait(driver2, 15)).until((WebDriver d) -> d.findElements(By.className("gameHeader")).size() > 0);
        elements = driver2.findElements(By.className("gameHeader"));
        elements.get(0).click();


        (new WebDriverWait(driver2, 15)).until((WebDriver d) -> d.findElements(By.className("acceptButton")).size() > 0);
        elements = driver2.findElements(By.className("acceptButton"));
        elements.get(0).click();


        //user 1: accept game
        (new WebDriverWait(driver, 15)).until((WebDriver d) -> d.findElements(By.className("acceptButton")).size() > 0);
        elements = driver.findElements(By.className("acceptButton"));
        elements.get(0).click();


        //user 1: go into game
        (new WebDriverWait(driver, 15)).until((WebDriver d) -> d.findElements(By.className("playButton")).size() > 0);
        elements = driver.findElements(By.className("playButton"));
        elements.get(0).click();

        (new WebDriverWait(driver, 15)).until((WebDriver d) -> d.getCurrentUrl().equals(MyServerConfiguration.getURL() + "#/game"));

        //user 2: go into game
        (new WebDriverWait(driver2, 25)).until((WebDriver d) -> d.findElements(By.className("playButton")).size() > 0);
        elements = driver2.findElements(By.className("playButton"));
        elements.get(0).click();

        (new WebDriverWait(driver2, 25)).until((WebDriver d) -> d.getCurrentUrl().equals(MyServerConfiguration.getURL() + "#/game"));


    }
}
