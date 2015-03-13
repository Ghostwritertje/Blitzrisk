package integration.web;

import be.kdg.services.UserService;
import integration.MyServerConfiguration;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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
 * Selenium tests for Chat
 */
@ContextConfiguration(locations = {"/testcontext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ChatIT {
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
        TestUserService.registerUser(driver, "chatspeler1", "test", "chatspeler1@test.be");
        TestUserService.registerUser(driver2, "chatspeler2", "test", "chatspeler2@test.be");
    }

    @Test
    public void testChatBetween2Users() {
        //Log in user 1
        TestUserService.loginUser(driver, "chatspeler1", "test");

        //user 1: create game
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElement(By.id("createGameBtn")));

        WebElement element = driver.findElement(By.id("createGameBtn"));
        element.click();

        //add user 2
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElements(By.className("newUserTxt")).size() > 0);
        List<WebElement> elements = driver.findElements(By.className("newUserTxt"));
        for (WebElement e : elements) {
            e.sendKeys("chatspeler2");
        }
        (new WebDriverWait(driver, 25)).until((WebDriver d) -> d.findElements(By.className("addPlayerBtn")).size() > 0);

        element = driver.findElement(By.className("addPlayerBtn"));
        element.click();


        //Log in user 2
        TestUserService.loginUser(driver2, "chatspeler2", "test");

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
        (new WebDriverWait(driver2, 15)).until((WebDriver d) -> d.findElements(By.className("playButton")).size() > 0);
        elements = driver2.findElements(By.className("playButton"));
        elements.get(0).click();

        //Player 1: send chatMessage
        (new WebDriverWait(driver, 15)).until((WebDriver d) -> d.findElement(By.className("msg_head")));
        element = driver.findElement(By.className("msg_head"));
        element.click();

        element = driver.findElement(By.id("newMessage"));
        element.sendKeys("Hello");
        element = driver.findElement(By.id("sendMessage"));
        element.click();

        //Player 2: receive message
        (new WebDriverWait(driver, 15)).until((WebDriver d) -> d.findElement(By.className("msg_head")));
        element = driver2.findElement(By.className("msg_head"));
        element.click();

        (new WebDriverWait(driver2, 15)).until((WebDriver d) -> d.findElements(By.className("playermessage")).size() > 0);

        //Player 2: send message
        element = driver2.findElement(By.id("newMessage"));
        element.sendKeys("Hi");
        element.sendKeys(Keys.ENTER);

        //Player 1: receive message
        (new WebDriverWait(driver, 15)).until((WebDriver d) -> d.findElements(By.className("playermessage")).size() > 1);


        //player 1 leave page
        element = driver.findElement(By.id("overviewTab"));
        element.click();

        //player 1: enter game again
        (new WebDriverWait(driver, 15)).until((WebDriver d) -> d.findElements(By.className("playButton")).size() > 0);
        elements = driver.findElements(By.className("playButton"));
        elements.get(0).click();

        (new WebDriverWait(driver, 15)).until((WebDriver d) -> d.findElements(By.className("playermessage")).size() > 1);


    }
}
