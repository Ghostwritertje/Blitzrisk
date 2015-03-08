package integration.api;

import be.kdg.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import integration.MyServerConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;

/**
 * Created by user jorandeboever
 * Date:5/02/15.
 */

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dispatcher.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SecurityIT {

    private final String URL = MyServerConfiguration.getURL() + "api/";


    @Autowired
    public UserService userService;

    @Before
    public void setUp() {
        try {

            userService.addUser("testuser", "testuserpass", "testuser@test.be");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @After
    public void cleanUp() {
        try {
            userService.removeUser("testuser");
            userService.removeUser("testuser2");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void testRegistration() {
        given().header("password", "testuserpass").header("email", "testuser2@test.be").put(URL + "/user/testuser2");
    }

    @Test
    public void testSecurePage() {
        get(URL + "friends").then().assertThat().statusCode(401);
    }

    @Test
    public void testCorrectUser() {
        String token = given().header("name", "testuser").header("password", "testuserpass").get(URL + "login").getBody().asString();
        given().header("X-Auth-Token", token).when().get(URL + "friends").then().assertThat().statusCode(200);

    }

    @Test
    public void testWrongPassword() {
        given().header("name", "testuser").header("password", "wrongpassword").get(URL + "login").then().assertThat().statusCode(400);
    }

    @Test
    public void testWrongUser() {
        given().header("name", "wronguser").header("password", "password").get(URL + "login").then().assertThat().statusCode(400);
    }

    @Test
    public void registerNewUser() {
        given().header("password", "testuser2pass").header("email", "testuser2@test.be").put(URL + "user/testuser2").then().assertThat().statusCode(200);

    }

    @Test
    public void registerExistingUser() {
        given().header("password", "testuserpass").header("email", "testuser@test.be").put(URL + "user/testuser").then().assertThat().statusCode(500);
    }

}
