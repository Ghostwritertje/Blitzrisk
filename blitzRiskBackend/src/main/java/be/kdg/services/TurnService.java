package be.kdg.services;

import be.kdg.dao.*;
import be.kdg.exceptions.IllegalMoveException;
import be.kdg.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * Created by Alexander on 13/2/2015.
 */
@Transactional
@Service("turnService")
public class TurnService {
    static Logger log = Logger.getLogger(TurnService.class);

    @Autowired
    private MoveDao moveDao;

    @Autowired
    private TurnDao turnDao;

    @Autowired
    private TerritoryDao territoryDao;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private GameDao gameDao;

    public void saveTurn(Turn turn) {
        turnDao.updateTurn(turn);
    }

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

    public List<Move> getMoves() {
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
        log.warn("player 1" + player.getId());
        log.warn("turnPlayer:" + turn.getPlayer().getId());
        if(!player.getId().equals(turn.getPlayer().getId())) throw new IllegalMoveException("wrong turn");
        executeTurn(turn, moveList);
        try {
            turn.setCalculatedMoves(moveList);
            turnDao.updateTurn(turn);
            for (Move move : moveList) {
                moveDao.updateMove(move);
            }
            setPlayerTurn(player, PlayerStatus.WAITING);
            return turn;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    private void executeTurn(Turn turn, List<Move> moveList) throws IllegalMoveException {

        Player player = turn.getPlayer();
        for (Move move : moveList) {
            if(!move.getOriginTerritory().getPlayer().getId().equals(player.getId())) {
                log.warn("error: illegal origin territory");
                throw new IllegalMoveException("Illegal origin territory");
            }
            //TODO: nog niet getest?
           /* boolean isNeighbour = false;
            for(Territory territory : move.getOriginTerritory().getNeighbourTerritories()) {
                if(territory.getId().equals(move.getDestinationTerritory().getId())) isNeighbour = true;
            }

            if (!isNeighbour) throw new IllegalMoveException("Destination is not a neighbour");
*/
            if (move.getDestinationTerritory().getPlayer().getId().equals(player.getId())) {
                log.warn("error: can't attack own territory");
                throw new IllegalMoveException("Can't attack own territory");
            }

            if (move.getOriginTerritory().getNumberOfUnits() - move.getNumberOfUnitsToAttack() < 1) {
                log.warn("error: not enough units to attack");
                throw new IllegalMoveException("Not enough units to attack");
            }

            calculateMove(move);
            turn.getMoves().add(move);
        }
    }

    private Move calculateMove(Move move) {
    log.warn("move calculation started");
        try {
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


            //if attacker has won
            if (survivingDefenders <= 0) {
                //surviving attackers will occupy the destination territory
                originTerritoryRemainingNrUnits = originTerritoryStartingNrUnits - attackers;
                move.getDestinationTerritory().setPlayer(move.getOriginTerritory().getPlayer());
                move.getDestinationTerritory().setNumberOfUnits(survivingAttacckers);
                move.setDestinationTerritoryRemainingNrUnits(survivingAttacckers);

                //updated number of units in origin country
                move.setOriginTerritoryRemainingNrUnits(originTerritoryRemainingNrUnits);
                move.getOriginTerritory().setNumberOfUnits(originTerritoryRemainingNrUnits);
            } else {
                //number of units in both territories will be reduced
                move.getDestinationTerritory().setNumberOfUnits(survivingDefenders);
                move.setDestinationTerritoryRemainingNrUnits(survivingDefenders);
                move.getOriginTerritory().setNumberOfUnits(survivingAttacckers);
                move.setOriginTerritoryRemainingNrUnits(survivingAttacckers);
            }
            log.warn("" + move.getDestinationTerritory().getNumberOfUnits());
            log.warn("" + move.getOriginTerritory().getNumberOfUnits());
            return move;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
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
            move.setDestinationTerritoryRemainingNrUnits(reinforcementsTotal);
            move.setOriginTerritoryRemainingNrUnits(reinforcementsTotal);
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
        setPlayerTurn(player, PlayerStatus.ATTACK);
    }

    public void moveUnits(Turn turn, Player player, List<Move> moves) throws IllegalMoveException {
        setPlayerTurn(player, PlayerStatus.WAITING);

    }

    public void setPlayerTurn(Player player, PlayerStatus playerStatus) {
        player.setPlayerStatus(playerStatus);
        playerDao.updatePlayer(player);
        if (playerStatus.equals(PlayerStatus.WAITING)) {
            Game game = player.getGame();

            game.setPlayerTurn(game.getPlayerTurn()+1);
            if(game.getPlayerTurn() >= game.getPlayers().size()) game.setPlayerTurn(0);
            Player newPlayer = game.getPlayers().get(game.getPlayerTurn());
            newPlayer.setPlayerStatus(PlayerStatus.REINFORCE);
            playerDao.updatePlayer(newPlayer);
            gameDao.updateGame(game);
        }
    }
}
