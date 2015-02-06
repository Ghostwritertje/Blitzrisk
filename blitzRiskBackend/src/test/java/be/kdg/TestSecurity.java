package be.kdg;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

import javax.xml.ws.Response;
import java.security.Principal;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by user jorandeboever
 * Date:5/02/15.
 */
public class TestSecurity {

    @Test
    public void testSecurity(){
    //    get("http://localhost:8080/BlitzRisk/api/secured/users").then().assertThat().statusCode(401);
    //    String token = get("http://localhost:8080/BlitzRisk/api/token/Joran/joran").getBody().asString(); // get token for user Joran
    //    given().header("X-Auth-Token", token).when().get("http://localhost:8080/BlitzRisk/api/secured/users").then().assertThat().statusCode(200);

    }
}
