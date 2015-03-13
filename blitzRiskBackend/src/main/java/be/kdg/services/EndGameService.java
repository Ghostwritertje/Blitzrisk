package be.kdg.services;

import be.kdg.dao.GameDao;
import be.kdg.exceptions.GameAlreadyOverException;
import be.kdg.model.Game;
import be.kdg.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marlies on 12/03/2015.
 */

@Transactional
@Service("endGameService")
public class EndGameService {

    @Autowired
    private GameDao gameDao;

    public Player calculateWinner(Game game) {
        List<Player> playerList = new ArrayList<>();
        for(Player player: game.getPlayers()) {
            if (player.getTerritories().size() > 0) playerList.add(player);
        }
        if(playerList.size() == 1) {
            game.setEnded(true);
            gameDao.updateGame(game);
            return playerList.get(0);
        }
        return null;
    }

    public boolean isActive(Player player) {
        if(player.getTerritories().size() > 0) return true;
        else return false;
    }

    public Player getNextActivePlayer(Player player) throws GameAlreadyOverException{
        Game game = player.getGame();
        List<Player> playerList = game.getPlayers();
        int oldPlayerTurn = game.getPlayerTurn();
        int playerTurn = oldPlayerTurn + 1;
        boolean playerFound = false;

        while (!playerFound) {
            if(playerTurn >= playerList.size()) playerTurn = 0;
            if(playerTurn == oldPlayerTurn) throw new GameAlreadyOverException();
            if(isActive(playerList.get(playerTurn))) playerFound = true;
            else playerTurn ++;
        }
        game.setPlayerTurn(playerTurn);
        gameDao.updateGame(game);
        return playerList.get(playerTurn);
    }
}
