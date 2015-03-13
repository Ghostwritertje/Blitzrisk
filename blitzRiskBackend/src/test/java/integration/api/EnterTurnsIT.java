package integration.api;

import be.kdg.exceptions.DuplicateEmailException;
import be.kdg.exceptions.DuplicateUsernameException;
import be.kdg.exceptions.IllegalMoveException;
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
    public void setUp() throws IllegalUserInviteException, DuplicateEmailException, DuplicateUsernameException {

        userService.addUser("turntestgameuser", "turntestuserpass", "turntestgameuser@test.be");
        userService.addUser("turntestgameuser2", "turntestuserpass", "turntestgameuser2@test.be");

        Game gameObject = gameService.createNewGame();
        game = gameObject.getId();

        gameService.addUserToGame(userService.getUser("turntestgameuser"), gameObject);
        gameService.addUserToGame(userService.getUser("turntestgameuser2"), gameObject);
        players = gameObject.getPlayers();
        for (Player player: players) {
            player.setInvitationStatus(InvitationStatus.ACCEPTED);
            player.setPlayerStatus(PlayerStatus.WAITING);
        }
        gameService.saveTerritories(gameObject,new ArrayList<>(territoryService.getTerritories() ));



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
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token).get( URL + "player/" + origin.getPlayer().getId() + "/numberOfReinforcements")
                .then().assertThat().statusCode(200);
    }

    @Test
    public void askReinforcementNumberWrongUser() {
        turnService.createTurn(gameService.getGame(game), origin.getPlayer());
        String token = given().header("name", "turntestgameuser2").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .get(URL + "player/" + origin.getPlayer().getId() + "/numberOfReinforcements")
                .then().assertThat().statusCode(403);
    }

    @Test
    public void getPlayerStatus() {
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .get(URL + "player/" + origin.getPlayer().getId() + "/getPlayerStatus")
                .then().assertThat().statusCode(200);

    }

    @Test
    public void getPlayerStatusWrongUser() {
        String token = given().header("name", "turntestgameuser2").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .get(URL + "player/"+ origin.getPlayer().getId() + "/getPlayerStatus")
                .then().assertThat().statusCode(403);

    }

    @Test
    public void skipAttack() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game),player);
        turn.setPlayer(player);
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        turnService.setPlayerTurn(player, PlayerStatus.ATTACK);
        turnService.saveTurn(turn);
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .post(URL + "player/" + origin.getPlayer().getId() + "/skipAttack")
                .then().assertThat().statusCode(200);
    }

    @Test
    public void skipAttackWronguser() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game),player);
        turn.setPlayer(player);
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        turnService.setPlayerTurn(player, PlayerStatus.ATTACK);
        turnService.saveTurn(turn);
        String token = given().header("name", "turntestgameuser2").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .post(URL + "player/" + origin.getPlayer().getId() + "/skipAttack")
                .then().assertThat().statusCode(403);
    }

    @Test
    public void skipMove() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game),player);
        turn.setPlayer(player);
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        turnService.setPlayerTurn(player, PlayerStatus.ATTACK);
        turnService.setPlayerTurn(player, PlayerStatus.MOVE);
        turnService.saveTurn(turn);
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .post(URL + "player/" + origin.getPlayer().getId() + "/skipMove")
                .then().assertThat().statusCode(200);
    }

    @Test
    public void skipMoveWrongUser() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game),player);
        turn.setPlayer(player);
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        turnService.setPlayerTurn(player, PlayerStatus.ATTACK);
        turnService.setPlayerTurn(player, PlayerStatus.MOVE);
        turnService.saveTurn(turn);
        String token = given().header("name", "turntestgameuser2").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .post(URL + "player/" + origin.getPlayer().getId() + "/skipMove")
                .then().assertThat().statusCode(403);
    }

    @Test
    public void skipAttackWithWrongStatus() throws IllegalMoveException {
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game),player);
        turn.setPlayer(player);
        turnService.saveTurn(turn);
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .post(URL + "player/" + origin.getPlayer().getId() + "/skipAttack")
                .then().assertThat().statusCode(405);
    }

    @Test
    public void skipMoveWithWrongStatus() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        turn.setPlayer(player);
        turnService.saveTurn(turn);
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .post(URL + "player/" + origin.getPlayer().getId() + "/skipMove")
                .then().assertThat().statusCode(405);
    }

    @Test
    public void reinforce() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        turnService.saveTurn(turn);
        territoryService.updateTerritory(origin);

        String moveWrapperList = String.format("[{\"id\":1,\"turnId\":%d,\"origin\":%d,\"destination\":%d,\"units\":3,\"calculatedUnits\":0}]", turn.getId(), origin.getId(), origin.getId());
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .request().body(moveWrapperList)
                .post(URL + "player/" + origin.getPlayer().getId() + "/reinforce").then().assertThat().statusCode(200);
    }

    @Test
    public void reinforceWrongUser() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        String moveWrapperList = String.format("[{\"id\":1,\"turnId\":%d,\"origin\":%d,\"destination\":%d,\"units\":3,\"calculatedUnits\":0}]", turn.getId(), origin.getId(), origin.getId());
        String token = given().header("name", "turntestgameuser2").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .request().body(moveWrapperList)
                .post(URL + "player/" + origin.getPlayer().getId() + "/reinforce").then().assertThat().statusCode(403);
    }

    @Test
    public void reinforceWhenNotOnTurn() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        turn.setPlayer(destination.getPlayer());
        turnService.saveTurn(turn);
        String moveWrapperList = String.format("[{\"id\":1,\"turnId\":%d,\"origin\":%d,\"destination\":%d,\"units\":3,\"calculatedUnits\":0}]", turn.getId(), origin.getId(), origin.getId());
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .request().body(moveWrapperList)
                .post(URL + "player/" + origin.getPlayer().getId() + "/reinforce").then()
                .assertThat().statusCode(405);
    }

    @Test
    public void reinforceNotAllowed() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        String moveWrapperList = String.format("[{\"id\":1,\"turnId\":%d,\"origin\":%d,\"destination\":%d,\"units\":3,\"calculatedUnits\":0}]", turn.getId(), origin.getId(), destination.getId());
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .request().body(moveWrapperList)
                .post(URL + "player/" + origin.getPlayer().getId() + "/reinforce")
                .then().assertThat().statusCode(405);
    }

    @Test
    public void attack() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game),player);
        turn.setPlayer(player);
        destination.setPlayer(destination.getPlayer());
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        turnService.setPlayerTurn(player, PlayerStatus.ATTACK);
        turnService.saveTurn(turn);
        origin.setNumberOfUnits(3);
        territoryService.updateTerritory(origin);
        String moveWrapperList = String.format("[{\"id\":1,\"turnId\":%d,\"origin\":%d,\"destination\":%d,\"unitsToAttackOrReinforce\":1}]", turn.getId(), origin.getId(), destination.getId());
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .request().body(moveWrapperList)
                .post(URL + "player/" + origin.getPlayer().getId() + "/attack").then().assertThat().statusCode(200);
    }

    @Test
    public void attackWrongUser() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game),player);
        turn.setPlayer(player);
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        turnService.setPlayerTurn(player, PlayerStatus.ATTACK);
        turnService.saveTurn(turn);
        origin.setNumberOfUnits(3);
        territoryService.updateTerritory(origin);
        String moveWrapperList = String.format("[{\"id\":1,\"turnId\":%d,\"origin\":%d,\"destination\":%d,\"unitsToAttackOrReinforce\":1}]", turn.getId(), origin.getId(), destination.getId());
        String token = given().header("name", "turntestgameuser2").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .request().body(moveWrapperList)
                .post(URL + "player/" + origin.getPlayer().getId() + "/attack").then().assertThat().statusCode(403);
    }

    @Test
    public void attackWhenNotOnTurn() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game),player);
        turn.setPlayer(destination.getPlayer());
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        turnService.setPlayerTurn(player, PlayerStatus.ATTACK);
        turnService.saveTurn(turn);
        origin.setNumberOfUnits(3);
        territoryService.updateTerritory(origin);
        String moveWrapperList = String.format("[{\"id\":1,\"turnId\":%d,\"origin\":%d,\"destination\":%d,\"unitsToAttackOrReinforce\":1}]", turn.getId(), origin.getId(), destination.getId());
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .request().body(moveWrapperList)
                .post(URL + "player/" + origin.getPlayer().getId() + "/attack").then().assertThat().statusCode(405);
    }

    @Test
    public void attackNotAllowed() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game),player);
        turn.setPlayer(player);
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        turnService.setPlayerTurn(player, PlayerStatus.ATTACK);
        turnService.saveTurn(turn);
        origin.setNumberOfUnits(3);
        territoryService.updateTerritory(origin);
        String moveWrapperList = String.format("[{\"id\":1,\"turnId\":%d,\"origin\":%d,\"destination\":%d,\"unitsToAttackOrReinforce\":1}]", turn.getId(), destination.getId(), destination.getId());
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .request().body(moveWrapperList)
                .post(URL + "player/" + origin.getPlayer().getId() + "/attack").then().assertThat().statusCode(405);
    }

    @Test
    public void moveUnits() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game),player);
        turn.setPlayer(player);
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        turnService.setPlayerTurn(player, PlayerStatus.ATTACK);
        turnService.setPlayerTurn(player, PlayerStatus.MOVE);
        turnService.saveTurn(turn);
        origin.setNumberOfUnits(3);
        territoryService.updateTerritory(origin);
        destination.setPlayer(player);
        territoryService.updateTerritory(destination);
        String moveWrapperList = String.format("[{\"id\":1,\"turnId\":%d,\"origin\":%d,\"destination\":%d,\"unitsToAttackOrReinforce\":1}]", turn.getId(), origin.getId(), destination.getId());
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .request().body(moveWrapperList)
                .post(URL + "player/" + origin.getPlayer().getId() + "/moveUnits").then().assertThat().statusCode(200);
    }

    @Test
    public void moveUnitsWrongUser() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game),player);
        turn.setPlayer(player);
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        turnService.setPlayerTurn(player, PlayerStatus.ATTACK);
        turnService.setPlayerTurn(player, PlayerStatus.MOVE);
        turnService.saveTurn(turn);
        origin.setNumberOfUnits(3);
        territoryService.updateTerritory(origin);
        destination.setPlayer(player);
        territoryService.updateTerritory(destination);
        String moveWrapperList = String.format("[{\"id\":1,\"turnId\":%d,\"origin\":%d,\"destination\":%d,\"unitsToAttackOrReinforce\":1}]", turn.getId(), origin.getId(), destination.getId());
        String token = given().header("name", "turntestgameuser2").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .request().body(moveWrapperList)
                .post(URL + "player/" + origin.getPlayer().getId() + "/moveUnits").then().assertThat().statusCode(403);
    }

    @Test
    public void moveWhenNotOnTurn() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game),player);
        turn.setPlayer(destination.getPlayer());
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        turnService.setPlayerTurn(player, PlayerStatus.ATTACK);
        turnService.setPlayerTurn(player, PlayerStatus.MOVE);
        turnService.saveTurn(turn);
        origin.setNumberOfUnits(3);
        territoryService.updateTerritory(origin);
        destination.setPlayer(player);
        territoryService.updateTerritory(destination);
        String moveWrapperList = String.format("[{\"id\":1,\"turnId\":%d,\"origin\":%d,\"destination\":%d,\"unitsToAttackOrReinforce\":1}]", turn.getId(), origin.getId(), destination.getId());
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .request().body(moveWrapperList)
                .post(URL + "player/" + origin.getPlayer().getId() + "/moveUnits").then().assertThat().statusCode(405);
    }

    @Test
    public void moveNotAllowed() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game),player);
        turn.setPlayer(player);
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        turnService.setPlayerTurn(player, PlayerStatus.ATTACK);
        turnService.setPlayerTurn(player, PlayerStatus.MOVE);
        turnService.saveTurn(turn);
        origin.setNumberOfUnits(3);
        territoryService.updateTerritory(origin);
        String moveWrapperList = String.format("[{\"id\":1,\"turnId\":%d,\"origin\":%d,\"destination\":%d,\"unitsToAttackOrReinforce\":1}]", turn.getId(), origin.getId(), destination.getId());
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON)
                .headers("X-Auth-Token", token)
                .request().body(moveWrapperList)
                .post(URL + "player/" + origin.getPlayer().getId() + "/moveUnits").then().assertThat().statusCode(405);
    }

    @Test
    public void recentTurns() {
        int turnId = 0;
        for (int i = 0; i< 3; i++) {
            Turn turn = turnService.createTurn(gameService.getGame(game), origin.getPlayer());
            turn.setPlayer(origin.getPlayer());
            turnService.saveTurn(turn);
            if(i == 1) turnId = turn.getId();
        }

        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON).headers("X-Auth-Token", token)
                .get(URL + "game/" + game + "/getRecentTurns" + "/turn/" + turnId )
                .then().assertThat().statusCode(200);

    }

    @Test
    public void recentTurnsWrongUser() throws DuplicateEmailException, DuplicateUsernameException {
        int turnId = 0;
        for (int i = 0; i< 3; i++) {
            Turn turn = turnService.createTurn(gameService.getGame(game), origin.getPlayer());
            turn.setPlayer(origin.getPlayer());
            turnService.saveTurn(turn);
            if(i == 1) turnId = turn.getId();
        }

        userService.addUser("wrongUser", "wrongUser", "wrongUser@test.be");
        String token = given().header("name", "wrongUser").header("password", "wrongUser").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON).headers("X-Auth-Token", token)
                .get(URL + "game/" + game + "/getRecentTurns" + "/turn/" + turnId)
                .then().assertThat().statusCode(403);
        userService.removeUser("wrongUser");
    }

    @Test
    public void getTurnId() throws IllegalMoveException{
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game),player);
        turn.setPlayer(player);
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        turnService.saveTurn(turn);
        String token = given().header("name", "turntestgameuser").header("password", "turntestuserpass").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON).headers("X-Auth-Token", token)
                .get(URL + "player/" + origin.getPlayer().getId() + "/getTurnId")
                .then().assertThat().statusCode(200);
    }

    @Test
    public void getTurnIdWrongUser() throws IllegalMoveException, DuplicateEmailException, DuplicateUsernameException {
        Player player = origin.getPlayer();
        Turn turn = turnService.createTurn(gameService.getGame(game),player);
        turn.setPlayer(player);
        turnService.setPlayerTurn(player, PlayerStatus.REINFORCE);
        turnService.saveTurn(turn);
        userService.addUser("wrongUser", "wrongUser", "wrongUser@test.be");
        String token = given().header("name", "wrongUser").header("password", "wrongUser").get(URL + "login").getBody().asString();
        given().contentType(ContentType.JSON).headers("X-Auth-Token", token)
                .get(URL + "player/" + origin.getPlayer().getId() + "/getTurnId")
                .then().assertThat().statusCode(403);
        userService.removeUser("wrongUser");
    }
}
