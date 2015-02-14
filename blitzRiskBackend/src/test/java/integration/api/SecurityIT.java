package integration.api;

import be.kdg.dao.UserService;
import integration.MyServerConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.put;

/**
 * Created by user jorandeboever
 * Date:5/02/15.
 */
@ContextConfiguration(locations = {"/testcontext.xml"})
public class SecurityIT {


    private final String SECURE_PAGE = MyServerConfiguration.URL + "/secured/users";

    private static UserService userService = new UserService();

    @BeforeClass
    public static void configure() {
        userService.removeUser("testuser");
        userService.removeUser("testuser2");
        userService.addUser("testuser", "testuserpass", "testuser@test.be");
    }

    @Test
    public void testSecurePage() {
        get(SECURE_PAGE).then().assertThat().statusCode(401);
    }


    @Test
    public void testCorrectUser() {
        String token = given().header("password", "testuserpass").get(MyServerConfiguration.URL + "/token/testuser").getBody().asString();
      // TODO: wrong error
      //  given().header("X-Auth-Token", token).when().get(SECURE_PAGE).then().assertThat().statusCode(200);

    }

    @Test
    public void testWrongPassword() {
        String token = given().header("password", "wrongpassword").get(MyServerConfiguration.URL + "/token/testuser").getBody().asString();
        given().header("X-Auth-Token", token).when().get(SECURE_PAGE).then().assertThat().statusCode(401);
    }

    @Test
    public void testWrongUser() {
        String token = given().header("password", "password").get(MyServerConfiguration.URL + "/token/wronguser").getBody().asString();
        given().header("X-Auth-Token", token).when().get(SECURE_PAGE).then().assertThat().statusCode(401);
    }

    @Test
    public void registerNewUser() {
        given().header("password", "testuser2pass").header("email", "testuser2@test.be").put(MyServerConfiguration.URL + "/user/testuser2").then().assertThat().statusCode(200);
        String token = given().header("password", "testuser2pass").get(MyServerConfiguration.URL + "/token/testuser2").getBody().asString();
        given().header("X-Auth-Token", token).when().get(SECURE_PAGE).then().assertThat().statusCode(200);

    }
    @Test
    public void registerExistingUser() {
        given().header("password", "testuserpass").header("email", "testuser@test.be").put(MyServerConfiguration.URL + "/user/testuser").then().assertThat().statusCode(200);
        String token = given().header("password", "testuserpass").get(MyServerConfiguration.URL + "/token/testuser").getBody().asString();
        given().header("X-Auth-Token", token).when().get(SECURE_PAGE).then().assertThat().statusCode(200);
    }

}
