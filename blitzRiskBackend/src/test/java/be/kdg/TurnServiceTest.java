package be.kdg;

import be.kdg.exceptions.IllegalMoveException;
import be.kdg.model.*;
import be.kdg.services.GameService;
import be.kdg.services.TurnService;
import be.kdg.services.UserService;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;

/**
 * Created by Marlies on 22/02/2015.
 */

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dispatcher.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TurnServiceTest {
    private @Mock Game game;
    private @Mock List<Player> players;
    private @Mock Player player1;
    private @Mock Player player2;
    private @Mock Player player3;
    private @Mock Territory origin;
    private @Mock Territory destination;

    @Autowired
    private TurnService turnService;

    @Before
    public void createGame() {
        MockitoAnnotations.initMocks(this);
        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        game.setPlayers(players);

        /*when(user.getEmail()).thenReturn("user1@test.be").thenReturn("user2@test.be").thenReturn("user3@test.be");
        when(user.getName()).thenReturn("user1").thenReturn("user2").thenReturn("user3");
        when(user.getPassword()).thenReturn("user1").thenReturn("user2").thenReturn("user3");*/

    }

    @Test(expected = IllegalMoveException.class)
    public void attackFromForeignCountry() throws IllegalMoveException{
        when(origin.getPlayer()).thenReturn(player1);
        when(origin.getNumberOfUnits()).thenReturn(2);
        when(destination.getPlayer()).thenReturn(player2);
        when(destination.getNumberOfUnits()).thenReturn(1);
        origin.addNeighbour(destination);
        destination.addNeighbour(origin);

        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);

        turnService.createTurn(game, player3, moves);

    }

    @Test(expected = IllegalMoveException.class)
    public void attackWithTooManyUnits() throws IllegalMoveException {
        when(origin.getPlayer()).thenReturn(player1);
        when(origin.getNumberOfUnits()).thenReturn(2);
        when(destination.getPlayer()).thenReturn(player2);
        when(destination.getNumberOfUnits()).thenReturn(1);
        origin.addNeighbour(destination);
        destination.addNeighbour(origin);

        Move move = new Move();
        move.setNumberOfUnitsToAttack(2);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);

        turnService.createTurn(game, player1, moves);
    }

    @Test
    public void someUnitsDied() throws IllegalMoveException {
        int count = 0;

        when(origin.getPlayer()).thenReturn(player1);
        when(origin.getNumberOfUnits()).thenReturn(40);
        when(destination.getPlayer()).thenReturn(player2);
        when(destination.getNumberOfUnits()).thenReturn(40);
        origin.addNeighbour(destination);
        destination.addNeighbour(origin);

        Move move = new Move();
        move.setNumberOfUnitsToAttack(35);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);

        for(int i = 0; i< 5; i++) {
            turnService.createTurn(game, player1, moves);
            if (move.getDestinationTerritoryRemainingNrUnits() == 40 || move.getOriginTerritoryRemainingNrUnits() == 40) {
                count++;
            }
        }
        Assert.assertFalse("Not all units should survive", count>4);

    }

    @Test
    public void someUnitsSurvived() throws IllegalMoveException {
        int count = 0;

        when(origin.getPlayer()).thenReturn(player1);
        when(origin.getNumberOfUnits()).thenReturn(40);
        when(destination.getPlayer()).thenReturn(player2);
        when(destination.getNumberOfUnits()).thenReturn(40);
        origin.addNeighbour(destination);
        destination.addNeighbour(origin);

        Move move = new Move();
        move.setNumberOfUnitsToAttack(35);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);

        for(int i = 0; i< 5; i++) {
            turnService.createTurn(game, player1, moves);
            if (move.getDestinationTerritoryRemainingNrUnits() == 0 || move.getOriginTerritoryRemainingNrUnits() < 2) {
                count++;
            }
        }
        Assert.assertFalse("Not all units should survive", count>4);
    }

    @Test
    public void differentResult() throws IllegalMoveException{
        int defendersCount = 0;
        int attackersCount = 0;

        when(origin.getPlayer()).thenReturn(player1);
        when(origin.getNumberOfUnits()).thenReturn(2);
        when(destination.getPlayer()).thenReturn(player2);
        when(destination.getNumberOfUnits()).thenReturn(1);
        origin.addNeighbour(destination);
        destination.addNeighbour(origin);

        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);

        for(int i = 0; i< 50; i++) {
            turnService.createTurn(game, player1, moves);
            if(move.getDestinationTerritory().getPlayer() == player1) attackersCount++;
            else defendersCount++;
        }
        Assert.assertFalse("Player shouldn't always win", attackersCount==10);
        Assert.assertFalse("Player shouldn't always lose", defendersCount==10);
    }


}
