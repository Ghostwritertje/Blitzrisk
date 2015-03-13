package be.kdg.services;

import be.kdg.dao.*;
import be.kdg.exceptions.GameAlreadyOverException;
import be.kdg.exceptions.IllegalMoveException;
import be.kdg.exceptions.IllegalTurnException;
import be.kdg.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Marlies on 10/03/2015.
 */

@Service("reinforceService")
public class ReinforceService {
    static Logger log = Logger.getLogger(TurnService.class);

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private TurnService turnService;

    public void reinforce(Turn turn, Player player, List<Move> moves) throws IllegalMoveException, IllegalTurnException, GameAlreadyOverException {
        turnService.playerOnTurnCheck(turn, player);
        checkReinforcements(turn, player, moves);
        List<Move> calculatedMoves = addReinforcements(moves);
        turnService.setPlayerTurn(player, PlayerStatus.ATTACK);
        turnService.updateTurnAfterMove(turn, calculatedMoves);
    }

    public int calculateNumberOfReinforcements(Player player) {
        double territoriesNo = (double) player.getTerritories().size();
        double nrOfUnits = Math.ceil(territoriesNo/3);

        Set<Territory> territorySet = player.getTerritories();

        int northAmCounter = 0;
        int southAmCounter = 0;
        int europeCounter = 0;
        int africaCounter = 0;
        int asiaCounter = 0;
        int australiaCounter = 0;

        for (Territory territory : territorySet){
            if (territory.getGameKey() < 10) northAmCounter++;
            if (territory.getGameKey() < 14 && territory.getGameKey() > 9) southAmCounter++;
            if (territory.getGameKey() < 21 && territory.getGameKey() > 13) europeCounter++;
            if (territory.getGameKey() < 27 && territory.getGameKey() > 20) africaCounter++;
            if (territory.getGameKey() < 39 && territory.getGameKey() > 26) asiaCounter++;
            if (territory.getGameKey() < 43 && territory.getGameKey() > 38) australiaCounter++;
        }
        if (northAmCounter == 9) nrOfUnits += 5;
        if (southAmCounter == 4) nrOfUnits += 2;
        if (europeCounter == 7) nrOfUnits += 5;
        if (africaCounter == 6) nrOfUnits += 3;
        if (asiaCounter == 12) nrOfUnits += 7;
        if (australiaCounter == 4) nrOfUnits += 2;

        if (nrOfUnits < 3) return 3;
        else return (int) nrOfUnits;
    }

    private void checkReinforcements(Turn turn, Player player, List<Move> moves) throws IllegalMoveException, IllegalTurnException, GameAlreadyOverException {
        if(player.getGame().isEnded()) throw new GameAlreadyOverException();
        turnService.playerOnTurnCheck(turn, player);
        for(Move move: moves) {
            if (!move.getDestinationTerritory().getId().equals(move.getOriginTerritory().getId())) {
                throw new IllegalMoveException("incorrect reinforecement - origin: "
                        + move.getOriginTerritory().getId() + " - destination: "
                        + move.getDestinationTerritory().getId());
            }
        }
        int reinforcementsTotal = 0;
        for (Move move : moves) {
            if(!move.getOriginTerritory().getPlayer().getId().equals(player.getId())) {
                throw new IllegalMoveException("player doesn't own the territories he wants to reinforce");
            }
            reinforcementsTotal += move.getNumberOfUnitsToAttack();
        }
        if (reinforcementsTotal >  calculateNumberOfReinforcements(player)) {
            throw new IllegalMoveException("Amount of allowed reinforcements is exceeded");
        }
    }

    private List<Move> addReinforcements(List<Move> moves) {
        for(Move move: moves) {
            Territory origin = move.getOriginTerritory();
            int newUnits = origin.getNumberOfUnits() + move.getNumberOfUnitsToAttack();
            move.getOriginTerritory().setNumberOfUnits(newUnits);
            move.setOriginTerritoryRemainingNrUnits(newUnits);
            move.getDestinationTerritory().setNumberOfUnits(newUnits);
            move.setDestinationTerritoryRemainingNrUnits(newUnits);
            origin.setNumberOfUnits(newUnits);
            log.warn("reinforcement added");
        }
        return moves;
    }
}
