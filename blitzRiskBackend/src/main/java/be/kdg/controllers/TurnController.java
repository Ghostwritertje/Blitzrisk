package be.kdg.controllers;

import be.kdg.exceptions.GameAlreadyOverException;
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
    private AttackService attackService;
    @Autowired
    private ReinforceService reinforceService;
    @Autowired
    private MoveUnitsService moveUnitsService;
    @Autowired
    private TerritoryService territoryService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/player/{playerId}/createTurn", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> createTurn(@RequestHeader("X-Auth-Token") String token,
                                              @PathVariable("playerId") String playerId) {

            User user = userService.getUser(TokenUtils.getUserNameFromToken(token));
            if (!playerService.isPlayerOfUser(user, Integer.parseInt(playerId))) {
                return new ResponseEntity<>(-1, HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(turnService.createTurn(Integer.parseInt(playerId)).getId(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/player/{playerId}/numberOfReinforcements", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> numberOfReinforcements(@RequestHeader("X-Auth-Token") String token,
                                                          @PathVariable("playerId") String playerId) {

        User user = userService.getUser(TokenUtils.getUserNameFromToken(token));
        if (!playerService.isPlayerOfUser(user, Integer.parseInt(playerId))) {
            return new ResponseEntity<>(-1, HttpStatus.FORBIDDEN);
        }

        Player player = playerService.getPlayerById(Integer.parseInt(playerId));

        return new ResponseEntity<>(reinforceService.calculateNumberOfReinforcements(player), HttpStatus.OK);
    }

    @RequestMapping(value = "/player/{playerId}/getPlayerStatus", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getPlayerStatus(@RequestHeader("X-Auth-Token") String token,
                                                  @PathVariable("playerId") String playerId){

        User user = userService.getUser(TokenUtils.getUserNameFromToken(token));
        if (!playerService.isPlayerOfUser(user, Integer.parseInt(playerId))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Player player = playerService.getPlayerById(Integer.parseInt(playerId));
        PlayerStatus playerStatus = playerService.getPlayerStatus(player);
        return new ResponseEntity<>(playerStatus.toString(), HttpStatus.OK);//TODO geeft soms een nullpointer.
    }

    @RequestMapping(value = "/player/{playerId}/getTurnId", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> getTurnId(@RequestHeader("X-Auth-Token") String token,
                                             @PathVariable("playerId") String playerId){

        User user = userService.getUser(TokenUtils.getUserNameFromToken(token));
        if (!playerService.isPlayerOfUser(user, Integer.parseInt(playerId))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Player player = playerService.getPlayerById(Integer.parseInt(playerId));
        try {
            Turn turn = turnService.getTurn(player);
            return new ResponseEntity<>(turn.getId(), HttpStatus.OK);
        }
        catch (IllegalMoveException e) {
            return new ResponseEntity<>(-1, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/player/{playerId}/skipAttack", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> skipAttack(@RequestHeader("X-Auth-Token") java.lang.String token,
                                             @PathVariable("playerId") java.lang.String playerId){

        User user = userService.getUser(TokenUtils.getUserNameFromToken(token));
        if (!playerService.isPlayerOfUser(user, Integer.parseInt(playerId))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Player player = playerService.getPlayerById(Integer.parseInt(playerId));
            try {
                turnService.setPlayerTurn(player, PlayerStatus.MOVE);
                return new ResponseEntity<>(player.getPlayerStatus().toString(), HttpStatus.OK);
            } catch (IllegalMoveException e) {
                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
            }
    }

    @RequestMapping(value = "/player/{playerId}/skipMove", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> skipMove(@RequestHeader("X-Auth-Token") java.lang.String token,
                                           @PathVariable("playerId") java.lang.String playerId){

        Player player = playerService.getPlayerById(Integer.parseInt(playerId));

        User user = userService.getUser(TokenUtils.getUserNameFromToken(token));
        if (!playerService.isPlayerOfUser(user, Integer.parseInt(playerId))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            turnService.setPlayerTurn(player, PlayerStatus.WAITING);
            return new ResponseEntity<>(player.getPlayerStatus().toString(), HttpStatus.OK);
        } catch (IllegalMoveException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @RequestMapping(value = "/game/{gameId}/getRecentTurns/turn/{turnId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<TurnWrapper>> getRecentTurns(@RequestHeader("X-Auth-Token") String token,
                                                            @PathVariable("gameId") String gameId,
                                                            @PathVariable ("turnId") String turnId){

        User user = userService.getUser(TokenUtils.getUserNameFromToken(token));
        Game game = gameService.getGame(Integer.parseInt(gameId));
        if (!gameService.isGameOfUser(user, game)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        int turnNumber = turnService.getTurnNumber(Integer.parseInt(turnId));
        List<Turn> turns = turnService.getRecentTurns(Integer.parseInt(gameId), turnNumber);
        List<TurnWrapper> turnWrappers = new ArrayList<>();
        for(Turn turn: turns) {
            turnWrappers.add(new TurnWrapper(turn));
        }
        return new ResponseEntity<>(turnWrappers ,HttpStatus.OK);
    }

    @RequestMapping(value = "/player/{playerId}/reinforce", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<List<MoveWrapper>> reinforce(@RequestHeader("X-Auth-Token") String token,
                                                       @PathVariable("playerId") String playerId,
                                                       @RequestBody List<MoveWrapper> moveWrappers){

        User user = userService.getUser(TokenUtils.getUserNameFromToken(token));
        if (!playerService.isPlayerOfUser(user, Integer.parseInt(playerId))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Player player = playerService.getPlayerById(Integer.parseInt(playerId));
        log.warn(moveWrappers.size());
        List<Move> moves = getMoves(moveWrappers);
        try {
            reinforceService.reinforce(moves.get(0).getTurn(), player, moves);
        }
        catch (IllegalTurnException  | IllegalMoveException | GameAlreadyOverException e) {
            log.warn(e.getMessage());
            log.warn(e.getCause());
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        List <MoveWrapper> newMoveWrappers = getUpdatedTerritories(moves);
        return new ResponseEntity<>(newMoveWrappers ,HttpStatus.OK);
    }

    @RequestMapping(value = "/player/{playerId}/attack", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<List<MoveWrapper>> attack(@RequestHeader("X-Auth-Token") String token,
                                                    @PathVariable("playerId") String playerId,
                                                    @RequestBody List<MoveWrapper> moveWrappers) {

        User user = userService.getUser(TokenUtils.getUserNameFromToken(token));
        if (!playerService.isPlayerOfUser(user, Integer.parseInt(playerId))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        log.warn("MoveWrappers.size: " + moveWrappers.size());
        List<Move> moves = getMoves(moveWrappers);
        Player player = playerService.getPlayerById(Integer.parseInt(playerId));
        try {
            attackService.attack(moves.get(0).getTurn(), moves, player);
        }
        catch (IllegalTurnException | IllegalMoveException | GameAlreadyOverException e) {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        List<MoveWrapper> updatedMoves = getUpdatedTerritories(moves);
        return new ResponseEntity<>(updatedMoves, HttpStatus.OK);
    }

    @RequestMapping(value = "/player/{playerId}/moveUnits", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<List<MoveWrapper>> moveUnits(@RequestHeader("X-Auth-Token") String token,
                                                       @PathVariable("playerId") String playerId,
                                                       @RequestBody List<MoveWrapper> moveWrappers) {

        User user = userService.getUser(TokenUtils.getUserNameFromToken(token));
        if (!playerService.isPlayerOfUser(user, Integer.parseInt(playerId))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Move> moves = getMoves(moveWrappers);
        Player player = playerService.getPlayerById(Integer.parseInt(playerId));
        try {
            moveUnitsService.moveUnits(moves.get(0).getTurn(), player, moves);
        }
        catch (IllegalTurnException | IllegalMoveException | GameAlreadyOverException e) {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
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