package be.kdg.services;

import be.kdg.dao.GameDao;
import be.kdg.dao.PlayerDao;
import be.kdg.model.*;
import be.kdg.exceptions.IllegalUserInviteException;
import be.kdg.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Player service provides a service for saving and retrieving players
 */
@Service("playerService")
public class PlayerService {
    private static final Logger logger = Logger.getLogger(PlayerService.class);

    @Autowired
    PlayerDao playerDao;

    @Autowired
    TerritoryService territoryService;

    @Autowired
    GameDao gameDao;

    public Player getPlayer(int playerId) {
        return playerDao.getPlayerById(playerId);
    }

    @Transactional
    public void removePlayer(Player player) {
        playerDao.removePlayer(player);
    }

    @Transactional
    public Player createPlayer(User user, Game game) throws IllegalUserInviteException {
        if (game.getPlayers().size() != 0) {
            for (Player player : game.getPlayers()) {
                if (player.getUser().getId() == user.getId()) {
                    throw new IllegalUserInviteException("User is already in this game");
                }
            }
        }
        Player player = new Player();
        player.setUser(user);
        player.setGame(game);
        player.setColor(game.getPlayers().size());
        player.setInvitationStatus(InvitationStatus.PENDING);
        player.setPlayerStatus(PlayerStatus.WAITING);
        game.addPlayer(player);
        playerDao.savePlayer(player);

        //gameDao.saveGame(game);
        return player;
    }

    @Transactional
    public PlayerStatus getPlayerStatus(Player player) {
        return player.getPlayerStatus();
    }

    @Transactional
    public void save(Player player) {
        playerDao.savePlayer(player);
    }

    @Transactional
    public void updatePlayer(Player player) {
        playerDao.updatePlayer(player);
    }

    @Transactional
    public void acceptGame(int playerId) {
        acceptGameForPlayer(playerId);
    }

    @Transactional
    public Player getPlayerById(int playerId) {
        return playerDao.getPlayerById(playerId);
    }

    @Transactional
    public boolean isPlayerOfUser (User user, int playerId) {
        List<Player> players = playerDao.getPlayersForUser(user);
        boolean isPlayerOfUser = false;
        for (Player player : players) {
            if(player.getId() == (playerId)) {
                isPlayerOfUser = true;
            }
        }
        return isPlayerOfUser;
    }

    private void acceptGameForPlayer(int playerId) {
        Player player = playerDao.getPlayerById(playerId);
        player.setInvitationStatus(InvitationStatus.ACCEPTED);
        playerDao.updatePlayer(player);
    }

    @Transactional
    public void checkIfGameCanStart(int playerId) {
        Player player = playerDao.getPlayerById(playerId);
        Game game = player.getGame();
        //Check if game can begin
        if (!game.isStarted()) {
            boolean ready = true;
            List<Player> gamePlayers = playerDao.getPlayersForGame(game);
            int numberOfPlayers = 0;
            for (Player gamePlayer : gamePlayers) {
                if (!gamePlayer.getInvitationStatus().equals(InvitationStatus.ACCEPTED)) {
                    ready = false;
                }
                numberOfPlayers = numberOfPlayers + 1;
            }
            if (ready && numberOfPlayers > 1) {
                game.setTerritories(new ArrayList<>(territoryService.getTerritories()));
                //   gameDao.saveGame(game);
                game.assignRandomTerritories();
                game.setStarted(true);
                game.getPlayers().get(0).setPlayerStatus(PlayerStatus.REINFORCE);
                gameDao.updateGame(game);
                logger.info("Game " + game.getId() + " has started!");
            }
        }
    }

    @Transactional
    public List<User> getRecentlyPlayed(String username) {
        return playerDao.getRecentlyPlayed(username);
    }
}
