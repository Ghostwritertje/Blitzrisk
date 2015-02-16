package be.kdg.services;

import be.kdg.exceptions.IllegalMoveException;
import be.kdg.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Alexander on 13/2/2015.
 */
@Service("turnService")
public class TurnService {

    public Turn createTurn(Game game, Player player,List<Move> moveList) {

        Turn turn = new Turn();
        turn.setMoves(moveList);
        turn.setPlayer(player);
        game.addTurn(turn);

        return turn;
    }

    public Turn executedTurn(Turn turn) throws IllegalMoveException {

        Player player = turn.getPlayer();
        for (Move move : turn.getMoves()) {
            if(!move.getOriginTerritory().getPlayer().equals(player)) {
                throw new IllegalMoveException("Illegal origin territory");
            }

            boolean isNeighbour = false;
            for(Territory territory : move.getOriginTerritory().getNeighbourTerritories()) {
                if(territory.equals(move.getDestinationTerritory())) isNeighbour = true;
            }

            if (!isNeighbour) throw new IllegalMoveException("Destination is not a neighbour");

            if (move.getDestinationTerritory().getPlayer().equals(player)) throw new IllegalMoveException("Can't attack own territory");

            if (move.getOriginTerritory().getNumberOfUnits()<2) throw new IllegalMoveException("Too few units to attack");

            Move calculatedMove = calculateMove(move);




        }
        //TODO: denken over replay !!
        return  new Turn();
    }

    public Move calculateMove(Move move) {

        int attackers = move.getNumberOfUnits();
        int defenders = move.getDestinationTerritory().getNumberOfUnits();
        int attackingDeaths = 0;
        int defendingDeaths = 0;

        for (int i = 0; i<attackers; i++) {
            if (Math.random()<0.7){
                attackingDeaths+=1;
            }
        }

        for (int i = 0; i<defenders; i++) {
            if (Math.random()<0.6){
                defendingDeaths+=1;
            }
        }



        //als verdedigend land veroverd is
        if (defenders-defendingDeaths == 0) {
            move.getDestinationTerritory().setPlayer(move.getOriginTerritory().getPlayer());
            move.getDestinationTerritory().setNumberOfUnits(attackers-attackingDeaths);
            move.getOriginTerritory().setNumberOfUnits(move.getOriginTerritory().getNumberOfUnits()-attackers);
        }
        else
        {
            move.getDestinationTerritory().setNumberOfUnits(defenders-defendingDeaths);
            move.getOriginTerritory().setNumberOfUnits(move.getNumberOfUnits()-attackingDeaths);
            move.setNumberOfUnits(0);
        }

        return move;
    }
}
