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

/**
 * Attack service provides a service for attacking territories
 */
@Transactional
@Service("attackService")
public class AttackService {

    static Logger log = Logger.getLogger(TurnService.class);

    @Autowired
    private TurnService turnService;

    public Turn attack(Turn turn,List<Move> moveList, Player player) throws IllegalMoveException, IllegalTurnException, GameAlreadyOverException {
        checkAttack(turn, player, moveList);
        List<Move> calculatedMoves = calculateAttack(moveList);
        turnService.setPlayerTurn(player, PlayerStatus.MOVE);
        turnService.updateTurnAfterMove(turn, calculatedMoves);
        Turn t = turnService.getTurn(turn.getId());
        for(Move move: t.getMoves()) {
            try {
                log.warn(move.getId());
                log.warn(move.getOriginTerritoryRemainingNrUnits());
            }catch (Exception e) {
                log.warn(e.getStackTrace());
            }
        }
        return turnService.getTurn(turn.getId());

    }

    private void checkAttack(Turn turn, Player player, List<Move> moves) throws IllegalTurnException, IllegalMoveException, GameAlreadyOverException{
        if(player.getGame().isEnded()) {
            log.warn("game has already ended");
            throw new GameAlreadyOverException();
        }
        turnService.playerOnTurnCheck(turn, player);
        for (Move move : moves) {
            if (!move.getOriginTerritory().getPlayer().getId().equals(player.getId())) {
                log.warn("error: illegal origin territory");
                throw new IllegalMoveException("Illegal origin territory");
            }
            boolean isNeighbour = false;
            log.warn("Neighbour size: " + move.getOriginTerritory().getNeighbourTerritories().size());
            for(Territory territory: move.getOriginTerritory().getNeighbourTerritories()) {
                if (territory.getId().equals(move.getDestinationTerritory().getId())) isNeighbour = true;
            }

            if (!isNeighbour) {
                log.warn("destination is not a neighbour");
                throw new IllegalMoveException("Destination is not a neighbour");
            }

            if (move.getDestinationTerritory().getPlayer().getId().equals(player.getId())) {
                log.warn("error: can't attack own territory");
                throw new IllegalMoveException("Can't attack own territory");
            }

            if (move.getOriginTerritory().getNumberOfUnits() - move.getNumberOfUnitsToAttack() < 1) {
                log.warn("error: not enough units to attack");
                throw new IllegalMoveException("Not enough units to attack");
            }
        }
    }

    private List<Move> calculateAttack(List<Move> moves) throws IllegalMoveException{
        List<Move> calculatedMoves = new ArrayList<>();
        for(Move move: moves) {
            try {
                int attackers = move.getNumberOfUnitsToAttack();
                int defenders = move.getDestinationTerritory().getNumberOfUnits();
                int survivingAttacckers = attackers;
                int survivingDefenders = defenders;
                int originTerritoryStartingNrUnits = move.getOriginTerritory().getNumberOfUnits();
                log.warn("origin starting number of units" + originTerritoryStartingNrUnits);
                log.warn("number of units to attack " + attackers);
                int originTerritoryRemainingNrUnits = originTerritoryStartingNrUnits - attackers;
                int destinationTerritoryStartingNrUnits = move.getDestinationTerritory().getNumberOfUnits();

                move.setOriginTerritoryStartingNrUnits(originTerritoryStartingNrUnits);
                move.setDestinationTerritoryStartingNrUnits(destinationTerritoryStartingNrUnits);

                //attackers have a 60% survival rate, defenders have a 70% survival rate
                for (int i = 0; i < attackers; i++) {
                    if (Math.random() < 0.7) {
                        survivingAttacckers -= 1;
                    }
                }

                for (int i = 0; i < defenders; i++) {
                    if (Math.random() < 0.6) {
                        survivingDefenders -= 1;
                    }
                }

                if(survivingAttacckers > survivingDefenders)  {
                    survivingAttacckers -= survivingDefenders;
                    survivingDefenders = 0;
                }


                //if attacker has won
                if (survivingDefenders <= 0 && survivingAttacckers > 0) {
                    //surviving attackers will occupy the destination territory
                    move.getDestinationTerritory().setPlayer(move.getOriginTerritory().getPlayer());
                    move.getDestinationTerritory().setNumberOfUnits(survivingAttacckers);
                    move.setDestinationTerritoryRemainingNrUnits(survivingAttacckers);

                    //updated number of units in origin country
                    move.setOriginTerritoryRemainingNrUnits(originTerritoryRemainingNrUnits);
                    move.getOriginTerritory().setNumberOfUnits(originTerritoryRemainingNrUnits);
                } else if(survivingDefenders > 0) {
                    //number of units in both territories will be reduced
                    move.getDestinationTerritory().setNumberOfUnits(survivingDefenders);
                    move.setDestinationTerritoryRemainingNrUnits(survivingDefenders);
                    move.getOriginTerritory().setNumberOfUnits(survivingAttacckers +  originTerritoryRemainingNrUnits);
                    move.setOriginTerritoryRemainingNrUnits(survivingAttacckers + originTerritoryRemainingNrUnits);
                }
                else {
                    move.getDestinationTerritory().setNumberOfUnits(0);
                    move.setOriginTerritoryRemainingNrUnits(0);
                    move.getDestinationTerritory().setPlayer(null);
                    move.setOriginTerritoryRemainingNrUnits(originTerritoryRemainingNrUnits);
                    move.getOriginTerritory().setNumberOfUnits(originTerritoryRemainingNrUnits);
                }
                log.warn("" + move.getDestinationTerritory().getNumberOfUnits());
                log.warn("" + move.getOriginTerritory().getNumberOfUnits());

                calculatedMoves.add(move);
            } catch (Exception e) {
                log.warn("something went wrong when calculating the attack:" + e.getCause());
                throw new IllegalMoveException("invalid move was inserted");
            }
        }
        return calculatedMoves;
    }



}