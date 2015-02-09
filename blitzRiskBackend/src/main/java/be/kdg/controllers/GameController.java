package be.kdg.controllers;

import be.kdg.model.Game;
import be.kdg.model.Player;
import be.kdg.model.Territory;
import be.kdg.model.User;
import be.kdg.services.TerritoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.xpath.XPathException;

import java.util.*;

/**
 * Created by Alexander on 6/2/2015.
 */
public class GameController {

    //TODO: autowired??
    private TerritoryService territoryService;

    public Game createNewGame(List<User> users) {


        territoryService = new TerritoryService();
        Random random = new Random();
        Game game = new Game();
        Set<Player> players =  new HashSet<Player>();

        for (User user : users) {
            Player player = new Player();
            player.setUser(user);
            players.add(player);
        }
        game.setPlayers(players);
        game.setTerritories(territoryService.getTerritories());

        ArrayList<Integer> allocatedTerritories = new ArrayList<Integer>();
        int playerCounter = 0;
        Player[] playersArray = game.getPlayers().toArray(new Player[game.getPlayers().size()]);
        int randomInt = 0;

        for (Territory territory : game.getTerritories()) {
            if (((42 - allocatedTerritories.size())/playersArray.length)<1) {
                break;
            }
            do {
                randomInt = random.nextInt(42);
                                                System.out.println(allocatedTerritories.size());
            } while (allocatedTerritories.contains(randomInt));

            Territory[] territoriesArray = game.getTerritories().toArray(new Territory[game.getTerritories().size()]);

            Territory randomTerritory = territoriesArray[randomInt];
            randomTerritory.setPlayer(playersArray[playerCounter++]);
            randomTerritory.setNumberOfUnits(1);
            allocatedTerritories.add(randomInt);

            if (playerCounter+1 > playersArray.length) {
                playerCounter=0;
            }

        }
    return game;
    }
}
