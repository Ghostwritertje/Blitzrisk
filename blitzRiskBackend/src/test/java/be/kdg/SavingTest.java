package be.kdg;

import be.kdg.dao.UserDao;
import be.kdg.exceptions.IllegalMoveException;
import be.kdg.model.*;
import be.kdg.services.*;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Marlies on 27/02/2015.
 */

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dispatcher.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SavingTest {

    @Autowired
    private TurnService turnService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

    @Autowired
    private TerritoryService territoryService;

    @Test
    public void saveReinforcement() throws IllegalMoveException{
        userService.addUser("test", "test", "test@test.be");
        User user = userService.getUser("test");

        Game game = gameService.createNewGame();
        gameService.addUserToGame(user, game);

        Player player = game.getPlayers().get(0);

        Territory territory = new Territory();
        territory.setGame(game);
        territory.setPlayer(player);
        territory.setNumberOfUnits(1);
        List territoryList = new ArrayList<>();
        territoryList.add(territory);
        Set territorySet = new HashSet<>();
        territorySet.add(territory);
        player.setTerritories(territorySet);
        game.setTerritories(territoryList);

        playerService.save(player);



        Move move = new Move();
        move.setNumberOfUnitsToAttack(3);
        move.setDestinationTerritory(territory);
        move.setOriginTerritory(territory);
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(game, player);
        turnService.addReinforcements(turn, player, reinforcements);
        Assert.assertTrue("player should have 4 units", territory.getNumberOfUnits() == 4);
    }
}
