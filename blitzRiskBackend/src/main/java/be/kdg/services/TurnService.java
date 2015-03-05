package be.kdg.services;

import be.kdg.dao.*;
import be.kdg.exceptions.IllegalMoveException;
import be.kdg.model.*;
import be.kdg.wrappers.MoveWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 13/2/2015.
 */
@Transactional
@Service("turnService")
public class TurnService {

    @Autowired
    private MoveDao moveDao;

    @Autowired
    private TurnDao turnDao;

    @Autowired
    private TerritoryDao territoryDao;

    @Autowired
    private PlayerDao playerDao;

    public void removeTurns(Game game) {
        for(Turn turn: game.getTurns()) {
            for(Move move: turn.getMoves()) {
                moveDao.removeMove(move);
            }
            turnDao.removeTurn(turn);
        }
    }

    public Turn getTurn(int turnId) {
        return turnDao.getTurnById(turnId);
    }

    public Turn createTurn(int playerId) {
        Player player = playerDao.getPlayerById(playerId);
        Game game = player.getGame();
        return createTurn(game, player);
    }

    public List<Move> moves() {
        return moveDao.findall();
    }

    public Turn createTurn(Game game, Player player) {
        Turn turn = new Turn();
        turn.setGame(game);
        turn.setPlayer(player);
        turnDao.updateTurn(turn);
        return turn;
    }

    public Turn attack(Turn turn,List<Move> moveList, Player player) throws IllegalMoveException {
        if(!player.getId().equals(turn.getPlayer().getId())) throw new IllegalMoveException("wrong turn");
        executeTurn(turn, moveList);
        turn.setCalculatedMoves(moveList);
        turnDao.updateTurn(turn);
        for (Move move: moveList) {
            moveDao.updateMove(move);
            territoryDao.updateTerritory(move.getOriginTerritory());
            territoryDao.updateTerritory(move.getDestinationTerritory());
        }
        return turn;
    }

    private void executeTurn(Turn turn, List<Move> moveList) throws IllegalMoveException {

        Player player = turn.getPlayer();
        for (Move move : moveList) {
            if(!move.getOriginTerritory().getPlayer().getId().equals(player.getId())) {
                throw new IllegalMoveException("Illegal origin territory");
            }
            //TODO: nog niet getest?
           /* boolean isNeighbour = false;
            for(Territory territory : move.getOriginTerritory().getNeighbourTerritories()) {
                if(territory.getId().equals(move.getDestinationTerritory().getId())) isNeighbour = true;
            }

            if (!isNeighbour) throw new IllegalMoveException("Destination is not a neighbour");
*/
            if (move.getDestinationTerritory().getPlayer().getId().equals(player.getId())) throw new IllegalMoveException("Can't attack own territory");

            if (move.getOriginTerritory().getNumberOfUnits() - move.getNumberOfUnitsToAttack() < 1) throw new IllegalMoveException("Not enough units to attack");

            calculateMove(move);
            turn.getMoves().add(move);
        }
    }

    private Move calculateMove(Move move) {

        int attackers = move.getNumberOfUnitsToAttack();
        int defenders = move.getDestinationTerritory().getNumberOfUnits();
        int survivingAttacckers = attackers;
        int survivingDefenders = defenders;
        int originTerritoryStartingNrUnits = move.getOriginTerritory().getNumberOfUnits();
        int originTerritoryRemainingNrUnits;
        int destinationTerritoryStartingNrUnits = move.getDestinationTerritory().getNumberOfUnits();

        move.setOriginTerritoryStartingNrUnits(originTerritoryStartingNrUnits);
        move.setDestinationTerritoryStartingNrUnits(destinationTerritoryStartingNrUnits);

        //attackers have a 60% survival rate, defenders have a 70% survival rate
        for (int i = 0; i<attackers; i++) {
            if (Math.random()<0.7){
                survivingAttacckers-=1;
            }
        }

        for (int i = 0; i<defenders; i++) {
            if (Math.random()<0.6){
                survivingDefenders-=1;
            }
        }


        //if attacker has won
        if (survivingDefenders <= 0) {
            //surviving attackers will occupy the destination territory
            originTerritoryRemainingNrUnits = originTerritoryStartingNrUnits-attackers;
            move.getDestinationTerritory().setPlayer(move.getOriginTerritory().getPlayer());
            move.getDestinationTerritory().setNumberOfUnits(survivingAttacckers);
            move.setDestinationTerritoryRemainingNrUnits(survivingAttacckers);

            //updated number of units in origin country
            move.setOriginTerritoryRemainingNrUnits(originTerritoryRemainingNrUnits);
            move.getOriginTerritory().setNumberOfUnits(originTerritoryRemainingNrUnits);
        }
        else
        {
            //number of units in both territories will be reduced
            move.getDestinationTerritory().setNumberOfUnits(survivingDefenders);
            move.setDestinationTerritoryRemainingNrUnits(survivingDefenders);
            move.getOriginTerritory().setNumberOfUnits(survivingAttacckers);
            move.setOriginTerritoryRemainingNrUnits(survivingAttacckers);
        }

        return move;
    }

    public int calculateNumberOfReinforcements(Player player) {
        double territoriesNo = (double) player.getTerritories().size();
        territoriesNo = Math.ceil(territoriesNo/3);
        if (territoriesNo < 3) return 3;
        else return (int) territoriesNo;
    }

    public int calculateNumberOfReinforcements(String playerIdStr) {
        int playerId = Integer.parseInt(playerIdStr);
        Player player = playerDao.getPlayerById(playerId);
        return calculateNumberOfReinforcements(player);
    }

    public void addReinforcements(Turn turn, Player player, List<Move> moves) throws IllegalMoveException{
        for(Move move: moves) {
            if (!move.getDestinationTerritory().getId().equals(move.getOriginTerritory().getId())) throw new IllegalMoveException("incorrect reinforecement - origin: " + move.getOriginTerritory().getId() + " - destination: " + move.getDestinationTerritory().getId());
        }
        int reinforcementsTotal = 0;
        for (Move move : moves) {
            if(!move.getOriginTerritory().getPlayer().getId().equals(player.getId())) throw new IllegalMoveException("player doesn't own the territories he wants to reinforce");
            reinforcementsTotal += move.getNumberOfUnitsToAttack();
        }
        if (reinforcementsTotal >  calculateNumberOfReinforcements(player)) throw new IllegalMoveException("Amount of allowed reinforcements is exceeded");


        for(Move move : moves){
            int numberOfUnits = move.getNumberOfUnitsToAttack() + move.getOriginTerritory().getNumberOfUnits();
            move.getOriginTerritory().setNumberOfUnits(numberOfUnits);
            move.setTurn(turn);
            territoryDao.updateTerritory(move.getOriginTerritory());
            moveDao.updateMove(move);
        }
        turn.setCalculatedMoves(moves);
        turnDao.updateTurn(turn);
    }
}
