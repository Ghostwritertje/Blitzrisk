package be.kdg.controllers;

import be.kdg.exceptions.IllegalMoveException;
import be.kdg.exceptions.IllegalTurnException;
import be.kdg.model.*;
import be.kdg.security.TokenUtils;
import be.kdg.services.*;
import be.kdg.wrappers.MoveWrapper;
import be.kdg.wrappers.TurnWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;


/**
 * Created by Marlies on 27/02/2015.
 */

@RestController
public class TurnController {

    static Logger log = Logger.getLogger(TurnController.class);

    @Autowired
    private TurnService turnService;
    @Autowired
    private TerritoryService territoryService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/createTurn", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> createTurn(@RequestHeader("X-Auth-Token") String token, @RequestHeader("playerId") String playerId) {

            User user = userService.getUser(TokenUtils.getUserNameFromToken(token));
            if (!playerService.isPlayerOfUser(user, Integer.parseInt(playerId))) {
                return new ResponseEntity<>(-1, HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(turnService.createTurn(Integer.parseInt(playerId)).getId(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/numberOfReinforcements", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> numberOfReinforcements(@RequestHeader("X-Auth-Token") String token, @RequestHeader("playerId") String playerId) {
        User user = userService.getUser(TokenUtils.getUserNameFromToken(token));
        if (!playerService.isPlayerOfUser(user, Integer.parseInt(playerId))) {
            return new ResponseEntity<>(-1,HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(turnService.calculateNumberOfReinforcements(playerId), HttpStatus.OK);
    }

    @RequestMapping(value = "/getPlayerStatus", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getPlayerStatus(@RequestHeader("X-Auth-Token") String token, @RequestHeader("playerId") String playerId){
        Player player = playerService.getPlayerById(Integer.parseInt(playerId));
        PlayerStatus playerStatus = playerService.getPlayerStatus(player);
        return new ResponseEntity<>(playerStatus.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/getTurnId", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> getTurnId(@RequestHeader("X-Auth-Token") String token, @RequestHeader("playerId") String playerId){
        Player player = playerService.getPlayerById(Integer.parseInt(playerId));
        try {
            Turn turn = turnService.getTurn(player);
            return new ResponseEntity<>(turn.getId(), HttpStatus.OK);
        }
        catch (IllegalMoveException e) {
            return new ResponseEntity<Integer>(-1, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/skipAttack", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> skipAttack(@RequestHeader("X-Auth-Token") java.lang.String token, @RequestHeader("playerId") java.lang.String playerId){
            Player player = playerService.getPlayerById(Integer.parseInt(playerId));
            try {
                turnService.setPlayerTurn(player, PlayerStatus.MOVE);
                return new ResponseEntity<>(player.getPlayerStatus().toString(), HttpStatus.OK);
            } catch (IllegalMoveException e) {
                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
            }
    }

    @RequestMapping(value = "/skipMove", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> skipMove(@RequestHeader("X-Auth-Token") java.lang.String token, @RequestHeader("playerId") java.lang.String playerId){
        Player player = playerService.getPlayerById(Integer.parseInt(playerId));
        try {
            turnService.setPlayerTurn(player, PlayerStatus.WAITING);
            return new ResponseEntity<>(player.getPlayerStatus().toString(), HttpStatus.OK);
        } catch (IllegalMoveException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @RequestMapping(value = "/getRecentTurns", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<TurnWrapper>> getRecentTurns(@RequestHeader("X-Auth-Token") String token, @RequestHeader("gameId") String gameId, @RequestHeader ("turnId") String turnId){
        int turnNumber = turnService.getTurnNumber(Integer.parseInt(turnId));
        List<Turn> turns = turnService.getRecentTurns(Integer.parseInt(gameId), turnNumber);
        List<TurnWrapper> turnWrappers = new ArrayList<>();
        for(Turn turn: turns) {
            turnWrappers.add(new TurnWrapper(turn));
        }
        return new ResponseEntity<>(turnWrappers ,HttpStatus.OK);
    }

    @RequestMapping(value = "/reinforce", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<List<MoveWrapper>> reinforce(@RequestHeader("X-Auth-Token") String token,
                                                       @RequestHeader("playerId") String playerId,
                                                       @RequestBody List<MoveWrapper> moveWrappers) throws IllegalMoveException, IllegalTurnException {
        List <MoveWrapper> newMoveWrappers = new ArrayList<>();
        User user = userService.getUser(TokenUtils.getUserNameFromToken(token));
        if (!playerService.isPlayerOfUser(user, Integer.parseInt(playerId))) {
            return new ResponseEntity<>(newMoveWrappers,HttpStatus.FORBIDDEN);
        }

        Player player = playerService.getPlayerById(Integer.parseInt(playerId));
        List<Move> moves = getMoves(moveWrappers);
        turnService.addReinforcements(moves.get(0).getTurn(), player, moves);
        newMoveWrappers = getUpdatedTerritories(moves);
        return new ResponseEntity<>(newMoveWrappers ,HttpStatus.OK);
    }

    @RequestMapping(value = "/attack", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<List<MoveWrapper>> attack(@RequestHeader("X-Auth-Token") String token,
                                                    @RequestHeader("playerId") String playerId,
                                                    @RequestBody List<MoveWrapper> moveWrappers) throws IllegalMoveException, IllegalTurnException {
        log.warn("attack called");
        List<Move> moves = getMoves(moveWrappers);
        Player player = playerService.getPlayerById(Integer.parseInt(playerId));
        turnService.attack(moves.get(0).getTurn(), moves, player);
        List<MoveWrapper> updatedMoves = getUpdatedTerritories(moves);
        return new ResponseEntity<>(updatedMoves, HttpStatus.OK);
    }

    @RequestMapping(value = "/moveUnits", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<List<MoveWrapper>> moveUnits(@RequestHeader("X-Auth-Token") String token,
                                                       @RequestHeader("playerId") String playerId,
                                                       @RequestBody List<MoveWrapper> moveWrappers) throws IllegalMoveException, IllegalTurnException {
        log.warn("move called");
        List<Move> moves = getMoves(moveWrappers);
        Player player = playerService.getPlayerById(Integer.parseInt(playerId));
        turnService.moveUnits(moves.get(0).getTurn(), player, moves);
        List<MoveWrapper> updatedMoves = getUpdatedTerritories(moves);
        return new ResponseEntity<>(updatedMoves, HttpStatus.OK);
    }

    public List<Move> getMoves(List<MoveWrapper> moveWrappers) {
        List <Move> moves = new ArrayList<>();
        for (MoveWrapper moveWrapper: moveWrappers) {
            Move move = new Move();
            Turn turn = turnService.getTurn(moveWrapper.getTurnId());
            Territory origin = territoryService.getTerritory(moveWrapper.getOrigin());
            Territory destination = territoryService.getTerritory(moveWrapper.getDestination());
            move.setDestinationTerritory(destination);
            move.setOriginTerritory(origin);
            move.setNumberOfUnitsToAttack(moveWrapper.getUnitsToAttackOrReinforce());
            move.setTurn(turn);
            moves.add(move);
        }
        return moves;
    }

    public List<MoveWrapper> getUpdatedTerritories(List<Move> moves) {
        List <MoveWrapper> updatedMoves = new ArrayList<>();
        for (Move move: moves) {
            MoveWrapper updatedMove = new MoveWrapper(move);
            updatedMoves.add(updatedMove);
        }
        return updatedMoves;
    }
}