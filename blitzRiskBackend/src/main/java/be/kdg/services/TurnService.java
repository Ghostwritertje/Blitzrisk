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

    public void excecuteTurn(Turn turn) throws IllegalMoveException {
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
        }

    }
}
