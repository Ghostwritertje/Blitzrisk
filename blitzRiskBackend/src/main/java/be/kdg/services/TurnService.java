package be.kdg.services;

import be.kdg.exceptions.IllegalMoveException;
import be.kdg.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 13/2/2015.
 */
@Service("turnService")
public class TurnService {

    public Turn createTurn(Game game, Player player,List<Move> moveList) throws IllegalMoveException {

        Turn turn = new Turn();
        turn.setMoves(moveList);
        turn.setPlayer(player);
        game.addTurn(turn);
        executeTurn(turn);
        return turn;
    }

    public void executeTurn(Turn turn) throws IllegalMoveException {

        Player player = turn.getPlayer();
        for (Move move : turn.getMoves()) {
            if(!move.getOriginTerritory().getPlayer().equals(player)) {
                throw new IllegalMoveException("Illegal origin territory");
            }

            /*boolean isNeighbour = false;
            for(Territory territory : move.getOriginTerritory().getNeighbourTerritories()) {
                if(territory.equals(move.getDestinationTerritory())) isNeighbour = true;
            }

            if (!isNeighbour) throw new IllegalMoveException("Destination is not a neighbour");
*/
            if (move.getDestinationTerritory().getPlayer().equals(player)) throw new IllegalMoveException("Can't attack own territory");

            if (move.getOriginTerritory().getNumberOfUnits() - move.getNumberOfUnitsToAttack() < 1) throw new IllegalMoveException("Not enough units to attack");

            Move calculatedMove = calculateMove(move);
        }
    }

    public Move calculateMove(Move move) {

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

    public double calculateNumberOfReinforcements(Player player) {
        double territoriesNo = (double) player.getTerritories().size();
        territoriesNo = Math.ceil(territoriesNo/3);
        if (territoriesNo < 3) return 3;
        else return territoriesNo;
    }

    public void addReinforcements(Player player, List<Move> moves) throws IllegalMoveException{
        List <Reinforcement> reinforcements = new ArrayList<>();
        for(Move move: moves) {
            Reinforcement reinforcement = new Reinforcement();
            if (move.getDestinationTerritory().equals(move.getOriginTerritory())) throw new IllegalMoveException("incorrect reinforecement");
            reinforcement.setTerritory(move.getOriginTerritory());
            reinforcement.setNumberOfUnits(move.getNumberOfUnitsToAttack());
        }
        int reinforcementsTotal = 0;
        for (Reinforcement reinforcement : reinforcements) {
            if(!reinforcement.getTerritory().getPlayer().equals(player)) throw new IllegalMoveException("player doesn't own the territories he wants to reinforce");
            reinforcementsTotal += reinforcement.getNumberOfUnits();
        }
        if (reinforcementsTotal > (int) calculateNumberOfReinforcements(player)) throw new IllegalMoveException("Amount of allowed reinforcements is exceeded");

        for(Reinforcement reinforcement : reinforcements){
            int numberOfUnits = reinforcement.getNumberOfUnits() + reinforcement.getTerritory().getNumberOfUnits();
            reinforcement.getTerritory().setNumberOfUnits(numberOfUnits);


        }
    }

    private class Reinforcement{
        private Territory territory;
        private Integer numberOfUnits;
        private Turn turn;

        public Territory getTerritory() {
            return territory;
        }

        public void setTerritory(Territory territory) {
            this.territory = territory;
        }

        public Integer getNumberOfUnits() {
            return numberOfUnits;
        }

        public void setNumberOfUnits(Integer numberOfUnits) {
            this.numberOfUnits = numberOfUnits;
        }

        public Turn getTurn() {
            return turn;
        }

        public void setTurn(Turn turn) {
            this.turn = turn;
        }
    }
}
