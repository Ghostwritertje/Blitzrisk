package be.kdg.services;

import be.kdg.dao.GameDao;
import be.kdg.model.Game;
import be.kdg.model.Player;
import be.kdg.model.Territory;
import be.kdg.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by Alexander on 6/2/2015.
 */


@Service("gameService")
@Transactional
public class GameService {

    @Autowired
    private TerritoryService territoryService;

    @Autowired
    private GameDao gameDao;

    public Game createNewGame(List<User> users) {
        Game game = new Game();
        game.setTerritories(territoryService.getTerritories());
        saveGame(game);
        return game;
    }

    public void addUserstToGame (List<User> users, Game game) {

        List<Player> players =  new ArrayList<>();

        int i = 0;
        for (User user : users) {
            Player player = new Player();
            player.setUser(user);
            player.setColor(i++);
            players.add(player);
        }
        game.setPlayers(players);
        game.setPlayerTurn(0);

        assignRandomTerritories(game);
    }

    public Game assignRandomTerritories(Game game) {


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

    public Player CurrentPlayerforTurn(Game game) {
        return game.getPlayers().get(game.getPlayerTurn());
    }


    public void saveGame(Game game) {
        gameDao.saveGame(game);
    }


    public Game getGame(int gameId) {
        return gameDao.getGame(gameId);
    }


}
