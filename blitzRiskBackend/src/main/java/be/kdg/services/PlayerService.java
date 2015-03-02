package be.kdg.services;

import be.kdg.dao.GameDao;
import be.kdg.dao.PlayerDao;
import be.kdg.model.*;
import be.kdg.exceptions.IllegalUserInviteException;
import be.kdg.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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

    public Player createPlayer (User user, Game game) throws IllegalUserInviteException {
        if(game.getPlayers().size() != 0) {
            for (Player player : game.getPlayers()) {
                if(player.getUser().getId() == user.getId()) {
                    throw new IllegalUserInviteException("User is already in this game");
                }
            }
        }
        Player player = new Player();
        player.setUser(user);
        player.setGame(game);
        player.setColor(game.getPlayers().size());
        player.setInvitationStatus(InvitationStatus.PENDING);
        game.addPlayer(player);
        playerDao.savePlayer(player);

        //gameDao.saveGame(game);
        return player;
    }

    public void save(Player player) {
        playerDao.savePlayer(player);
    }

    public void acceptGame(int playerId) {
        Player player = playerDao.getPlayerById(playerId);
        player.setInvitationStatus(InvitationStatus.ACCEPTED);
        playerDao.updatePlayer(player);

    }

    public Player getPlayerById(int playerId) {
        return playerDao.getPlayerById(playerId);
    }


}
