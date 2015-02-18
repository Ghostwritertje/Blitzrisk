package integration.api;

import be.kdg.services.UserManagerService;
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

    private final String URL = "http://localhost:8080/BlitzRisk/api";
    //private final String SECURE_PAGE = URL + "/secured/users";

    private final String SECURE_PAGE = MyServerConfiguration.URL + "/secured/users";


    @Autowired
    public UserManagerService userService;

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
    public void testRegistration() { //ok
        given().header("password", "testuserpass").header("email", "testuser2@test.be").put(URL + "/user/testuser2");
    }

    @Test
    public void testSecurePage() {
        get(SECURE_PAGE).then().assertThat().statusCode(401);
    }


    @Test
    public void testCorrectUser() { //ok
        //String token = given().header("password", "testuserpass").get(URL + "/token/testuser").getBody().asString();
        // TODO: wrong error
       // given().header("X-Auth-Token", token).when().get(SECURE_PAGE).then().assertThat().statusCode(200);
        String token = given().header("password", "testuserpass").get(MyServerConfiguration.URL + "/token/testuser").getBody().asString();
      // TODO: wrong error
       // given().header("X-Auth-Token", token).when().get(SECURE_PAGE).then().assertThat().statusCode(200);
    }

    @Test
    public void testWrongPassword() {
       //given().header("name", "testuser").header("password", "wrongpassword").get(URL + "/login").then().assertThat().statusCode(400);
        String token = given().header("password", "wrongpassword").get(MyServerConfiguration.URL + "/token/testuser").getBody().asString();
        given().header("X-Auth-Token", token).when().get(SECURE_PAGE).then().assertThat().statusCode(401);
    }

    @Test
    public void testWrongUser() {
        //given().header("name", "wronguser").header("password", "password").get(URL + "/login").then().assertThat().statusCode(400);
        String token = given().header("password", "password").get(MyServerConfiguration.URL + "/token/wronguser").getBody().asString();
        given().header("X-Auth-Token", token).when().get(SECURE_PAGE).then().assertThat().statusCode(401);
    }

    @Test
    public void registerNewUser() {
        //given().header("password", "testuser2pass").header("email", "testuser2@test.be").put(URL + "/user/testuser2").then().assertThat().statusCode(200);
        given().header("password", "testuser2pass").header("email", "testuser2@test.be").put(MyServerConfiguration.URL + "/user/testuser2").then().assertThat().statusCode(200);
        String token = given().header("password", "testuser2pass").get(MyServerConfiguration.URL + "/token/testuser2").getBody().asString();
        given().header("X-Auth-Token", token).when().get(SECURE_PAGE).then().assertThat().statusCode(200);
    }

    @Test
    public void registerExistingUser() {
        //given().header("password", "testuserpass").header("email", "testuser@test.be").put(URL + "/user/testuser").then().assertThat().statusCode(500);
        given().header("password", "testuserpass").header("email", "testuser@test.be").put(MyServerConfiguration.URL + "/user/testuser").then().assertThat().statusCode(200);
        String token = given().header("password", "testuserpass").get(MyServerConfiguration.URL + "/token/testuser").getBody().asString();
        given().header("X-Auth-Token", token).when().get(SECURE_PAGE).then().assertThat().statusCode(200);
    }
}
