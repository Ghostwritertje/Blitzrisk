package integration.api;

import be.kdg.dao.UserService;
import be.kdg.services.UserManagerServicee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;

/**
 * Created by user jorandeboever
 * Date:5/02/15.
 */

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dispatcher.xml"})
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SecurityIT {

    private final String URL = "http://localhost:8080/BlitzRisk/api";
    private final String SECURE_PAGE = URL + "/secured/users";

    @Autowired
    public UserService userService;

    @Before
    public void setUp() {
        try {
            userService.removeUser("testuser");
            userService.removeUser("testuser2");
            userService.addUser("testuser", "testuserpass", "testuser@test.be");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testRegistration() {
        given().header("password", "testuserpass").header("email", "testuser@test.be").put(URL + "/user/testuser");
    }

    @Test
    public void testSecurePage() {
        get(SECURE_PAGE).then().assertThat().statusCode(401);
    }


    @Test
    public void testCorrectUser() {
        String token = given().header("password", "testuserpass").get(URL + "/token/testuser").getBody().asString();
      // TODO: wrong error
      //  given().header("X-Auth-Token", token).when().get(SECURE_PAGE).then().assertThat().statusCode(200);

    }

    @Test
    public void testWrongPassword() {
        String token = given().header("password", "wrongpassword").get(URL + "/token/testuser").getBody().asString();
        given().header("X-Auth-Token", token).when().get(SECURE_PAGE).then().assertThat().statusCode(401);
    }

    @Test
    public void testWrongUser() {
        String token = given().header("password", "password").get(URL + "/token/wronguser").getBody().asString();
        given().header("X-Auth-Token", token).when().get(SECURE_PAGE).then().assertThat().statusCode(401);
    }

    @Test
    public void registerNewUser() {
        given().header("password", "testuser2pass").header("email", "testuser2@test.be").put(URL + "/user/testuser2").then().assertThat().statusCode(200);
        String token = given().header("password", "testuser2pass").get(URL + "/token/testuser2").getBody().asString();
        given().header("X-Auth-Token", token).when().get(SECURE_PAGE).then().assertThat().statusCode(200);

    }
    @Test
    public void registerExistingUser() {
        given().header("password", "testuserpass").header("email", "testuser@test.be").put(URL + "/user/testuser").then().assertThat().statusCode(200);
        String token = given().header("password", "testuserpass").get(URL + "/token/testuser").getBody().asString();
        given().header("X-Auth-Token", token).when().get(SECURE_PAGE).then().assertThat().statusCode(200);
    }

}
