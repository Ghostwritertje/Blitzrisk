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

/**
 * Created by Gunther Claessens.
 */
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dispatcher.xml"})
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
        driver2 = new ChromeDriver();
    }

    @Before
    public void logInAsSeleniumUser() {
        driver.get(URL + "#/register");
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElement(By.id("username")));
        WebElement element = driver.findElement(By.id("username"));
        element.sendKeys("speler1");
        element = driver.findElement(By.id("password"));
        element.sendKeys("test");
        element = driver.findElement(By.id("email"));
        element.sendKeys("speler1@test");
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElement(By.id("registerSuccess")));


        //Log in

        driver.get(MyServerConfiguration.getURL());
        (new WebDriverWait(driver, 15)).until((WebDriver d) -> d.findElement(By.id("username")));
        element = driver.findElement(By.id("username"));
        element.sendKeys("speler1");
        element = driver.findElement(By.id("password"));
        element.sendKeys("test");
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.getCurrentUrl().equals(MyServerConfiguration.getURL() + "#/overview"));

        driver2.get(URL + "#/register");
        (new WebDriverWait(driver2, 5)).until((WebDriver d) -> d.findElement(By.id("username")));
        element = driver2.findElement(By.id("username"));
        element.sendKeys("speler2");
        element = driver2.findElement(By.id("password"));
        element.sendKeys("test");
        element = driver2.findElement(By.id("email"));
        element.sendKeys("speler2@test");
        element.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver2, 5)).until((WebDriver d) -> d.findElement(By.id("registerSuccess")));

        driver2.get(MyServerConfiguration.getURL());
        (new WebDriverWait(driver2, 15)).until((WebDriver d) -> d.findElement(By.id("username")));
        WebElement element2 = driver2.findElement(By.id("username"));
        element2.sendKeys("speler2");
        element2 = driver2.findElement(By.id("password"));
        element2.sendKeys("test");
        element2.sendKeys(Keys.ENTER);
        (new WebDriverWait(driver2, 5)).until((WebDriver d) -> d.getCurrentUrl().equals(MyServerConfiguration.getURL() + "#/overview"));

    }

    @AfterClass
    public static void quitDriver() {
        //driver.quit();
        //driver2.quit();
    }

    @After
    public void removeSeleniumUsers(){

  /*      try {
            userService.removeUser("speler1");

        } catch (IllegalArgumentException e) {

        }

        try {
            userService.removeUser("speler2");

        } catch (IllegalArgumentException e) {

        }*/
    }

    @Test
    public void testLoadGameboard(){
        WebElement element = driver.findElement(By.id("createGameBtn"));
        element.click();

        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElement(By.id("newUserTxt")));
        element = driver.findElement(By.id("newUserTxt"));
        element.sendKeys("speler2");
        element = driver.findElement(By.id("addPlayerBtn"));
        element.click();
        element = driver.findElement(By.id("overviewTab"));
        element.click();
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElements(By.className("acceptGameBtns")));
        List<WebElement> elements = driver.findElements(By.className("acceptGameBtns"));
        for (WebElement e : elements){
            e.click();
        }

        element = driver2.findElement(By.id("overviewTab"));
        element.click();
        (new WebDriverWait(driver2, 5)).until((WebDriver d) -> d.findElements(By.className("acceptGameBtns")));
        elements = driver2.findElements(By.className("acceptGameBtns"));
        for (WebElement e : elements){
            e.click();
        }

        element = driver.findElement(By.id("overviewTab"));
        element.click();
        (new WebDriverWait(driver, 5)).until((WebDriver d) -> d.findElements(By.className("acceptGameBtns")));
        elements = driver.findElements(By.className("acceptedGames"));
        elements.get(0).click();
        elements = driver2.findElements(By.className("acceptedGames"));
        elements.get(0).click();
    }
}
