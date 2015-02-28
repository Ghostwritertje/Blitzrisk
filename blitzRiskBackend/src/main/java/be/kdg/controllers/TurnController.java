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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Marlies on 27/02/2015.
 */

@RestController
public class TurnController {

    @Autowired
    private TurnService turnService;
    @Autowired
    private TerritoryService territoryService;
    @Autowired
    private PlayerService playerService;

    @RequestMapping(value = "/createTurn", method = RequestMethod.GET, produces = "application/text")
    public String createTurn(@RequestHeader("X-Auth-Token") String token, @RequestHeader("playerId") String playerId) {
        return "" +  turnService.createTurn(Integer.parseInt(playerId));
    }

    @RequestMapping(value = "/numberOfReinforcements", method = RequestMethod.GET, produces = "application/text")
    public String numberOfReinforcements(@RequestHeader("X-Auth-Token") String token, @RequestHeader("playerId") String playerId) {
        return "" +  turnService.calculateNumberOfReinforcements(playerId);
    }

    @RequestMapping(value = "/reinforce", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public List<MoveWrapper> reinforce(@RequestHeader("X-Auth-Token") String token, @RequestHeader("playerId") String playerId, @RequestBody List<MoveWrapper> moveWrappers) {
        Player player = playerService.getPlayer(Integer.parseInt(playerId));
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
        }
        try {
            turnService.addReinforcements(moves.get(0).getTurn(), player, moves);
        }
        catch (IllegalMoveException illegalMove) {
            return null;
        }

        List <MoveWrapper> newMoveWrappers = new ArrayList<>();
        for (Move move: moves) {
            MoveWrapper moveWrapper = new MoveWrapper(move);
            newMoveWrappers.add(moveWrapper);
        }
        return newMoveWrappers;
    }


}
