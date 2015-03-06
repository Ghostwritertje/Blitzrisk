package be.kdg;

import be.kdg.dao.*;
import be.kdg.exceptions.IllegalMoveException;
import be.kdg.model.*;
import be.kdg.services.TurnService;
import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

/**
 * Created by Marlies on 22/02/2015.
 */

@ContextConfiguration(locations = {"file:src/test/resources/testcontext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TurnServiceTest {
    private @Mock Game game;
    private List<Player> players;
    private List<Territory> territories;
    private @Mock SessionFactory sessionFactory;
    private @Mock Session session;
    private @Mock Query query;
    private @Mock User user;

    @Autowired
    private TerritoryDao territoryDao;
    @Autowired
    private TurnDao turnDao;
    @Autowired
    private MoveDao moveDao;
    @Autowired
    private GameDao gameDao;
    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private TurnService turnService;

    @Before
    public void createGame() throws IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        territoryDao.setSessionFactory(sessionFactory);
        turnDao.setSessionFactory(sessionFactory);
        moveDao.setSessionFactory(sessionFactory);
        gameDao.setSessionFactory(sessionFactory);
        playerDao.setSessionFactory(sessionFactory);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(game.getPlayerTurn()).thenReturn(0);


        players = new ArrayList<>();
        for (int i = 0 ; i<3 ; i++) {
            Player player = new Player();
            player.setGame(game);
            players.add(player);

            Class playerClass = player.getClass();
            List<Field> fields = Lists.newArrayList(playerClass.getDeclaredFields());
            Field playerId = fields.get(0);
            playerId.setAccessible(true);
            playerId.set(player, i + 1);

        }
        when(game.getPlayers()).thenReturn(players);

        Territory origin = new Territory();
        Class territoryClass = origin.getClass();
        List<Field> fields = Lists.newArrayList(territoryClass.getDeclaredFields());
        Field territoryId = fields.get(0);
        territoryId.setAccessible(true);
        territoryId.set(origin, 1);
        origin.setNumberOfUnits(2);
        origin.setPlayer(players.get(0));

        Territory destination = new Territory();
        territoryId.set(destination, 2);
        destination.setNumberOfUnits(1);
        destination.setPlayer(players.get(1));
        destination.addNeighbour(origin);
        origin.addNeighbour(destination);
        territories = new ArrayList<>();
        territories.add(origin);
        territories.add(destination);

        Set<Territory> playerTerritories = new HashSet<>();
        playerTerritories.add(origin);
        players.get(0).setTerritories(playerTerritories);
        playerTerritories = new HashSet<>();
        playerTerritories.add(destination);
        players.get(1).setTerritories(playerTerritories);
        session.saveOrUpdate(origin);
        session.saveOrUpdate(destination);

    }

    @Test
    public void attack() throws IllegalMoveException {
        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(territories.get(1));
        move.setOriginTerritory(territories.get(0));
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(game, players.get(0));
        turn = turnService.attack(turn, moves, players.get(0));
        Assert.assertTrue("turn isn't valid", turn.getCalculatedMoves().size() == 1);
    }

    @Test(expected = IllegalMoveException.class)
    public void wrongTurn() throws  IllegalMoveException {
        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(territories.get(1));
        move.setOriginTerritory(territories.get(0));
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(game, players.get(0));
        turn = turnService.attack(turn, moves, players.get(1));
    }

    @Test(expected = IllegalMoveException.class)
    public void attackFromForeignCountry() throws IllegalMoveException{
        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(territories.get(1));
        move.setOriginTerritory(territories.get(0));
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(game, players.get(2));
        turn = turnService.attack(turn, moves, players.get(2));
    }

    @Test(expected = IllegalMoveException.class)
    public void attackWithTooManyUnits() throws IllegalMoveException {
        Move move = new Move();
        move.setNumberOfUnitsToAttack(2);
        move.setDestinationTerritory(territories.get(1));
        move.setOriginTerritory(territories.get(0));
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(game, players.get(0));
        turn = turnService.attack(turn, moves, players.get(0));
    }

    @Test
    public void someUnitsDied() throws IllegalMoveException {
        int count = 0;

        Move move = new Move();
        move.setNumberOfUnitsToAttack(35);
        move.setDestinationTerritory(territories.get(1));
        move.setOriginTerritory(territories.get(0));
        List<Move> moves = new ArrayList<>();
        moves.add(move);

        for(int i = 0; i< 5; i++) {
            territories.get(0).setNumberOfUnits(40);
            territories.get(1).setNumberOfUnits(40);
            Turn turn = turnService.createTurn(game, players.get(0));
            turn = turnService.attack(turn, moves, players.get(0));
            if (move.getDestinationTerritoryRemainingNrUnits() == 40 || move.getOriginTerritoryRemainingNrUnits() == 40) {
                count++;
            }
        }
        Assert.assertFalse("Not all units should survive", count>4);
    }

    @Test
    public void someUnitsSurvived() throws IllegalMoveException {
        int count = 0;

        Move move = new Move();
        move.setNumberOfUnitsToAttack(35);
        move.setDestinationTerritory(territories.get(1));
        move.setOriginTerritory(territories.get(0));
        List<Move> moves = new ArrayList<>();
        moves.add(move);

        for(int i = 0; i< 5; i++) {
            territories.get(0).setNumberOfUnits(40);
            territories.get(1).setNumberOfUnits(40);
            Turn turn = turnService.createTurn(game, players.get(0));
            turn = turnService.attack(turn, moves, players.get(0));
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

        for(int i = 0; i< 20; i++) {
            territories.get(0).setPlayer(players.get(0));
            territories.get(1).setPlayer(players.get(1));
            territories.get(0).setNumberOfUnits(2);
            territories.get(1).setNumberOfUnits(1);
            Move move = new Move();
            move.setNumberOfUnitsToAttack(1);
            move.setDestinationTerritory(territories.get(1));
            move.setOriginTerritory(territories.get(0));
            List<Move> moves = new ArrayList<>();
            moves.add(move);
            Turn turn = turnService.createTurn(game, players.get(0));
            turn = turnService.attack(turn, moves, players.get(0));
            if(move.getDestinationTerritory().getPlayer() == players.get(0)) attackersCount++;
            else defendersCount++;
        }

        Assert.assertFalse("Player shouldn't always win", attackersCount==20);
        Assert.assertFalse("Player shouldn't always lose", defendersCount==20);
    }

    @Test
    public void reinforceWithFewTerritories() throws IllegalMoveException{
        Move move = new Move();
        move.setNumberOfUnitsToAttack(3);
        move.setDestinationTerritory(territories.get(0));
        move.setOriginTerritory(territories.get(0));
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(game, players.get(0));
        turnService.addReinforcements(turn, players.get(0), reinforcements);
        Assert.assertTrue("player should have 5 units", territories.get(0).getNumberOfUnits() == 5);
    }

    @Test
    public void reinforceWithManyTerritories() throws IllegalMoveException {
        HashSet playerTerritories = new HashSet();
        playerTerritories.add(territories.get(0));
        for (int i = 0; i < 12; i++) {
            Territory territory = new Territory();
            territory.setPlayer(players.get(0));
            territory.setNumberOfUnits(1);
            territories.add(territory);
            playerTerritories.add(territory);
        }
        players.get(0).setTerritories(playerTerritories);

        Move move = new Move();
        move.setNumberOfUnitsToAttack(4);
        move.setDestinationTerritory(territories.get(0));
        move.setOriginTerritory(territories.get(0));
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(game, players.get(0));
        turnService.addReinforcements(turn, players.get(0), reinforcements);
        Assert.assertTrue("Origin should have 6 units", territories.get(0).getNumberOfUnits() == 6);
    }

    @Test(expected = IllegalMoveException.class)
    public void reinforceTooManyUnits() throws IllegalMoveException{
        Move move = new Move();
        move.setNumberOfUnitsToAttack(5);
        move.setDestinationTerritory(territories.get(0));
        move.setOriginTerritory(territories.get(0));
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(game, players.get(0));
        turnService.addReinforcements(turn, players.get(0), reinforcements);
    }

    @Test(expected = IllegalMoveException.class)
    public void reinforceForeignTerritory() throws IllegalMoveException{
        Move move = new Move();
        move.setNumberOfUnitsToAttack(3);
        move.setDestinationTerritory(territories.get(0));
        move.setOriginTerritory(territories.get(0));
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(game, players.get(0));
        turnService.addReinforcements(turn, players.get(1), reinforcements);
    }

    @Test(expected = IllegalMoveException.class)
    public void wrongMoveSyntax() throws IllegalMoveException{
        Move move = new Move();
        move.setNumberOfUnitsToAttack(3);
        move.setDestinationTerritory(territories.get(0));
        move.setOriginTerritory(territories.get(1));
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(game, players.get(0));
        turnService.addReinforcements(turn, players.get(0), reinforcements);
    }
}
