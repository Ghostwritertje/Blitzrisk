package be.kdg.services;

import be.kdg.dao.GameDao;
import be.kdg.dao.PlayerDao;
import be.kdg.dao.UserDao;
import be.kdg.exceptions.IllegalUserInviteException;
import be.kdg.exceptions.UnAuthorizedActionException;
import be.kdg.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 *
 * Created by Alexander on 6/2/2015.
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
    private TerritoryService territoryService;

    @Autowired
    private GameDao gameDao;

    @Transactional
    public void removeGame(Game game) {
        gameDao.removeGame(game);
    }

    @Transactional
    public void updateGame(Game game) {
        gameDao.saveGame(game);
    }

    @Transactional
    public Game saveTerritories(Game game, List<Territory> territories) {
        game.setTerritories(territories);
        game.assignRandomTerritories();
        gameDao.updateGame(game);
        return game;
    }

    @Transactional
    public Game createNewGame() {
        Game game = new Game();
        //Wordt al gedaan in constructor van game
        // game.setTerritories(territoryService.getTerritories());

        game.setPlayerTurn(0);
        gameDao.saveGame(game);
        return game;
    }

/*    public void addUsersToGame(List<User> users, Game game) {

        for (User user : users) {
            Player player =  playerService.createPlayer(user, game);

        }
      //  game.setPlayers(players);

        assignRandomTerritories(game);
    }*/


    public void addUserToGame(User user, Game game) throws IllegalUserInviteException {
        playerService.createPlayer(user, game);

    }
   /* public Game assignRandomTerritories(Game game) {


        List<Territory> territoryList = new ArrayList<>(game.getTerritories());
        Collections.shuffle(territoryList);

        while(territoryList.size()%game.getPlayers().size() != 0) {
            territoryList.remove(territoryList.size()-1);
        }

        List<Player> playerList = new ArrayList<>(game.getPlayers());
        int i = 0;
        for (Territory territory : territoryList) {
            territory.setPlayer(playerList.get(i++));
            territory.setNumberOfUnits(1);

            if (i == playerList.size()) {
                i = 0;
            }
        }
        return game;
    }
*/
   /* public Player CurrentPlayerforTurn(Game game) {
        return game.getPlayers().get(game.getPlayerTurn());
    }*/

  /*  @Transactional
    public void updateGame(Game game) {
        gameDao.updateGame(game);
    }


    @Transactional
    public Game getGame(int gameId) {
        return gameDao.getGame(gameId);
    }

    @Transactional
    public void removeGame(Game game) {
        gameDao.removeGame(game);
    }
*/

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
        while(player == null && counter < 10) {
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

        for(Player player : game.getPlayers()) {
            if(player.getUser().getId() == user.getId()) {
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
      /*  for(Game game: games){
            game.setPlayers(playerDao.getPlayersForGame(game));
        }*/
        return   games;
    }
}
