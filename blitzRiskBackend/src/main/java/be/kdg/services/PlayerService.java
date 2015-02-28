package be.kdg.services;

import be.kdg.dao.GameDao;
import be.kdg.dao.PlayerDao;
import be.kdg.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 *
 * Created by Alexander on 20/2/2015.
 */
@Service("playerService")
@Transactional
public class PlayerService {

    @Autowired
    PlayerDao playerDao;

    @Autowired
    TerritoryService territoryService;

    @Autowired
    GameDao gameDao;

    public Player createPlayer (User user, Game game) {
        Player player = new Player();
        player.setUser(user);
        player.setGame(game);
        player.setColor(game.getPlayers().size());
        player.setInvitationStatus(InvitationStatus.PENDING);
        playerDao.savePlayer(player);
        //game.addPlayer(player);
        //gameDao.saveGame(game);
        return player;
    }

    public Player getPlayer (int playerId) {
        return playerDao.getPlayerById(playerId);
    }

    public void save(Player player) {
        playerDao.savePlayer(player);
    }

    public void acceptGame(int playerId) {
        Player player = playerDao.getPlayerById(playerId);
        player.setInvitationStatus(InvitationStatus.ACCEPTED);
        playerDao.updatePlayer(player);

        //Check if game can begin
        boolean everyoneAccepted = true;
        List<Player> gamePlayers = player.getGame().getPlayers();
        for(Player gamePlayer : gamePlayers){
            if(!gamePlayer.getInvitationStatus().equals(InvitationStatus.ACCEPTED)){
                everyoneAccepted = false;
            }
        }

        if(everyoneAccepted && gamePlayers.size() > 1){
            Game game = player.getGame();
            gameDao.saveGame(game);
            game.assignTerritoriesToPlayers();
            game.setStarted(true);
            gameDao.updateGame(game);
        }
    }


}
