package integration.api;

import be.kdg.exceptions.IllegalUserInviteException;
import be.kdg.model.*;
import be.kdg.services.*;
import com.jayway.restassured.http.ContentType;
import integration.MyServerConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;

/**
 * Created by Marlies on 2/03/2015.
 */

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dispatcher.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class EnterTurnsIT {

    private final String URL = MyServerConfiguration.getURL() + "api/";

    @Autowired
    public UserService userService;
    @Autowired
    public GameService gameService;
    @Autowired
    public PlayerService playerService;
    @Autowired
    public TurnService turnService;
    @Autowired
    public TerritoryService territoryService;

    private List<Player> players;
    private int game;
    private Territory origin;
    private Territory destination;

    @Before
    public void setUp() throws IllegalUserInviteException {

        userService.addUser("turntestgameuser", "turntestuserpass", "turntestgameuser@test.be");
        userService.addUser("turntestgameuser2", "turntestuserpass", "turntestgameuser2@test.be");

        Game gameObject = gameService.createNewGame();
        game = gameObject.getId();

        gameService.addUserToGame(userService.getUser("turntestgameuser"), gameObject);
        gameService.addUserToGame(userService.getUser("turntestgameuser2"), gameObject);
        players = gameObject.getPlayers();
        for (Player player: players) {
            player.setInvitationStatus(InvitationStatus.ACCEPTED);
            //playerService.save(player);
        }
        //gameObject.setTerritories();
        gameService.saveTerritories(gameObject,new ArrayList<>(territoryService.getTerritories() ));
        //gameObject.assignRandomTerritories();



        int i = 0;
        destination = null;
        boolean destinationFound = false;
        while (!destinationFound && i < gameObject.getTerritories().size()) {
            origin = gameObject.getTerritories().get(i);
            for (Territory territory : origin.getNeighbourTerritories()) {
                if (!territory.getPlayer().equals(origin.getPlayer())) {
                    destination = territory;
                    destinationFound = true;
                }

            }
            i++;
        }
    }

    @After
    public void cleanUp() {
        Game gameObject = gameService.getGame(game);
        turnService.removeTurns(gameObject);

        for(Territory territory: gameObject.getTerritories()) {
            territoryService.removeTerritory(territory);
        }
        for(Player player: players) {
            playerService.removePlayer(player);
        }
        gameService.removeGame(gameService.getGame(game));
        userService.removeUser("turntestgameuser");
        userService.removeUser("turntestgameuser2");
    }

    @Test
    public void askReinforcementNumber() {
        turnService.createTurn(gameService.getGame(game), origin.getPlayer());
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON).headers("X-Auth-Token", token, "playerId", origin.getPlayer().getId()).get(URL + "numberOfReinforcements").then().assertThat().statusCode(200);

    }

    @Test
    public void getPlayerStatus() {
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON).headers("X-Auth-Token", token, "playerId", origin.getPlayer().getId()).get(URL + "getPlayerStatus").then().assertThat().statusCode(200);

    }

    @Test
    public void reinforce() {
        Turn turn = turnService.createTurn(gameService.getGame(game), origin.getPlayer());
        String moveWrapperList = String.format("[{\"id\":1,\"turnId\":%d,\"origin\":%d,\"destination\":%d,\"units\":3,\"calculatedUnits\":0}]", turn.getId(), origin.getId(), origin.getId());
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token, "playerId", "" + origin.getPlayer().getId())
                .request().body(moveWrapperList)
                .post(URL + "reinforce").then().assertThat().statusCode(200);
    }

    @Test
    public void attack() {
        Turn turn = turnService.createTurn(gameService.getGame(game), origin.getPlayer());
        turn.setPlayer(origin.getPlayer());
        turnService.saveTurn(turn);
        origin.setNumberOfUnits(3);
        territoryService.updateTerritory(origin);
        String moveWrapperList = String.format("[{\"id\":1,\"turnId\":%d,\"origin\":%d,\"destination\":%d,\"unitsToAttackOrReinforce\":1}]", turn.getId(), origin.getId(), destination.getId());
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token, "playerId", "" + origin.getPlayer().getId())
                .request().body(moveWrapperList)
                .post(URL + "attack").then().assertThat().statusCode(200);
    }

    @Test
    public void recentTurns() {
        for (int i = 0; i< 3; i++) {
            Turn turn = turnService.createTurn(gameService.getGame(game), origin.getPlayer());
            turn.setPlayer(origin.getPlayer());
            turnService.saveTurn(turn);
        }

        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON).headers("X-Auth-Token", token, "gameId", game, "turnId", 1 ).get(URL + "getRecentTurns").then().assertThat().statusCode(200);

    }
}
