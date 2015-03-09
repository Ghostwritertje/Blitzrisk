package be.kdg;

import be.kdg.dao.*;
import be.kdg.exceptions.IllegalMoveException;
import be.kdg.model.*;
import be.kdg.services.TerritoryService;
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
    @Autowired
    private TerritoryService territoryService;

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
        origin.setGameKey(1);

        Territory destination = new Territory();
        territoryId.set(destination, 2);
        destination.setNumberOfUnits(1);
        destination.setPlayer(players.get(1));
        destination.addNeighbour(origin);
        destination.setGameKey(2);
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
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.ATTACK);
        playerDao.savePlayer(player);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(territories.get(1));
        move.setOriginTerritory(territories.get(0));
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(game, player);
        turn = turnService.attack(turn, moves, player);
        Assert.assertTrue("turn isn't valid", turn.getCalculatedMoves().size() == 1);
    }

    @Test
    public void attackWithWrongStatus() throws IllegalMoveException {
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.WAITING);
        playerDao.savePlayer(player);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(territories.get(1));
        move.setOriginTerritory(territories.get(0));
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(game, player);
        turn = turnService.attack(turn, moves, player);
    }

    @Test(expected = IllegalMoveException.class)
    public void wrongTurn() throws  IllegalMoveException {
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.ATTACK);
        playerDao.savePlayer(player);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(territories.get(1));
        move.setOriginTerritory(territories.get(0));
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(game, player);
        turn = turnService.attack(turn, moves, players.get(1));
    }

    @Test(expected = IllegalMoveException.class)
    public void attackFromForeignCountry() throws IllegalMoveException{
        Player player = players.get(2);
        player.setPlayerStatus(PlayerStatus.ATTACK);
        playerDao.savePlayer(player);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(territories.get(1));
        move.setOriginTerritory(territories.get(0));
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(game, player);
        turn = turnService.attack(turn, moves, player);
    }

    /*@Test(expected = IllegalMoveException.class)
    public void isNoNeighbourTest() throws IllegalMoveException {
        Set <Territory> emptyTerritories = new HashSet<>();
        territories.get(0).setNeighbourTerritories(emptyTerritories);
        territories.get(1).setNeighbourTerritories(emptyTerritories);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(territories.get(1));
        move.setOriginTerritory(territories.get(0));
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(game, players.get(0));
        turn = turnService.attack(turn, moves, players.get(0));
    }*/

    @Test(expected = IllegalMoveException.class)
    public void attackWithTooManyUnits() throws IllegalMoveException {
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.ATTACK);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(2);
        move.setDestinationTerritory(territories.get(1));
        move.setOriginTerritory(territories.get(0));
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(game, player);
        turn = turnService.attack(turn, moves, player);
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
            Player player = players.get(0);
            player.setPlayerStatus(PlayerStatus.ATTACK);
            playerDao.savePlayer(player);
            territories.get(0).setNumberOfUnits(40);
            territories.get(1).setNumberOfUnits(40);
            Turn turn = turnService.createTurn(game, player);
            turn = turnService.attack(turn, moves, player);
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
            Player player = players.get(0);
            player.setPlayerStatus(PlayerStatus.ATTACK);
            playerDao.savePlayer(player);
            territories.get(0).setNumberOfUnits(40);
            territories.get(1).setNumberOfUnits(40);
            Turn turn = turnService.createTurn(game, player);
            turn = turnService.attack(turn, moves, player);
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
            Player player = players.get(0);
            player.setPlayerStatus(PlayerStatus.ATTACK);
            territories.get(0).setPlayer(player);
            territories.get(1).setPlayer(players.get(1));
            territories.get(0).setNumberOfUnits(2);
            territories.get(1).setNumberOfUnits(1);
            Move move = new Move();
            move.setNumberOfUnitsToAttack(1);
            move.setDestinationTerritory(territories.get(1));
            move.setOriginTerritory(territories.get(0));
            List<Move> moves = new ArrayList<>();
            moves.add(move);
            Turn turn = turnService.createTurn(game, player);
            turn = turnService.attack(turn, moves, player);
            if(move.getDestinationTerritory().getPlayer() == player) attackersCount++;
            else defendersCount++;
        }

        Assert.assertFalse("Player shouldn't always win", attackersCount==20);
        Assert.assertFalse("Player shouldn't always lose", defendersCount==20);
    }

    @Test
    public void reinforceWithFewTerritories() throws IllegalMoveException{
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.REINFORCE);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(3);
        move.setDestinationTerritory(territories.get(0));
        move.setOriginTerritory(territories.get(0));
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(game, player);
        turnService.addReinforcements(turn, player, reinforcements);
        Assert.assertTrue("player should have 5 units", territories.get(0).getNumberOfUnits() == 5);
    }

    @Test(expected = IllegalMoveException.class)
    public void reinforceWithWrongStatus() throws IllegalMoveException {
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.ATTACK);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(3);
        move.setDestinationTerritory(territories.get(0));
        move.setOriginTerritory(territories.get(0));
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(game, player);
        turnService.addReinforcements(turn, player, reinforcements);
    }

    @Test
    public void reinforceWithManyTerritories() throws IllegalMoveException {
        HashSet playerTerritories = new HashSet();
        playerTerritories.add(territories.get(0));
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.REINFORCE);
        for (int i = 0; i < 12; i++) {
            Territory territory = new Territory();
            territory.setPlayer(players.get(0));
            territory.setNumberOfUnits(1);
            territory.setGameKey(i+3);
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
        Turn turn = turnService.createTurn(game, player);
        turnService.addReinforcements(turn, player, reinforcements);
        Assert.assertTrue("Origin should have 6 units", territories.get(0).getNumberOfUnits() == 6);
    }

    @Test(expected = IllegalMoveException.class)
    public void reinforceTooManyUnits() throws IllegalMoveException{
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.REINFORCE);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(5);
        move.setDestinationTerritory(territories.get(0));
        move.setOriginTerritory(territories.get(0));
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(game, player);
        turnService.addReinforcements(turn, player, reinforcements);
    }

    @Test(expected = IllegalMoveException.class)
    public void reinforceForeignTerritory() throws IllegalMoveException{
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.REINFORCE);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(3);
        move.setDestinationTerritory(territories.get(0));
        move.setOriginTerritory(territories.get(0));
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(game, player);
        turnService.addReinforcements(turn, players.get(1), reinforcements);
    }

    @Test(expected = IllegalMoveException.class)
    public void wrongMoveSyntax() throws IllegalMoveException{
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.REINFORCE);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(3);
        move.setDestinationTerritory(territories.get(0));
        move.setOriginTerritory(territories.get(1));
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(game, player);
        turnService.addReinforcements(turn, player, reinforcements);
    }

    @Test
    public void getExtraUnitsWithFullContinent() throws IllegalMoveException {
        Set<Territory> fullTerritories = territoryService.getTerritories();
        Set<Territory> subSelectTerritories = new HashSet<>();
        Player player = new Player();
        for (Territory territory : fullTerritories){
            if (territory.getGameKey() < 10) subSelectTerritories.add(territory);
        }
        player.setTerritories(subSelectTerritories);

        Assert.assertEquals("Player should have 8 units", 8, turnService.calculateNumberOfReinforcements(player));
    }

    @Test
    public void testPlayerStatusAfterAttack() throws IllegalMoveException{
        players.get(0).setPlayerStatus(PlayerStatus.ATTACK);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(territories.get(1));
        move.setOriginTerritory(territories.get(0));
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(game, players.get(0));
        turnService.attack(turn, moves, players.get(0));
        Assert.assertTrue("playerstatus isn't move", players.get(0).getPlayerStatus().equals(PlayerStatus.MOVE));
    }

    @Test
    public void testPlayerStatusAfterReinforce() throws IllegalMoveException{
        players.get(0).setPlayerStatus(PlayerStatus.REINFORCE);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(3);
        move.setDestinationTerritory(territories.get(0));
        move.setOriginTerritory(territories.get(0));
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(game, players.get(0));
        turnService.addReinforcements(turn, players.get(0), reinforcements);
        Assert.assertTrue("playerstatus isn't move", players.get(0).getPlayerStatus().equals(PlayerStatus.ATTACK));
    }

    @Test
    public void testPlayerStatusAfterMove() throws IllegalMoveException{
        players.get(0).setPlayerStatus(PlayerStatus.MOVE);
        turnService.moveUnits(players.get(0));
        Assert.assertTrue("playerstatus isn't waiting", players.get(0).getPlayerStatus().equals(PlayerStatus.WAITING));
        Assert.assertTrue("new player should have status reinforce", players.get(1).getPlayerStatus().equals(PlayerStatus.REINFORCE));
    }
}
