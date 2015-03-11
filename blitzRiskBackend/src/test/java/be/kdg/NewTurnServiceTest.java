package be.kdg;

import be.kdg.exceptions.IllegalMoveException;
import be.kdg.exceptions.IllegalTurnException;
import be.kdg.exceptions.IllegalUserInviteException;
import be.kdg.model.*;
import be.kdg.services.*;
import junit.framework.Assert;
import org.junit.After;
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
 * Created by Marlies on 10/03/2015.
 */

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dispatcher.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class NewTurnServiceTest {
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
    @Autowired
    private AttackService attackService;
    @Autowired
    private ReinforceService reinforceService;
    @Autowired
    private MoveUnitsService moveUnitsService;

    private List<Player> players;
    private int game;
    private Territory origin;
    private Territory destination;

    @Before
    public void setUp() throws IllegalUserInviteException{
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
            playerService.updatePlayer(player);
        }


        origin = new Territory();
        destination = new Territory();
        destination.addNeighbour(origin);
        origin.addNeighbour(destination);
        List<Territory> territories = new ArrayList<>();
        territories.add(origin);
        territories.add(destination);
        gameService.saveTerritories(gameObject, territories);
        origin.setPlayer(players.get(0));
        origin.setNumberOfUnits(3);
        origin.setGameKey(1);
        territoryService.updateTerritory(origin);
        destination.setPlayer(players.get(1));
        destination.setNumberOfUnits(1);
        destination.setGameKey(2);
        territoryService.updateTerritory(destination);

    }

    @After
    public void cleanUp() {
        Game gameObject = gameService.getGame(game);
        turnService.removeTurns(gameObject);

        territoryService.removeTerritory(origin);
        territoryService.removeTerritory(destination);

        for(Player player: players) {
            playerService.removePlayer(player);
        }
        gameService.removeGame(gameService.getGame(game));
        userService.removeUser("turntestgameuser");
        userService.removeUser("turntestgameuser2");
    }

    @Test
    public void attack() throws IllegalMoveException, IllegalTurnException{
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.ATTACK);
        playerService.updatePlayer(player);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);

        List<Move> moves = new ArrayList<>();
        moves.add(move);

        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        move.setTurn(turn);
        turn = attackService.attack(turn, moves, player);
        Assert.assertTrue("turn isn't valid", turn.getCalculatedMoves().size() == 1);
    }

    @Test(expected = IllegalMoveException.class)
    public void attackWithWrongStatus() throws IllegalMoveException, IllegalTurnException {
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.WAITING);
        playerService.updatePlayer(player);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(origin);
        move.setOriginTerritory(destination);
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        move.setTurn(turn);
        attackService.attack(turn, moves, player);
    }

    @Test(expected = IllegalTurnException.class)
    public void AttackWhenNotOnTurn() throws  IllegalMoveException, IllegalTurnException {
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.ATTACK);
        playerService.updatePlayer(player);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        move.setTurn(turn);
        attackService.attack(turn, moves, players.get(1));
    }

    @Test(expected = IllegalMoveException.class)
    public void attackFromForeignCountry() throws IllegalMoveException, IllegalTurnException {
        Player player = players.get(1);
        player.setPlayerStatus(PlayerStatus.ATTACK);
        playerService.updatePlayer(player);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        move.setTurn(turn);
        attackService.attack(turn, moves, player);
    }

    /*@Test(expected = IllegalMoveException.class)
    public void isNoNeighbourTest() throws IllegalMoveException, IllegalTurnException {
        Set <Territory> emptyTerritories = new HashSet<>();
        origin.setNeighbourTerritories(emptyTerritories);
        destination.setNeighbourTerritories(emptyTerritories);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(game, players.get(0));
        turn = attackService.attack(turn, moves, players.get(0));
    }*/

    @Test(expected = IllegalMoveException.class)
    public void attackWithTooManyUnits() throws IllegalMoveException, IllegalTurnException {
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.ATTACK);
        playerService.updatePlayer(player);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(5);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        move.setTurn(turn);
        attackService.attack(turn, moves, player);
    }

    @Test
    public void someUnitsDied() throws IllegalMoveException, IllegalTurnException {
        int count = 0;
        Move move = new Move();
        move.setNumberOfUnitsToAttack(35);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);

        for(int i = 0; i< 5; i++) {
            Player player = players.get(0);
            player.setPlayerStatus(PlayerStatus.ATTACK);
            playerService.updatePlayer(player);
            origin.setNumberOfUnits(40);
            destination.setNumberOfUnits(40);
            Turn turn = turnService.createTurn(gameService.getGame(game), player);
            move.setTurn(turn);
            attackService.attack(turn, moves, player);
            if (move.getDestinationTerritoryRemainingNrUnits() == 40 || move.getOriginTerritoryRemainingNrUnits() == 40) {
                count++;
            }
        }
        Assert.assertFalse("Not all units should survive", count>4);
    }

    @Test
    public void someUnitsSurvived() throws IllegalMoveException, IllegalTurnException {
        int count = 0;

        Move move = new Move();
        move.setNumberOfUnitsToAttack(35);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);

        for(int i = 0; i< 5; i++) {
            Player player = players.get(0);
            player.setPlayerStatus(PlayerStatus.ATTACK);
            playerService.updatePlayer(player);
            origin.setNumberOfUnits(40);
            destination.setNumberOfUnits(40);
            Turn turn = turnService.createTurn(gameService.getGame(game), player);
            move.setTurn(turn);
            attackService.attack(turn, moves, player);
            if (move.getDestinationTerritoryRemainingNrUnits() == 0 || move.getOriginTerritoryRemainingNrUnits() < 2) {
                count++;
            }
        }
        Assert.assertFalse("Not all units should survive", count > 4);
    }

    @Test
    public void differentResult() throws IllegalMoveException, IllegalTurnException {
        int defendersCount = 0;
        int attackersCount = 0;

        for(int i = 0; i< 20; i++) {
            Player player = players.get(0);
            player.setPlayerStatus(PlayerStatus.ATTACK);
            origin.setPlayer(player);
            destination.setPlayer(players.get(1));
            origin.setNumberOfUnits(2);
            destination.setNumberOfUnits(1);
            Move move = new Move();
            move.setNumberOfUnitsToAttack(1);
            move.setDestinationTerritory(destination);
            move.setOriginTerritory(origin);
            List<Move> moves = new ArrayList<>();
            moves.add(move);
            Turn turn = turnService.createTurn(gameService.getGame(game), player);
            move.setTurn(turn);
            attackService.attack(turn, moves, player);
            if(move.getDestinationTerritory().getPlayer() == player) attackersCount++;
            else defendersCount++;
        }

        Assert.assertFalse("Player shouldn't always win", attackersCount == 20);
        Assert.assertFalse("Player shouldn't always lose", defendersCount == 20);
    }

    @Test
    public void reinforceWithFewTerritories() throws IllegalMoveException, IllegalTurnException {
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.REINFORCE);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(3);
        move.setDestinationTerritory(origin);
        move.setOriginTerritory(origin);
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        move.setTurn(turn);
        reinforceService.reinforce(turn, player, reinforcements);
        Assert.assertTrue("player should have 5 units", origin.getNumberOfUnits() == 6);
    }

    @Test(expected = IllegalMoveException.class)
    public void reinforceWithWrongStatus() throws IllegalMoveException, IllegalTurnException {
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.ATTACK);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(3);
        move.setDestinationTerritory(origin);
        move.setOriginTerritory(origin);
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        move.setTurn(turn);
        reinforceService.reinforce(turn, player, reinforcements);
    }

    @Test(expected = IllegalTurnException.class)
    public void reinforceWhenNotOnTurn() throws IllegalMoveException, IllegalTurnException {
        Player player = players.get(1);
        player.setPlayerStatus(PlayerStatus.REINFORCE);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(3);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(destination);
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), players.get(0));
        move.setTurn(turn);
        reinforceService.reinforce(turn, player, reinforcements);
    }

    @Test
    public void reinforceWithManyTerritories() throws IllegalMoveException, IllegalTurnException {
        HashSet playerTerritories = new HashSet();
        playerTerritories.add(origin);
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.REINFORCE);
        List <Territory>territories = new ArrayList<>();
        territories.add(origin);
        territories.add(destination);
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
        move.setDestinationTerritory(origin);
        move.setOriginTerritory(origin);
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        move.setTurn(turn);
        reinforceService.reinforce(turn, player, reinforcements);
        Assert.assertTrue("Origin should have 7 units", origin.getNumberOfUnits() == 7);
    }

    @Test(expected = IllegalMoveException.class)
    public void reinforceTooManyUnits() throws IllegalMoveException, IllegalTurnException {
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.REINFORCE);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(5);
        move.setDestinationTerritory(origin);
        move.setOriginTerritory(origin);
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        move.setTurn(turn);
        reinforceService.reinforce(turn, player, reinforcements);
    }

    @Test(expected = IllegalMoveException.class)
    public void reinforceForeignTerritory() throws IllegalMoveException, IllegalTurnException {
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.REINFORCE);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(3);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(destination);
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        move.setTurn(turn);
        reinforceService.reinforce(turn, players.get(0), reinforcements);

    }

    @Test(expected = IllegalMoveException.class)
    public void wrongMoveSyntax() throws IllegalMoveException, IllegalTurnException {
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.REINFORCE);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(3);
        move.setDestinationTerritory(origin);
        move.setOriginTerritory(destination);
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        move.setTurn(turn);
        reinforceService.reinforce(turn, player, reinforcements);
    }

    @Test
    public void getExtraUnitsWithFullContinent() throws IllegalMoveException, IllegalTurnException {
        Set<Territory> fullTerritories = territoryService.getTerritories();
        Set<Territory> subSelectTerritories = new HashSet<>();
        Player player = new Player();
        for (Territory territory : fullTerritories){
            if (territory.getGameKey() < 10) subSelectTerritories.add(territory);
        }
        player.setTerritories(subSelectTerritories);

        Assert.assertEquals("Player should have 8 units", 8, reinforceService.calculateNumberOfReinforcements(player));
    }

    @Test
    public void testPlayerStatusAfterAttack() throws IllegalMoveException, IllegalTurnException {
        players.get(0).setPlayerStatus(PlayerStatus.ATTACK);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), players.get(0));
        move.setTurn(turn);
        attackService.attack(turn, moves, players.get(0));
        Assert.assertTrue("playerstatus isn't move", players.get(0).getPlayerStatus().equals(PlayerStatus.MOVE));
    }

    @Test
    public void testPlayerStatusAfterReinforce() throws IllegalMoveException, IllegalTurnException {
        players.get(0).setPlayerStatus(PlayerStatus.REINFORCE);
        Move move = new Move();
        move.setNumberOfUnitsToAttack(3);
        move.setDestinationTerritory(origin);
        move.setOriginTerritory(origin);
        List<Move> reinforcements = new ArrayList<>();
        reinforcements.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), players.get(0));
        move.setTurn(turn);
        reinforceService.reinforce(turn, players.get(0), reinforcements);
        Assert.assertTrue("playerstatus isn't move", players.get(0).getPlayerStatus().equals(PlayerStatus.ATTACK));
    }

    @Test
    public void testPlayerStatusAfterMove() throws IllegalMoveException, IllegalTurnException {
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.MOVE);
        /*Territory origin = origin;
        origin.setNumberOfUnits(3);
        territoryService.updateTerritory(origin);
        Territory destination = destination;*/
        destination.setPlayer(player);
        territoryService.updateTerritory(destination);

        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        move.setTurn(turn);
        moveUnitsService.moveUnits(turn, player, moves);
        Assert.assertTrue("playerstatus isn't waiting", players.get(0).getPlayerStatus().equals(PlayerStatus.WAITING));
        Assert.assertTrue("new player should have status reinforce", players.get(1).getPlayerStatus().equals(PlayerStatus.REINFORCE));
    }

    @Test
    public void move() throws IllegalMoveException, IllegalTurnException {
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.MOVE);
        /*Territory origin = origin;
        origin.setNumberOfUnits(3);
        territoryService.updateTerritory(origin);
        Territory destination = destination;*/
        destination.setPlayer(player);
        territoryService.updateTerritory(destination);


        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        move.setTurn(turn);
        moveUnitsService.moveUnits(turn, player, moves);
        Assert.assertTrue("origin should have 2 units", origin.getNumberOfUnits() == 2);
        Assert.assertTrue("destination should have 2 units", destination.getNumberOfUnits() == 2);
    }

    @Test(expected = IllegalTurnException.class)
    public void moveWhenNotOnTurn() throws IllegalMoveException, IllegalTurnException {
        Player player = players.get(1);
        player.setPlayerStatus(PlayerStatus.MOVE);
        Territory origin = destination;
        origin.setNumberOfUnits(3);
        territoryService.updateTerritory(origin);
        Territory destination = origin;
        destination.setPlayer(player);
        territoryService.updateTerritory(destination);

        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), players.get(0));
        move.setTurn(turn);
        moveUnitsService.moveUnits(turn, player, moves);
        Assert.assertTrue("origin should have 2 units", origin.getNumberOfUnits() == 2);
        Assert.assertTrue("destination should have 2 units", destination.getNumberOfUnits() == 2);
    }

    @Test(expected = IllegalMoveException.class)
    public void moveFromEnemyTerritory() throws IllegalMoveException, IllegalTurnException {
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.MOVE);
        Territory origin = destination;
        origin.setNumberOfUnits(3);
        territoryService.updateTerritory(origin);
        Territory destination = origin;

        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        move.setTurn(turn);
        moveUnitsService.moveUnits(turn, player, moves);
    }

    @Test(expected = IllegalMoveException.class)
    public void moveToEnemyTerritory() throws IllegalMoveException, IllegalTurnException {
        Player player = players.get(0);
        player.setPlayerStatus(PlayerStatus.MOVE);
        /*Territory origin = origin;
        origin.setNumberOfUnits(3);
        territoryService.updateTerritory(origin);
        Territory destination = destination;*/

        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        move.setTurn(turn);
        moveUnitsService.moveUnits(turn, player, moves);
    }

    @Test(expected = IllegalMoveException.class)
    public void moveWithWrongPlayer() throws IllegalMoveException, IllegalTurnException {
        Player player = players.get(1);
        player.setPlayerStatus(PlayerStatus.MOVE);
        /*Territory origin = origin;
        origin.setNumberOfUnits(3);
        territoryService.updateTerritory(origin);
        Territory destination = destination;*/
        destination.setPlayer(players.get(0));
        territoryService.updateTerritory(destination);

        Move move = new Move();
        move.setNumberOfUnitsToAttack(1);
        move.setDestinationTerritory(destination);
        move.setOriginTerritory(origin);
        List<Move> moves = new ArrayList<>();
        moves.add(move);
        Turn turn = turnService.createTurn(gameService.getGame(game), player);
        move.setTurn(turn);
        moveUnitsService.moveUnits(turn, player, moves);
        Assert.assertTrue("origin should have 2 units", origin.getNumberOfUnits() == 2);
        Assert.assertTrue("destination should have 2 units", destination.getNumberOfUnits() == 2);
    }
}
