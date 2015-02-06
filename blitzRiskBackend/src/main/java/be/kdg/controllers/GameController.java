package be.kdg.controllers;

import be.kdg.model.Game;
import be.kdg.model.Player;
import be.kdg.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Alexander on 6/2/2015.
 */
public class GameController {

    public void createNewGame(List<User> users) {

        Game game = new Game();
        Set<Player> players =  new HashSet<Player>();

        for (User user : users) {
            Player player = new Player();
            player.setUser(user);
            players.add(player);
        }
        game.setPlayers(players);



    }
}
