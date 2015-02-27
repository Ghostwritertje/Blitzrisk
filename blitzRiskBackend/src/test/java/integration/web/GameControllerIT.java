package integration.web;

import be.kdg.model.InvitationStatus;
import be.kdg.model.Player;
import be.kdg.model.User;
import be.kdg.services.GameService;
import be.kdg.services.UserService;
import integration.MyServerConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by user jorandeboever
 * Date:25/02/15.
 */
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dispatcher.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class GameControllerIT {
    private final String URL = MyServerConfiguration.getURL() + "api/";


    @Autowired
    public UserService userService;

    @Autowired
    public GameService gameService;

    @Before
    public void setUp() {
        try {
            userService.addUser("testgameuser", "testuserpass", "testgameuser@test.be");
            userService.addUser("testgameuser2", "testuserpass", "testgameuser2@test.be");
            userService.addUser("testgameuser3", "testuserpass", "testgameuser3@test.be");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @After
    public void cleanUp() {

    }

    @Test
    public void testCreateGame() {

        String token = given().header("name", "testgameuser").header("password", "testuserpass").get(URL + "login").getBody().asString();
        given().header("X-Auth-Token", token).get(URL + "createGame").then().assertThat().statusCode(200);

    }

    @Test
    public void testAcceptGame() {

        String token = given().header("name", "testgameuser").header("password", "testuserpass").get(URL + "login").getBody().asString();

        given().header("X-Auth-Token", token).get(URL + "createGame").getBody().asString();
        List<Player> players = gameService.getPlayers("testgameuser");

        for (Player player : players) {
            if (player.getInvitationStatus().equals(InvitationStatus.PENDING))
                given().header("X-Auth-Token", token).put(URL + "acceptGame/" + player.getId()).then().assertThat().statusCode(200);
        }

    }

    @Test
    public void testInviteUser() {
      //  int id = userService.getUser("testgameuser2").getId();
        String token = given().header("name", "testgameuser").header("password", "testuserpass").get(URL + "login").getBody().asString();
        String gameId = given().header("X-Auth-Token", token).get(URL + "createGame").getBody().asString();
        String invitedUsername = given().header("X-Auth-Token", token).post(URL + "game/" + gameId + "/invite/testgameuser2" ).getBody().asString();
        assertEquals("testgameuser2", invitedUsername);
    }

    @Test
    public void testInviteRandomUser() {
        String token = given().header("name", "testgameuser").header("password", "testuserpass").get(URL + "login").getBody().asString();
        String gameId = given().header("X-Auth-Token", token).get(URL + "createGame").getBody().asString();
        given().header("X-Auth-Token", token).post(URL + "game/" + gameId + "/invite-random").then().assertThat().statusCode(200);

    }

    @Test
    public void testGetPlayersForUser() {
        String token = given().header("name", "testgameuser").header("password", "testuserpass").get(URL + "login").getBody().asString();
        given().header("X-Auth-Token", token).get(URL + "createGame").getBody().asString();
        given().header("X-Auth-Token", token).get(URL + "user/" + "testgameuser" + "/players").then().assertThat().statusCode(200);
    }

    @Test
    public void getGame() {
        String token = given().header("name", "testgameuser").header("password", "testuserpass").get(URL + "login").getBody().asString();
        String id = given().header("X-Auth-Token", token).get(URL + "createGame").getBody().asString();
        given().header("X-Auth-Token", token).get(URL + "game/" + id).then().assertThat().statusCode(200);
    }
}
