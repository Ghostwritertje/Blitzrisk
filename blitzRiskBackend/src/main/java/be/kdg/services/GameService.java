package be.kdg.services;

import be.kdg.model.Game;
import be.kdg.model.Player;
import be.kdg.model.Territory;
import be.kdg.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Alexander on 6/2/2015.
 */

@Service("gameService")
public class GameService {

    @Autowired
    private TerritoryService territoryService;

    public Game createNewGame(List<User> users) {

        Game game = new Game();
        Set<Player> players =  new HashSet<Player>();

        for (User user : users) {
            Player player = new Player();
            player.setUser(user);
            players.add(player);
        }
        game.setPlayers(players);
        game.setTerritories(territoryService.getTerritories());

        assignRandomTerritories(game);
    return game;
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

            if (i == playerList.size()) {
                i = 0;
            }
        }
        return game;

    }
}
