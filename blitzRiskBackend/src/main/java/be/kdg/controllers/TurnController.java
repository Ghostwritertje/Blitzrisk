package be.kdg.controllers;

import be.kdg.exceptions.IllegalMoveException;
import be.kdg.model.*;
import be.kdg.security.TokenUtils;
import be.kdg.services.*;
import be.kdg.wrappers.MoveWrapper;
import be.kdg.wrappers.UpdatedTerritoriesWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * Created by Marlies on 27/02/2015.
 */

@RestController
public class TurnController {

    static Logger log = Logger.getLogger(TurnController.class.getName());

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
            return new ResponseEntity<>(-1,HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(turnService.createTurn(Integer.parseInt(playerId)).getId(),HttpStatus.CREATED);
    }

    @RequestMapping(value = "/numberOfReinforcements", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> numberOfReinforcements(@RequestHeader("X-Auth-Token") String token, @RequestHeader("playerId") String playerId) {
        User user = userService.getUser(TokenUtils.getUserNameFromToken(token));
        if (!playerService.isPlayerOfUser(user, Integer.parseInt(playerId))) {
            return new ResponseEntity<>(-1,HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(turnService.calculateNumberOfReinforcements(playerId), HttpStatus.OK);
    }

    @RequestMapping(value = "/reinforce", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<List<UpdatedTerritoriesWrapper>> reinforce(@RequestHeader("X-Auth-Token") String token, @RequestHeader("playerId") String playerId, @RequestBody List<MoveWrapper> moveWrappers) throws IllegalMoveException{
        List <UpdatedTerritoriesWrapper> newMoveWrappers = new ArrayList<>();
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
    public ResponseEntity<List<UpdatedTerritoriesWrapper>> attack(@RequestHeader("X-Auth-Token") String token, @RequestHeader("playerId") String playerId, @RequestBody List<MoveWrapper> moveWrappers) throws IllegalMoveException{
        List<Move> moves = getMoves(moveWrappers);
        Player player = playerService.getPlayer(Integer.parseInt(playerId));
        turnService.attack(moves.get(0).getTurn(), moves, player);
        List<UpdatedTerritoriesWrapper> updatedTerritories = getUpdatedTerritories(moves);
        return new ResponseEntity<>(updatedTerritories, HttpStatus.OK);
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
            move.setNumberOfUnitsToAttack(moveWrapper.getUnits());
            move.setTurn(turn);
            moves.add(move);
        }
        return moves;
    }

    public List<UpdatedTerritoriesWrapper> getUpdatedTerritories(List<Move> moves) {
        List <UpdatedTerritoriesWrapper> updatedTerritories = new ArrayList<>();
        for (Move move: moves) {
            UpdatedTerritoriesWrapper updatedTerritory = new UpdatedTerritoriesWrapper(move.getOriginTerritory());
            updatedTerritories.add(updatedTerritory);
            if(!move.getOriginTerritory().getId().equals(move.getDestinationTerritory().getId())){
                updatedTerritory = new UpdatedTerritoriesWrapper(move.getDestinationTerritory());
                updatedTerritories.add(updatedTerritory);
            }

        }
        return updatedTerritories;
    }


}
