package integration.api;

import be.kdg.exceptions.IllegalUserInviteException;
import be.kdg.model.*;
import be.kdg.services.*;
import integration.MyServerConfiguration;
import org.junit.After;
import org.junit.Assert;
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

            userService.addUser("testgameuser", "testuserpass", "testgameuser@test.be");
            userService.addUser("testgameuser2", "testuserpass", "testgameuser2@test.be");

            Game gameObject = gameService.createNewGame();
            game = gameObject.getId();

            gameService.addUserToGame(userService.getUser("testgameuser"), gameObject);
            gameService.addUserToGame(userService.getUser("testgameuser2"), gameObject);
            players = gameObject.getPlayers();
            for (Player player: players) {
                player.setInvitationStatus(InvitationStatus.ACCEPTED);
            }

            /*origin = territoryService.getNewTerritory();
            origin.setGame(game);
            origin.setNumberOfUnits(1);
            origin.setPlayer(players.get(0));
            origin.setGameKey(game.getId());
            destination = territoryService.getNewTerritory();
            destination.setGame(game);
            destination.setNumberOfUnits(1);
            destination.setPlayer(players.get(1));
            destination.setGameKey(game.getId());
            origin.addNeighbour(destination);
            destination.addNeighbour(origin);
            territoryService.updateTerritory(origin);
            territoryService.updateTerritory(destination);*/

        gameObject.assignRandomTerritories();
            for(Territory territory: gameObject.getTerritories()) {
                territoryService.updateTerritory(territory);
            }

            int i = 0;
            destination = null;
            boolean destinationFound = false;
            while (!destinationFound && i < gameObject.getTerritories().size()) {
                //List <Territory> territories = game.getTerritories();
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
            userService.removeUser("testgameuser");
            userService.removeUser("testgameuser2");
    }

    @Test
    public void askReinforcementNumber() {
        destination.getPlayer();
        String token = given().header("name", "testgameuser").header("password", "testuserpass").get(URL + "login").getBody().asString();
        given().header("X-Auth-Token", token, "playerId", 1).get(URL + "createTurn").then().assertThat().statusCode(201);

    }
}
