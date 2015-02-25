package be.kdg.services;

import be.kdg.dao.PlayerDao;
import be.kdg.model.Game;
import be.kdg.model.InvitationStatus;
import be.kdg.model.Player;
import be.kdg.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by Alexander on 20/2/2015.
 */
@Service("playerService")
@Transactional
public class PlayerService {

    @Autowired
    PlayerDao playerDao;

    public void createPlayer (User user, Game game) {
        Player player = new Player();
        player.setUser(user);
        player.setGame(game);
        player.setInvitationStatus(InvitationStatus.PENDING);
        player.setColor(game.getPlayers().size());
        game.addPlayer(player);
        playerDao.savePlayer(player);
    }

    public Player getPlayerById(int id) {
        return playerDao.getPlayerById(id);
    }

    public void savePlayer(Player player) {
        playerDao.savePlayer(player);
    }
}
