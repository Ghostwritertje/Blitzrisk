package be.kdg.services;

import be.kdg.dao.GameDao;
import be.kdg.dao.PlayerDao;
import be.kdg.dao.TerritoryDao;
import be.kdg.dao.UserDao;
import be.kdg.exceptions.IllegalUserInviteException;
import be.kdg.exceptions.UnAuthorizedActionException;
import be.kdg.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Attack service provides a service for general methods about a game
 */
@Service("gameService")
public class GameService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TerritoryDao territoryDao;

    @Autowired
    private GameDao gameDao;


    @Transactional
    public void removeGame(Game game) {
        gameDao.removeGame(game);
    }

    @Transactional
    public Game saveTerritories(Game game, List<Territory> territories) {
        game.setTerritories(territories);
        game.assignRandomTerritories();
        for (Territory territory: territories) {
            territoryDao.saveTerritory(territory);
        }
        gameDao.updateGame(game);
        return game;
    }

    @Transactional
    public Game createNewGame() {
        Game game = new Game();
        game.setEnded(false);
        game.setPlayerTurn(0);
        for (int i = 1; i< game.getPlayers().size(); i++) {
            game.getPlayers().get(i).setPlayerStatus(PlayerStatus.WAITING);
        }
        gameDao.saveGame(game);
        return game;
    }

    public void addUserToGame(User user, Game game) throws IllegalUserInviteException {
        playerService.createPlayer(user, game);

    }

    @Transactional
    public void updateGame(Game game) {
        gameDao.updateGame(game);
    }

    @Transactional
    public Player inviteUser(String userName, int gameId) throws IllegalUserInviteException {
        User user = userDao.loadUserByUsername(userName);
        if (user != null) {
            return createPlayerForInvite(user.getId(), gameId);
        } else
            return null;
    }

    @Transactional
    public Player inviteRandomUser(int gameId) {
        List<User> users = userDao.findall();
        Random random = new Random();
        Player player = null;
        int counter = 0;
        while (player == null && counter < 10) {
            try {
                counter++;
                player = createPlayerForInvite(users.get(random.nextInt(users.size())).getId(), gameId);
            } catch (IllegalUserInviteException e) {
                e.printStackTrace();
            }
        }
        return player;
    }

    private Player createPlayerForInvite(int userId, int gameId) throws IllegalUserInviteException {
        User user = userDao.loadUserById(userId);
        Game game = gameDao.getGame(gameId);
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
        playerDao.savePlayer(player);
        return player;
    }

    @Transactional
    public List<Player> getPlayers(String username) {
        User user = userDao.loadUserByUsername(username);
        return playerDao.getPlayersForUser(user);

    }

    @Transactional
    public Game getGame(int gameId) {
        return gameDao.getGame(gameId);
    }

    @Transactional
    public void checkUserInGame(int gameId, User user) throws UnAuthorizedActionException {
        Game game = gameDao.getGame(gameId);
        boolean inGame = false;

        for (Player player : game.getPlayers()) {
            if (player.getUser().getId() == user.getId()) {
                inGame = true;
            }
        }

        if (inGame == false) {
            throw new UnAuthorizedActionException("You are not part of this game!");
        }
    }

    @Transactional
    public List<Game> getGames(String username) {
        User user = userDao.loadUserByUsername(username);
        List<Game> games = gameDao.getGamesForUser(user);
        return games;
    }

    @Transactional
    public boolean isGameOfUser(User user, Game game) {
        boolean isPlayer = false;
        for (Player player: user.getPlayers()) {
            for(Player gamePlayer: game.getPlayers()) {
                if (gamePlayer.getId().equals(player.getId())) isPlayer = true;
            }
        }
        return isPlayer;
    }



}
