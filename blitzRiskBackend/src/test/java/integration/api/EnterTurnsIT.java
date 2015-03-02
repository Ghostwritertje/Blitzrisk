package integration.api;

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
    private Game game;
    private Territory origin;
    private Territory destination;

    public void setUp() {
        try {
            userService.addUser("testgameuser", "testuserpass", "testgameuser@test.be");
            userService.addUser("testgameuser2", "testuserpass", "testgameuser2@test.be");

            game = gameService.createNewGame();

            gameService.addUserToGame(userService.getUser("testgameuser"), game);
            gameService.addUserToGame(userService.getUser("testgameuser2"), game);
            players = game.getPlayers();
            for (Player player: players) {
                player.setInvitationStatus(InvitationStatus.ACCEPTED);
            }

            origin = new Territory();
            origin.setGame(game);
            origin.setNumberOfUnits(1);
            origin.setPlayer(players.get(0));
            origin.setGameKey(game.getId());
            destination = new Territory();
            destination.setGame(game);
            destination.setNumberOfUnits(1);
            destination.setPlayer(players.get(1));
            destination.setGameKey(game.getId());
            origin.addNeighbour(destination);
            destination.addNeighbour(origin);
            territoryService.addTerritory(origin);
            territoryService.addTerritory(destination);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @After
    public void cleanUp() {
        try {
            turnService.removeTurns(game);
            territoryService.removeTerritory(origin);
            territoryService.removeTerritory(destination);
            for(Player player: players) {
                playerService.removePlayer(player);
            }
            gameService.removeGame(game);
            userService.removeUser("testgameuser");
            userService.removeUser("testgameuser2");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void askReinforcementNumber() {

    }
}
