package be.kdg.controllers;

import be.kdg.exceptions.IllegalMoveException;
import be.kdg.model.Move;
import be.kdg.model.Player;
import be.kdg.model.Territory;
import be.kdg.model.Turn;
import be.kdg.services.PlayerService;
import be.kdg.services.TerritoryService;
import be.kdg.services.TurnService;
import be.kdg.wrappers.MoveWrapper;
import be.kdg.wrappers.UpdatedTerritoriesWrapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/createTurn", method = RequestMethod.GET, produces = "application/text")
    public int createTurn(@RequestHeader("X-Auth-Token") String token, @RequestHeader("playerId") String playerId) {
        return  turnService.createTurn(Integer.parseInt(playerId)).getId();
    }

    @RequestMapping(value = "/numberOfReinforcements", method = RequestMethod.GET, produces = "application/text")
    public String numberOfReinforcements(@RequestHeader("X-Auth-Token") String token, @RequestHeader("playerId") String playerId) {
        return "" +  turnService.calculateNumberOfReinforcements(playerId);
    }

    @RequestMapping(value = "/moveList", method = RequestMethod.GET, produces = "application/json")
    public List<MoveWrapper> moveList() {
        List<Move> moves = turnService.moves();
        List<MoveWrapper> moveWrapperList = new ArrayList<>();
        for(Move move: moves) {
            MoveWrapper moveWrapper = new MoveWrapper(move);
            moveWrapperList.add(moveWrapper);
        }
        return moveWrapperList;
    }

    @RequestMapping(value = "/reinforce", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public List<UpdatedTerritoriesWrapper> reinforce(@RequestHeader("X-Auth-Token") String token, @RequestHeader("playerId") String playerId, @RequestBody List<MoveWrapper> moveWrappers) throws IllegalMoveException{
        Player player = playerService.getPlayerById(Integer.parseInt(playerId));
        List<Move> moves = new ArrayList<>();
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
        //try {
            turnService.addReinforcements(moves.get(0).getTurn(), player, moves);
        //}
        //catch (IllegalMoveException illegalMove) {
            //log.info("illegalmove: "+illegalMove.getMessage());
            //return null;
        //}



        List <UpdatedTerritoriesWrapper> newMoveWrappers = new ArrayList<>();
        for (Move move: moves) {
            UpdatedTerritoriesWrapper moveWrapper = new UpdatedTerritoriesWrapper(move.getOriginTerritory());
            newMoveWrappers.add(moveWrapper);
        }
        return newMoveWrappers;
    }


}
