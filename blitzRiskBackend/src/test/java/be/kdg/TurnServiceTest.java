package be.kdg;

import be.kdg.exceptions.IllegalMoveException;
import be.kdg.model.Game;
import be.kdg.model.Move;
import be.kdg.model.Territory;
import be.kdg.model.User;
import be.kdg.services.GameService;
import be.kdg.services.TurnService;
import be.kdg.services.UserManagerService;
import org.junit.Before;
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
 * Created by Marlies on 22/02/2015.
 */

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dispatcher.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TurnServiceTest {
    private Game game;
    private List<User> users;

    @Autowired
    private UserManagerService userManagerService;
    @Autowired
    private GameService gameService;
    @Autowired
    private TurnService turnService;

    @Before
    public void createGame() {
        userManagerService.addUser("Lyle Collins", "lyle", "lyle@collins.be");
        userManagerService.addUser("Margaret Crawford", "margaret", "margaret@crawford.be");
        userManagerService.addUser("Lorenzo Jones", "lorenzo", "lorenzo@jones.be");
        userManagerService.addUser("Guadalupe Howard", "guadalupe", "gaudalupe@howard.be");
        users = new ArrayList<>();
        users.add(userManagerService.getUser("Lyle Collins"));
        users.add(userManagerService.getUser("Margaret Crawford"));
        users.add(userManagerService.getUser("Lorenzo Jones"));
        users.add(userManagerService.getUser("Guadalupe Howard"));
        game = gameService.createNewGame(users);
    }

    @Test(expected = IllegalMoveException.class)
    public void attackFromForeignCountry() {
        /*HashSet territorySet = ((HashSet<Territory>)game.getTerritories());
        Territory attackingTerritory = territorySet
        attackingTerritory.setPlayer(game.getPlayers().get(0));
        Move move = new Move();
        move.setOriginTerritory(attackingTerritory);
        move.setDestinationTerritory(((Set<Territory>) game.getTerritories()).get(1));
        move.setNumberOfUnitsToAttack(1);
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        turnService.createTurn(game, game.getPlayers().get(3), moves);*/
    }
}
